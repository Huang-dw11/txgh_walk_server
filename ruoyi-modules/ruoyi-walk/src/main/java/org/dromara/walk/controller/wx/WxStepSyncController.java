package org.dromara.walk.controller.wx;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.walk.domain.WalkScoreRecord;
import org.dromara.walk.domain.WalkStepDaily;
import org.dromara.walk.domain.vo.WalkActivityVo;
import org.dromara.walk.mapper.WalkStepDailyMapper;
import org.dromara.walk.mapper.WalkStepSyncLogMapper;
import org.dromara.walk.service.IWalkActivityService;
import org.dromara.walk.service.IWalkScoreService;
import org.dromara.walk.service.IWalkStepService;
import org.dromara.walk.service.IWeChatService;
import org.dromara.walk.service.impl.WeChatSessionCache;
import org.dromara.walk.utils.WxLoginHelper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wx/steps")
@Tag(name = "小程序步数同步")
public class WxStepSyncController {

    private final IWeChatService weChatService;
    private final IWalkStepService stepService;
    private final IWalkActivityService activityService;
    private final IWalkScoreService scoreService;
    private final WeChatSessionCache sessionCache;
    private final WalkStepSyncLogMapper syncLogMapper;
    private final WalkStepDailyMapper stepDailyMapper;

    @SuppressWarnings("unchecked")
    @Operation(summary = "同步微信运动步数")
    @PostMapping("/sync")
    public R<WalkStepDaily> syncSteps(@RequestBody Map<String, String> params) {
        Long memberId = WxLoginHelper.getMemberId();
        String openid = WxLoginHelper.getOpenid();
        Long activityId = WxLoginHelper.getActivityId();

        log.info("【同步步数】收到请求：memberId={}, activityId={}, openid={}", memberId, activityId, openid);

        if (memberId == null || memberId == 0L) {
            log.warn("【同步步数】未注册用户，拒绝同步：openid={}", openid);
            return R.fail("请先完成报名后再同步步数");
        }

        String sessionKey = sessionCache.get(openid);
        if (sessionKey == null) {
            log.warn("【同步步数】session_key 缓存缺失，openid={}", openid);
            return R.fail("会话已过期，请重新登录");
        }

        String encryptedData = params.get("encryptedData");
        String iv = params.get("iv");

        if (encryptedData == null || iv == null) {
            log.warn("【同步步数】缺少 encryptedData 或 iv 参数");
            return R.fail("参数不完整");
        }

        Map<String, Object> decrypted;
        try {
            decrypted = weChatService.decryptWeRunData(sessionKey, encryptedData, iv);
            log.info("【同步步数】步数数据解密成功");
        } catch (RuntimeException e) {
            log.error("【同步步数】步数数据解密失败：{}", e.getMessage());
            sessionCache.remove(openid);
            return R.fail("session_key 失效，请重新登录");
        }

        List<Map<String, Object>> stepInfoList =
                (List<Map<String, Object>>) decrypted.get("stepInfoList");
        if (stepInfoList == null || stepInfoList.isEmpty()) {
            log.warn("【同步步数】stepInfoList 为空，可能用户未开启微信运动");
            return R.fail("未获取到步数数据，请确保已开启微信运动");
        }

        String todayStr = DateUtils.getDate();

        WalkActivityVo activity = activityService.queryById(activityId);
        if (activity == null) {
            log.warn("【同步步数】活动不存在：activityId={}", activityId);
            return R.fail("活动不存在");
        }
        String activityStart = DateUtils.formatDate(activity.getActivityStartTime());
        String activityEnd = DateUtils.formatDate(activity.getActivityEndTime());
        int bufferDays = activity.getBufferDays() != null ? activity.getBufferDays() : 1;
        String bufferEnd = DateUtils.formatDate(DateUtils.addDays(activity.getActivityEndTime(), bufferDays));

        if (todayStr.compareTo(activityStart) < 0) {
            log.warn("【同步步数】活动尚未开始，拒绝同步：today={}, activityStart={}", todayStr, activityStart);
            return R.fail("活动尚未开始");
        }

        if (todayStr.compareTo(bufferEnd) > 0) {
            log.warn("【同步步数】活动已结束且超过缓冲期，跳过：today={}, activityEnd={}, bufferDays={}, bufferEnd={}",
                    todayStr, activityEnd, bufferDays, bufferEnd);
            return R.fail("活动已结束");
        }

        Map<String, Integer> stepMap = new HashMap<>();
        for (Map<String, Object> entry : stepInfoList) {
            long ts = ((Number) entry.get("timestamp")).longValue();
            int st = ((Number) entry.get("step")).intValue();
            String ds = DateUtils.formatDate(new Date(ts * 1000L));
            stepMap.put(ds, st);
        }

        WalkStepDaily record = null;

        List<WalkStepDaily> unsettledList = stepDailyMapper.selectUnsettledByDate(
                memberId, activityId, activityStart, todayStr);
        log.info("【同步步数】未结算历史天数量：{}", unsettledList.size());
        for (WalkStepDaily unsettled : unsettledList) {
            String dateStr = DateUtils.formatDate(unsettled.getRecordDate());
            if (dateStr.compareTo(activityStart) < 0 || dateStr.compareTo(activityEnd) > 0) {
                log.debug("【同步步数】历史天 {} 不在活动期内（{}~{}），跳过", dateStr, activityStart, activityEnd);
                continue;
            }
            Integer steps = stepMap.get(dateStr);
            if (steps != null) {
                log.info("【同步步数】补录历史天步数：date={}, steps={}, isCheckin={}",
                        dateStr, steps, unsettled.getIsCheckin());
                WalkStepDaily updated = stepService.syncStepsByDate(memberId, activityId, dateStr, steps);
                log.info("【同步步数】历史天补录成功：recordId={}, effectiveSteps={}, isCheckin={}, isTarget={}",
                        updated.getRecordId(), updated.getEffectiveSteps(),
                        updated.getIsCheckin(), updated.getIsTarget());
            }
            scoreService.settleDailyScore(memberId, activityId, dateStr);
            log.info("【同步步数】历史天结算完成：date={}", dateStr);
        }

        Integer todaySteps = stepMap.get(todayStr);
        if (todaySteps != null && todayStr.compareTo(activityEnd) <= 0) {
            log.info("【同步步数】今日原始步数：{}", todaySteps);
            record = stepService.syncSteps(memberId, activityId, todaySteps);
            log.info("【同步步数】今日同步成功：recordId={}, effectiveSteps={}, isCheckin={}, isTarget={}",
                    record.getRecordId(), record.getEffectiveSteps(),
                    record.getIsCheckin(), record.getIsTarget());
            scoreService.handleRealtimeScore(memberId, activityId, todayStr);
            log.info("【同步步数】今日实时处理完成");
        } else if (todaySteps != null) {
            log.info("【同步步数】缓冲期，跳过今日打卡写入：today={}, activityEnd={}", todayStr, activityEnd);
        } else {
            log.warn("【同步步数】stepInfoList 未包含今日数据：todayStr={}", todayStr);
        }

        scoreService.checkAndBreakChain(memberId, activityId);
        log.info("【同步步数】断链兜底检查完成");

        if (record == null) {
            log.info("【同步步数】今日无打卡记录（缓冲期或无步数数据），同步完成");
            return R.ok();
        }

        return R.ok(record);
    }

