package org.dromara.walk.controller.wx;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.walk.domain.WalkScoreSummary;
import org.dromara.walk.domain.WalkStepDaily;
import org.dromara.walk.domain.vo.WalkActivityVo;
import org.dromara.walk.domain.vo.WalkMemberVo;
import org.dromara.walk.mapper.WalkStepSyncLogMapper;
import org.dromara.walk.service.IWalkActivityService;
import org.dromara.walk.service.IWalkMemberService;
import org.dromara.walk.service.IWalkScoreService;
import org.dromara.walk.service.IWalkStepService;
import org.dromara.walk.utils.WxLoginHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wx")
@Tag(name = "小程序首页")
public class WxHomeController {

    private final IWalkActivityService activityService;
    private final IWalkStepService stepService;
    private final IWalkScoreService scoreService;
    private final IWalkMemberService memberService;
    private final WalkStepSyncLogMapper syncLogMapper;

    @Operation(summary = "首页信息")
    @GetMapping("/home")
    public R<Map<String, Object>> home() {
        Long memberId = WxLoginHelper.getMemberId();
        Long activityId = WxLoginHelper.getActivityId();
        log.info("【首页】获取首页信息：memberId={}, activityId={}", memberId, activityId);

        WalkMemberVo member = memberService.queryById(memberId);
        if (member == null) {
            log.warn("【首页】会员不存在或已被删除：memberId={}", memberId);
            return R.fail("会员不存在，请重新报名或绑定手机号");
        }

        WalkActivityVo activity = activityService.queryById(activityId);
        if (activity == null) {
            log.warn("【首页】活动不存在：activityId={}", activityId);
            return R.fail("活动不存在");
        }

        WalkStepDaily todaySteps = stepService.getTodaySteps(memberId, activityId);
        Map<String, Object> homeData = stepService.getHomeData(memberId, activityId);
        WalkScoreSummary scoreSummary = scoreService.getScoreSummary(memberId, activityId);

        int todaySyncCount = syncLogMapper.countByDate(memberId, activityId, DateUtils.getDate());

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("activity", activity);
        result.put("homeData", homeData);
        result.put("todaySteps", todaySteps);
        result.put("isCheckedIn", todaySteps != null && "Y".equals(todaySteps.getIsCheckin()));
        result.put("isTarget", todaySteps != null && "Y".equals(todaySteps.getIsTarget()));
        result.put("todaySyncCount", todaySyncCount);
        result.put("totalScore", scoreSummary != null ? scoreSummary.getTotalScore() : 0);
        result.put("streak", scoreSummary != null ? scoreSummary.getCurrentContinuousDays() : 0);
        log.info("【首页】首页信息返回成功：memberId={}, totalScore={}, streak={}",
                memberId, result.get("totalScore"), result.get("streak"));
        return R.ok(result);
    }

    @Deprecated
    @Operation(summary = "打卡（已废弃）")
    @PostMapping("/checkin")
    public R<WalkStepDaily> checkin() {
        Long memberId = WxLoginHelper.getMemberId();
        Long activityId = WxLoginHelper.getActivityId();
        log.info("【打卡-已废弃】收到请求：memberId={}, activityId={}", memberId, activityId);
        WalkStepDaily record = stepService.checkin(memberId, activityId);
        log.info("【打卡-已废弃】成功：memberId={}, recordId={}, isCheckin={}",
                memberId, record.getRecordId(), record.getIsCheckin());
        return R.ok(record);
    }
}
