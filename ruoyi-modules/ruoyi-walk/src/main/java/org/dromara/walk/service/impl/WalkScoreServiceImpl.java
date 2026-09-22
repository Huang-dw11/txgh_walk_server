package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.enums.FormatsType;
import org.dromara.walk.domain.*;
import org.dromara.walk.mapper.*;
import org.dromara.walk.service.IWalkScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkScoreServiceImpl implements IWalkScoreService {

    private final WalkScoreRecordMapper scoreRecordMapper;
    private final WalkScoreSummaryMapper scoreSummaryMapper;
    private final WalkContinuousCheckinMapper continuousCheckinMapper;
    private final WalkStepDailyMapper stepDailyMapper;
    private final WalkActivityMapper activityMapper;

    @Override
    public WalkScoreSummary getScoreSummary(Long memberId, Long activityId) {
        return scoreSummaryMapper.selectByMember(activityId, memberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleRealtimeScore(Long memberId, Long activityId, String recordDate) {
        WalkActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            log.warn("【积分引擎-实时】活动不存在，跳过：activityId={}, memberId={}, date={}", activityId, memberId, recordDate);
            return;
        }

        WalkStepDaily stepDaily = stepDailyMapper.selectMemberStepByDate(memberId, activityId, recordDate);
        if (stepDaily == null) {
            log.debug("【积分引擎-实时】无步数记录，跳过：memberId={}, activityId={}, date={}", memberId, activityId, recordDate);
            return;
        }
        if ("N".equals(stepDaily.getIsCheckin()) || "N".equals(stepDaily.getIsTarget())) {
            log.info("【积分引擎-实时】今日未达标或未打卡，等隔日同步再判：memberId={}, date={}", memberId, recordDate);
            return;
        }

        // 缓冲期当日不计基础分
        Date recordDateObj = DateUtils.parseDate(recordDate);
        if (activity.getActivityEndTime() != null && recordDateObj.after(activity.getActivityEndTime())) {
            log.info("【积分引擎-实时】该日已过活动期（缓冲期），不计基础分：memberId={}, date={}", memberId, recordDate);
            stepDailyMapper.markSettled(memberId, activityId, recordDate);
            return;
        }

        // 幂等：已发过基础分则跳过
        WalkScoreRecord existing = scoreRecordMapper.selectScoreRecordUnique(memberId, "1", recordDate);
        if (existing != null) {
            log.debug("【积分引擎-实时】基础积分已存在，跳过：memberId={}, date={}", memberId, recordDate);
            return;
        }

        WalkScoreRecord baseScore = new WalkScoreRecord();
        baseScore.setActivityId(activityId);
        baseScore.setMemberId(memberId);
        baseScore.setScoreType("1");
        baseScore.setScoreValue(1);
        baseScore.setRelatedDate(recordDateObj);
        scoreRecordMapper.insert(baseScore);
        log.info("【积分引擎-实时】基础积分+1：memberId={}, date={}", memberId, recordDate);

        handleContinuousCheckin(memberId, activityId, recordDate, activity);
        refreshScoreSummary(memberId, activityId);
        stepDailyMapper.markSettled(memberId, activityId, recordDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleDailyScore(Long memberId, Long activityId, String recordDate) {
        WalkActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            log.warn("【积分引擎-结算】活动不存在，跳过：activityId={}, memberId={}, date={}", activityId, memberId, recordDate);
            return;
        }

        WalkStepDaily stepDaily = stepDailyMapper.selectMemberStepByDate(memberId, activityId, recordDate);
        if (stepDaily == null) {
            log.debug("【积分引擎-结算】无步数记录（缺勤天），跳过：memberId={}, date={}", memberId, recordDate);
            return;
        }

        if ("Y".equals(stepDaily.getIsSettled())) {
            log.debug("【积分引擎-结算】该天已结算，跳过：memberId={}, date={}", memberId, recordDate);
            return;
        }

        // 缓冲期当日不计基础分
        Date recordDateObj = DateUtils.parseDate(recordDate);
        if (activity.getActivityEndTime() != null && recordDateObj.after(activity.getActivityEndTime())) {
            log.info("【积分引擎-结算】该日已过活动期（缓冲期），不计基础分：memberId={}, date={}", memberId, recordDate);
            stepDailyMapper.markSettled(memberId, activityId, recordDate);
            return;
        }

        if ("Y".equals(stepDaily.getIsCheckin()) && "Y".equals(stepDaily.getIsTarget())) {
            WalkScoreRecord existing = scoreRecordMapper.selectScoreRecordUnique(memberId, "1", recordDate);
            if (existing == null) {
                WalkScoreRecord baseScore = new WalkScoreRecord();
                baseScore.setActivityId(activityId);
                baseScore.setMemberId(memberId);
                baseScore.setScoreType("1");
                baseScore.setScoreValue(1);
                baseScore.setRelatedDate(recordDateObj);
                scoreRecordMapper.insert(baseScore);
                log.info("【积分引擎-结算】补发基础积分+1：memberId={}, date={}", memberId, recordDate);
            }
            handleContinuousCheckin(memberId, activityId, recordDate, activity);
            refreshScoreSummary(memberId, activityId);
        } else if ("Y".equals(stepDaily.getIsCheckin()) && "N".equals(stepDaily.getIsTarget())) {
            log.info("【积分引擎-结算】打卡但未达标，触发断裂：memberId={}, date={}", memberId, recordDate);
            handleBreak(memberId, activityId, recordDate);
            refreshScoreSummary(memberId, activityId);
        } else {
            log.warn("【积分引擎-结算】异常状态，按断链处理：memberId={}, date={}", memberId, recordDate);
            handleBreak(memberId, activityId, recordDate);
            refreshScoreSummary(memberId, activityId);
        }

        stepDailyMapper.markSettled(memberId, activityId, recordDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkAndBreakChain(Long memberId, Long activityId) {
        WalkContinuousCheckin current = continuousCheckinMapper.selectCurrentContinuous(memberId, activityId);
        if (current == null) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(current.getStartDate());
        cal.add(Calendar.DAY_OF_MONTH, current.getContinuousDays() - 1);
        Date lastCheckinDate = cal.getTime();

        String todayStr = DateUtils.getDate();
        Date today = DateUtils.parseDate(todayStr);

        cal.setTime(lastCheckinDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date expectedNextDay = cal.getTime();

        if (expectedNextDay.before(today)) {
            String expectedStr = DateUtils.parseDateToStr(FormatsType.YYYY_MM_DD, expectedNextDay);
            log.info("【积分引擎-断链兜底】检测到缺勤，触发断裂：memberId={}, 期望次日={}", memberId, expectedStr);
            handleBreak(memberId, activityId, expectedStr);
            refreshScoreSummary(memberId, activityId);
        }
    }

    private void handleBreak(Long memberId, Long activityId, String recordDate) {
        WalkContinuousCheckin current = continuousCheckinMapper.selectCurrentContinuous(memberId, activityId);
        if (current == null) {
            return;
        }

        Date breakDate = DateUtils.parseDate(recordDate);
        if (breakDate.before(current.getStartDate())) {
            return;
        }

        updateMaxContinuousIfNeeded(memberId, activityId, current.getContinuousDays());

        current.setIsBroken("Y");
        current.setBrokenDate(breakDate);
        continuousCheckinMapper.breakContinuousCheckin(current);
    }

    private void updateMaxContinuousIfNeeded(Long memberId, Long activityId, int continuousDays) {
        if (continuousDays <= 0) {
            return;
        }
        WalkScoreSummary summary = scoreSummaryMapper.selectByMember(activityId, memberId);
        if (summary == null) {
            return;
        }
        if (continuousDays > summary.getMaxContinuousDays()) {
            summary.setMaxContinuousDays(continuousDays);
            scoreSummaryMapper.updateById(summary);
        }
    }

    private void handleContinuousCheckin(Long memberId, Long activityId, String recordDate, WalkActivity activity) {
        WalkContinuousCheckin current = continuousCheckinMapper.selectCurrentContinuous(memberId, activityId);
        Date date = DateUtils.parseDate(recordDate);

        if (current == null) {
            startNewChain(memberId, activityId, date);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(current.getStartDate());
        cal.add(Calendar.DAY_OF_MONTH, current.getContinuousDays() - 1);
        Date lastCheckinDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date expectedDate = cal.getTime();

        if (!date.equals(expectedDate)) {
            updateMaxContinuousIfNeeded(memberId, activityId, current.getContinuousDays());
            current.setIsBroken("Y");
            current.setBrokenDate(expectedDate);
            continuousCheckinMapper.breakContinuousCheckin(current);
            startNewChain(memberId, activityId, date);
            return;
        }

        current.setContinuousDays(current.getContinuousDays() + 1);
        int days = current.getContinuousDays();
        continuousCheckinMapper.updateById(current);
        log.info("【连续签到】连续天数+1：memberId={}, days={}", memberId, days);

        checkAndGrantReward(memberId, activityId, current, days, activity);
    }

    private void startNewChain(Long memberId, Long activityId, Date startDate) {
        WalkContinuousCheckin newRecord = new WalkContinuousCheckin();
        newRecord.setActivityId(activityId);
        newRecord.setMemberId(memberId);
        newRecord.setStartDate(startDate);
        newRecord.setContinuousDays(1);
        newRecord.setIsBroken("N");
        newRecord.setReward7daysCount(0);
        newRecord.setReward14daysCount(0);
        newRecord.setReward21daysReceived("N");
        continuousCheckinMapper.insert(newRecord);
    }

    /**
     * 连续奖励发放（修复版）
     * - 21天奖励：用 reward21daysReceived 标志做幂等（移除了原先 broken 的 "21days" 查询）
     * - 7/14天奖励：用全局积分记录计数做单周期上限校验（跨所有链统计，而非仅当前链）
     */
    private void checkAndGrantReward(Long memberId, Long activityId, WalkContinuousCheckin record, int days, WalkActivity activity) {
        if (days >= 21 && "N".equals(record.getReward21daysReceived())) {
            grantReward(memberId, activityId, "4", activity.getReward21days(), null, record.getRecordId());
            record.setReward21daysReceived("Y");
            continuousCheckinMapper.updateById(record);
            log.info("【连续签到】21天奖励发放：memberId={}, score={}", memberId, activity.getReward21days());
        }

        if (days % 7 == 0 && days < 21) {
            int max7 = activity.getReward7daysMaxCount() != null ? activity.getReward7daysMaxCount() : 3;
            int max14 = activity.getReward14daysMaxCount() != null ? activity.getReward14daysMaxCount() : 2;

            List<WalkScoreRecord> allRecords = scoreRecordMapper.selectMemberScoreRecords(memberId, activityId);
            int globalCount7 = (int) allRecords.stream().filter(r -> "2".equals(r.getScoreType())).count();
            int globalCount14 = (int) allRecords.stream().filter(r -> "3".equals(r.getScoreType())).count();

            if (days == 7 && globalCount7 < max7) {
                grantReward(memberId, activityId, "2", activity.getReward7days(), null, record.getRecordId());
                record.setReward7daysCount(record.getReward7daysCount() + 1);
                continuousCheckinMapper.updateById(record);
                log.info("【连续签到】7天奖励发放：memberId={}, score={}, 全局已发={}/{}", memberId, activity.getReward7days(), globalCount7 + 1, max7);
            }
            if (days == 14 && globalCount14 < max14) {
                grantReward(memberId, activityId, "3", activity.getReward14days(), null, record.getRecordId());
                record.setReward14daysCount(record.getReward14daysCount() + 1);
                continuousCheckinMapper.updateById(record);
                log.info("【连续签到】14天奖励发放：memberId={}, score={}, 全局已发={}/{}", memberId, activity.getReward14days(), globalCount14 + 1, max14);
            }
        }
    }

    private void grantReward(Long memberId, Long activityId, String scoreType, Integer scoreValue, String relatedDate, Long continuousRecordId) {
        if (scoreValue == null || scoreValue <= 0) {
            return;
        }
        WalkScoreRecord record = new WalkScoreRecord();
        record.setActivityId(activityId);
        record.setMemberId(memberId);
        record.setScoreType(scoreType);
        record.setScoreValue(scoreValue);
        if (relatedDate != null) {
            record.setRelatedDate(DateUtils.parseDate(relatedDate));
        }
        record.setContinuousRecordId(continuousRecordId);
        scoreRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshScoreSummary(Long memberId, Long activityId) {
        WalkScoreSummary summary = scoreSummaryMapper.selectByMember(activityId, memberId);
        if (summary == null) {
            summary = new WalkScoreSummary();
            summary.setActivityId(activityId);
            summary.setMemberId(memberId);
            summary.setBaseScore(0);
            summary.setTotalScore(0);
            summary.setTotalCheckinDays(0);
            summary.setCurrentContinuousDays(0);
            summary.setMaxContinuousDays(0);
            summary.setTotalEffectiveSteps(0);
            scoreSummaryMapper.insert(summary);
        }

        List<WalkScoreRecord> records = scoreRecordMapper.selectMemberScoreRecords(memberId, activityId);
        int baseScore = 0, reward7 = 0, reward14 = 0, reward21 = 0;
        for (WalkScoreRecord r : records) {
            switch (r.getScoreType()) {
                case "1" -> baseScore += r.getScoreValue();
                case "2" -> reward7 += r.getScoreValue();
                case "3" -> reward14 += r.getScoreValue();
                case "4" -> reward21 += r.getScoreValue();
            }
        }

        WalkContinuousCheckin current = continuousCheckinMapper.selectCurrentContinuous(memberId, activityId);
        int continuousDays = current != null ? current.getContinuousDays() : 0;

        LambdaQueryWrapper<WalkStepDaily> lqw = Wrappers.lambdaQuery();
        lqw.eq(WalkStepDaily::getMemberId, memberId);
        lqw.eq(WalkStepDaily::getActivityId, activityId);
        List<WalkStepDaily> steps = stepDailyMapper.selectList(lqw);
        long checkinDays = steps.stream().filter(r -> "Y".equals(r.getIsCheckin()) && "Y".equals(r.getIsTarget())).count();
        long totalSteps = steps.stream().mapToLong(WalkStepDaily::getEffectiveSteps).sum();

        summary.setBaseScore(baseScore);
        summary.setReward7daysScore(reward7);
        summary.setReward14daysScore(reward14);
        summary.setReward21daysScore(reward21);
        summary.setTotalScore(baseScore + reward7 + reward14 + reward21);
        summary.setTotalCheckinDays((int) checkinDays);
        summary.setCurrentContinuousDays(continuousDays);
        if (continuousDays > summary.getMaxContinuousDays()) {
            summary.setMaxContinuousDays(continuousDays);
        }
        summary.setTotalEffectiveSteps((int) totalSteps);
        scoreSummaryMapper.updateById(summary);
    }

    @Override
    public List<WalkScoreSummary> getScoreRanking(Long activityId) {
        return scoreSummaryMapper.selectScoreRanking(activityId);
    }

    @Override
    public List<WalkScoreRecord> getMemberScoreRecords(Long memberId, Long activityId) {
        return scoreRecordMapper.selectMemberScoreRecords(memberId, activityId);
    }
}