    @Operation(summary = "获取历史打卡记录")
    @GetMapping("/history")
    public R<Map<String, Object>> history() {
        Long memberId = WxLoginHelper.getMemberId();
        Long activityId = WxLoginHelper.getActivityId();
        log.info("【历史打卡】获取历史记录：memberId={}, activityId={}", memberId, activityId);

        List<WalkStepDaily> stepList = stepService.getMemberStepHistory(memberId, activityId);
        List<WalkScoreRecord> scoreList = scoreService.getMemberScoreRecords(memberId, activityId);
        List<Map<String, Object>> syncRows = syncLogMapper.selectLatestSyncTimeByDate(memberId, activityId);
        Map<String, Date> lastSyncMap = new HashMap<>();
        if (syncRows != null) {
            for (Map<String, Object> row : syncRows) {
                Object dateObj = row.get("sync_date");
                Object timeObj = row.get("last_sync_time");
                if (dateObj != null && timeObj instanceof Date) {
                    lastSyncMap.put(dateObj.toString(), (Date) timeObj);
                }
            }
        }

        List<Map<String, Object>> steps = new ArrayList<>();
        if (stepList != null) {
            for (WalkStepDaily s : stepList) {
                if (s.getRecordDate() == null) continue;
                String dateStr = DateUtils.formatDate(s.getRecordDate());
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("date", dateStr);
                item.put("isCheckin", s.getIsCheckin());
                item.put("isTarget", s.getIsTarget());
                item.put("isSettled", s.getIsSettled());
                item.put("effectiveSteps", s.getEffectiveSteps());
                item.put("checkinTime", s.getCheckinTime());
                Date lastSync = lastSyncMap.get(dateStr);
                if (lastSync == null) {
                    lastSync = s.getUpdateTime() != null ? s.getUpdateTime() : s.getCheckinTime();
                }
                item.put("lastSyncTime", lastSync);
                steps.add(item);
            }
        }

        List<Map<String, Object>> scores = new ArrayList<>();
        if (scoreList != null) {
            for (WalkScoreRecord r : scoreList) {
                if (r.getRelatedDate() == null) continue;
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("date", DateUtils.formatDate(r.getRelatedDate()));
                item.put("scoreType", r.getScoreType());
                item.put("scoreValue", r.getScoreValue());
                scores.add(item);
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("steps", steps);
        result.put("scores", scores);
        log.info("【历史打卡】返回：memberId={}, stepCount={}, scoreCount={}", memberId, steps.size(), scores.size());
        return R.ok(result);
    }
}
