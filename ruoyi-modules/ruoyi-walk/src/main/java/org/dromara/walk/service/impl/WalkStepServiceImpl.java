package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.system.domain.SysDept;
import org.dromara.system.mapper.SysDeptMapper;
import org.dromara.walk.domain.*;
import org.dromara.walk.mapper.*;
import org.dromara.walk.service.IWalkStepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkStepServiceImpl implements IWalkStepService {

    private final WalkStepDailyMapper stepDailyMapper;
    private final WalkStepSyncLogMapper syncLogMapper;
    private final WalkMemberMapper memberMapper;
    private final WalkActivityMapper activityMapper;
    private final SysDeptMapper sysDeptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalkStepDaily syncSteps(Long memberId, Long activityId, Integer wechatSteps) {
        WalkActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new ServiceException("活动不存在");
        }

        String today = DateUtils.getDate();
        int maxSteps = activity.getDailyMaxSteps() != null ? activity.getDailyMaxSteps() : 15000;
        int effectiveSteps = Math.min(wechatSteps, maxSteps);
        int targetSteps = activity.getDailyTargetSteps() != null ? activity.getDailyTargetSteps() : 7000;
        String isTarget = effectiveSteps >= targetSteps ? "Y" : "N";

        WalkStepDaily record = stepDailyMapper.selectMemberStepByDate(memberId, activityId, today);
        if (record != null) {
            if ("Y".equals(record.getIsLocked())) {
                return record;
            }
            record.setWechatSteps(wechatSteps);
            record.setEffectiveSteps(effectiveSteps);
            record.setIsTarget(isTarget);
            if ("N".equals(record.getIsCheckin())) {
                record.setIsCheckin("Y");
                record.setCheckinTime(DateUtils.getNowDate());
            }
            stepDailyMapper.updateById(record);
        } else {
            record = new WalkStepDaily();
            record.setActivityId(activityId);
            record.setMemberId(memberId);
            record.setRecordDate(DateUtils.parseDate(today));
            record.setWechatSteps(wechatSteps);
            record.setEffectiveSteps(effectiveSteps);
            record.setIsTarget(isTarget);
            record.setIsCheckin("Y");
            record.setCheckinTime(DateUtils.getNowDate());
            record.setIsLocked("N");
            stepDailyMapper.insert(record);
        }

        WalkStepSyncLog syncLog = new WalkStepSyncLog();
        syncLog.setMemberId(memberId);
        syncLog.setActivityId(activityId);
        syncLog.setSyncDate(DateUtils.parseDate(today));
        syncLog.setSyncSteps(wechatSteps);
        syncLog.setSyncType("1");
        syncLog.setSyncTime(DateUtils.getNowDate());
        syncLogMapper.insert(syncLog);

        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalkStepDaily syncStepsByDate(Long memberId, Long activityId, String recordDate, Integer wechatSteps) {
        WalkActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new ServiceException("活动不存在");
        }

        int maxSteps = activity.getDailyMaxSteps() != null ? activity.getDailyMaxSteps() : 15000;
        int effectiveSteps = Math.min(wechatSteps, maxSteps);
        int targetSteps = activity.getDailyTargetSteps() != null ? activity.getDailyTargetSteps() : 7000;
        String isTarget = effectiveSteps >= targetSteps ? "Y" : "N";

        WalkStepDaily record = stepDailyMapper.selectMemberStepByDate(memberId, activityId, recordDate);
        if (record != null) {
            if ("Y".equals(record.getIsLocked())) {
                return record;
            }
            record.setWechatSteps(wechatSteps);
            record.setEffectiveSteps(effectiveSteps);
            record.setIsTarget(isTarget);
            stepDailyMapper.updateById(record);
        } else {
            record = new WalkStepDaily();
            record.setActivityId(activityId);
            record.setMemberId(memberId);
            record.setRecordDate(DateUtils.parseDate(recordDate));
            record.setWechatSteps(wechatSteps);
            record.setEffectiveSteps(effectiveSteps);
            record.setIsTarget(isTarget);
            record.setIsCheckin("N");
            record.setIsLocked("N");
            stepDailyMapper.insert(record);
        }

        WalkStepSyncLog syncLog = new WalkStepSyncLog();
        syncLog.setMemberId(memberId);
        syncLog.setActivityId(activityId);
        syncLog.setSyncDate(DateUtils.parseDate(recordDate));
        syncLog.setSyncSteps(wechatSteps);
        syncLog.setSyncType("2");
        syncLog.setSyncTime(DateUtils.getNowDate());
        syncLogMapper.insert(syncLog);

        record.setIsLocked("Y");
        stepDailyMapper.updateById(record);

        return record;
    }

    @Override
    public WalkStepDaily checkin(Long memberId, Long activityId) {
        String today = DateUtils.getDate();
        WalkStepDaily record = stepDailyMapper.selectMemberStepByDate(memberId, activityId, today);
        if (record != null) {
            if ("N".equals(record.getIsCheckin())) {
                record.setIsCheckin("Y");
                record.setCheckinTime(DateUtils.getNowDate());
                stepDailyMapper.updateById(record);
            }
            return record;
        }
        record = new WalkStepDaily();
        record.setActivityId(activityId);
        record.setMemberId(memberId);
        record.setRecordDate(DateUtils.parseDate(today));
        record.setWechatSteps(0);
        record.setEffectiveSteps(0);
        record.setIsTarget("N");
        record.setIsCheckin("Y");
        record.setCheckinTime(DateUtils.getNowDate());
        record.setIsLocked("N");
        stepDailyMapper.insert(record);
        return record;
    }

    @Override
    public WalkStepDaily getTodaySteps(Long memberId, Long activityId) {
        String today = DateUtils.getDate();
        WalkStepDaily record = stepDailyMapper.selectMemberStepByDate(memberId, activityId, today);
        if (record == null) {
            record = new WalkStepDaily();
            record.setRecordDate(DateUtils.parseDate(today));
            record.setWechatSteps(0);
            record.setEffectiveSteps(0);
            record.setIsTarget("N");
            record.setIsCheckin("N");
        }
        return record;
    }

    @Override
    public List<WalkStepDaily> getDailyRanking(Long activityId, String recordDate) {
        if (recordDate == null) {
            recordDate = DateUtils.getDate();
        }
        return stepDailyMapper.selectDailyRanking(activityId, recordDate);
    }

    @Override
    public List<WalkStepDaily> getMemberStepHistory(Long memberId, Long activityId) {
        LambdaQueryWrapper<WalkStepDaily> lqw = Wrappers.lambdaQuery();
        lqw.eq(WalkStepDaily::getMemberId, memberId);
        lqw.eq(WalkStepDaily::getActivityId, activityId);
        lqw.orderByAsc(WalkStepDaily::getRecordDate);
        return stepDailyMapper.selectList(lqw);
    }

    @Override
    public void autoCheckin(Long memberId, Long activityId) {
        String today = DateUtils.getDate();
        WalkStepDaily record = stepDailyMapper.selectMemberStepByDate(memberId, activityId, today);
        if (record == null) {
            record = new WalkStepDaily();
            record.setActivityId(activityId);
            record.setMemberId(memberId);
            record.setRecordDate(DateUtils.parseDate(today));
            record.setWechatSteps(0);
            record.setEffectiveSteps(0);
            record.setIsTarget("N");
            record.setIsCheckin("Y");
            record.setCheckinTime(DateUtils.getNowDate());
            record.setIsLocked("N");
            stepDailyMapper.insert(record);
        } else if ("N".equals(record.getIsCheckin())) {
            record.setIsCheckin("Y");
            record.setCheckinTime(DateUtils.getNowDate());
            stepDailyMapper.updateById(record);
        }
    }

    @Override
    public Map<String, Object> getHomeData(Long memberId, Long activityId) {
        WalkMember member = memberMapper.selectById(memberId);
        if (member == null) {
            return Collections.emptyMap();
        }

        WalkStepDaily todaySteps = getTodaySteps(memberId, activityId);
        LambdaQueryWrapper<WalkStepDaily> lqw = Wrappers.lambdaQuery();
        lqw.eq(WalkStepDaily::getMemberId, memberId);
        lqw.eq(WalkStepDaily::getActivityId, activityId);
        List<WalkStepDaily> allRecords = stepDailyMapper.selectList(lqw);

        long checkinDays = allRecords.stream().filter(r -> "Y".equals(r.getIsCheckin()) && "Y".equals(r.getIsTarget())).count();
        long totalSteps = allRecords.stream().mapToLong(WalkStepDaily::getEffectiveSteps).sum();

        String deptName = "";
        if (member.getDeptId() != null) {
            SysDept dept = sysDeptMapper.selectById(member.getDeptId());
            deptName = dept != null ? dept.getDeptName() : "";
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("realName", member.getRealName());
        data.put("deptId", member.getDeptId());
        data.put("deptName", deptName);
        data.put("todaySteps", todaySteps);
        data.put("totalCheckinDays", checkinDays);
        data.put("totalEffectiveSteps", totalSteps);
        return data;
    }
}
