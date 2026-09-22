package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.enums.FormatsType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.*;
import org.dromara.walk.domain.vo.WalkDeptVo;
import org.dromara.walk.domain.vo.WalkOrgRankingVo;
import org.dromara.walk.domain.vo.WalkRankingVo;
import org.dromara.walk.mapper.*;
import org.dromara.walk.service.IWalkRankingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkRankingServiceImpl implements IWalkRankingService {

    private final WalkStepDailyMapper stepDailyMapper;
    private final WalkScoreSummaryMapper scoreSummaryMapper;
    private final WalkMemberMapper memberMapper;
    private final WalkDeptMapper walkDeptMapper;
    private final WalkWinnerMapper winnerMapper;
    private final WalkActivityMapper activityMapper;
    private final WalkRankingMapper rankingMapper;
    private final WalkOrgRankingMapper orgRankingMapper;

    @Override
    public TableDataInfo<WalkRankingVo> getDailyStepRanking(Long activityId, String recordDate, PageQuery pageQuery) {
        if (recordDate == null) {
            recordDate = DateUtils.getDate();
        }
        List<WalkStepDaily> stepList = stepDailyMapper.selectDailyRanking(activityId, recordDate);
        List<WalkRankingVo> allRankings = new ArrayList<>();
        int rank = 1;
        for (WalkStepDaily step : stepList) {
            WalkRankingVo r = new WalkRankingVo();
            r.setRankNo(rank++);
            r.setMemberId(step.getMemberId());
            r.setRankValue(step.getEffectiveSteps());
            r.setRecordDate(step.getRecordDate());
            enrichRankingVo(r);
            allRankings.add(r);
        }
        // 内存分页（已按 effective_steps desc 排序，rank_no 即时计算）
        int pageNum = pageQuery.getPageNum();
        int pageSize = pageQuery.getPageSize();
        int total = allRankings.size();
        int fromIndex = Math.min((pageNum - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<WalkRankingVo> pageList = fromIndex < toIndex ? allRankings.subList(fromIndex, toIndex) : new ArrayList<>();

        Page<WalkRankingVo> page = new Page<>(pageNum, pageSize);
        page.setTotal(total);
        page.setRecords(pageList);
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<WalkRankingVo> getTotalStepRanking(Long activityId, PageQuery pageQuery) {
        Page<WalkRankingVo> page = pageQuery.build();
        IPage<WalkRankingVo> result = rankingMapper.selectRankingByType(page, activityId, "2", null);
        result.getRecords().forEach(this::enrichRankingVo);
        return TableDataInfo.build(result);
    }

    @Override
    public TableDataInfo<WalkRankingVo> getScoreRanking(Long activityId, PageQuery pageQuery) {
        Page<WalkRankingVo> page = pageQuery.build();
        IPage<WalkRankingVo> result = rankingMapper.selectRankingByType(page, activityId, "3", null);
        result.getRecords().forEach(this::enrichRankingVo);
        return TableDataInfo.build(result);
    }

    private void enrichRankingVo(WalkRankingVo r) {
        if (r.getMemberId() != null) {
            WalkMember member = memberMapper.selectById(r.getMemberId());
            if (member != null) {
                r.setRealName(member.getRealName());
                r.setDeptId(member.getDeptId());
                if (member.getDeptId() != null) {
                    WalkDeptVo dept = walkDeptMapper.selectWalkDeptById(member.getDeptId());
                    if (dept != null) {
                        r.setDeptName(dept.getDeptName());
                    }
                }
            }
        }
    }

    // ===== 全量版（wx 小程序端 / 数据导出用）=====

    @Override
    public List<WalkRankingVo> getDailyStepRankingList(Long activityId, String recordDate) {
        if (recordDate == null) {
            recordDate = DateUtils.getDate();
        }
        List<WalkStepDaily> stepList = stepDailyMapper.selectDailyRanking(activityId, recordDate);
        List<WalkRankingVo> rankings = new ArrayList<>();
        int rank = 1;
        for (WalkStepDaily step : stepList) {
            WalkRankingVo r = new WalkRankingVo();
            r.setRankNo(rank++);
            r.setMemberId(step.getMemberId());
            r.setRankValue(step.getEffectiveSteps());
            r.setRecordDate(step.getRecordDate());
            enrichRankingVo(r);
            rankings.add(r);
        }
        return rankings;
    }

    @Override
    public List<WalkRankingVo> getTotalStepRankingList(Long activityId) {
        List<WalkRankingVo> rankings = rankingMapper.selectAllRankingByType(activityId, "2", null);
        rankings.forEach(this::enrichRankingVo);
        return rankings;
    }

    @Override
    public List<WalkRankingVo> getScoreRankingList(Long activityId) {
        List<WalkRankingVo> rankings = rankingMapper.selectAllRankingByType(activityId, "3", null);
        rankings.forEach(this::enrichRankingVo);
        return rankings;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshTotalRankings(Long activityId) {
        refreshStepRanking(activityId);
        refreshScoreRanking(activityId);
        refreshOrgRankings(activityId);
    }

    private void refreshStepRanking(Long activityId) {
        rankingMapper.deleteRankingByType(activityId, "2", null);

        LambdaQueryWrapper<WalkMember> memberLqw = Wrappers.lambdaQuery();
        memberLqw.eq(WalkMember::getActivityId, activityId);
        memberLqw.eq(WalkMember::getSignupStatus, "1");
        memberLqw.ne(WalkMember::getAbnormalFlag, "1");
        List<WalkMember> members = memberMapper.selectList(memberLqw);

        List<WalkRanking> rankings = new ArrayList<>();
        for (WalkMember member : members) {
            LambdaQueryWrapper<WalkStepDaily> stepLqw = Wrappers.lambdaQuery();
            stepLqw.eq(WalkStepDaily::getMemberId, member.getMemberId());
            stepLqw.eq(WalkStepDaily::getActivityId, activityId);
            List<WalkStepDaily> steps = stepDailyMapper.selectList(stepLqw);
            int totalSteps = steps.stream().mapToInt(WalkStepDaily::getEffectiveSteps).sum();
            if (totalSteps <= 0) {
                continue;
            }

            WalkRanking r = new WalkRanking();
            r.setActivityId(activityId);
            r.setRankingType("2");
            r.setMemberId(member.getMemberId());
            r.setRankValue(totalSteps);
            r.setDeptId(member.getDeptId());
            rankings.add(r);
        }

        rankings.sort((a, b) -> Integer.compare(b.getRankValue(), a.getRankValue()));
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRankNo(i + 1);
        }

        if (!rankings.isEmpty()) {
            rankings.forEach(r -> r.setRankingId(IdWorker.getId()));
            rankingMapper.batchInsertRanking(rankings);
        }
    }

    private void refreshScoreRanking(Long activityId) {
        rankingMapper.deleteRankingByType(activityId, "3", null);

        List<WalkScoreSummary> summaries = scoreSummaryMapper.selectScoreRanking(activityId);
        List<WalkRanking> rankings = new ArrayList<>();
        int rank = 1;
        for (WalkScoreSummary s : summaries) {
            WalkMember member = memberMapper.selectById(s.getMemberId());
            if (member == null || "2".equals(member.getSignupStatus()) || "3".equals(member.getSignupStatus()) || "1".equals(member.getAbnormalFlag())) {
                continue;
            }
            WalkRanking r = new WalkRanking();
            r.setActivityId(activityId);
            r.setRankingType("3");
            r.setMemberId(s.getMemberId());
            r.setRankNo(rank++);
            r.setRankValue(s.getTotalScore());
            r.setDeptId(member.getDeptId());
            rankings.add(r);
        }

        if (!rankings.isEmpty()) {
            rankings.forEach(r -> r.setRankingId(IdWorker.getId()));
            rankingMapper.batchInsertRanking(rankings);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshOrgRankings(Long activityId) {
        orgRankingMapper.deleteOrgRankingByActivity(activityId);

        LambdaQueryWrapper<WalkMember> memberLqw = Wrappers.lambdaQuery();
        memberLqw.eq(WalkMember::getActivityId, activityId);
        memberLqw.eq(WalkMember::getSignupStatus, "1");
        memberLqw.ne(WalkMember::getAbnormalFlag, "1");
        List<WalkMember> members = memberMapper.selectList(memberLqw);
        Map<Long, List<WalkMember>> deptMembers = members.stream()
            .collect(Collectors.groupingBy(WalkMember::getDeptId));

        List<WalkOrgRanking> rankings = new ArrayList<>();
        for (Map.Entry<Long, List<WalkMember>> entry : deptMembers.entrySet()) {
            Long deptId = entry.getKey();
            List<WalkMember> deptMemberList = entry.getValue();

            WalkDeptVo dept = walkDeptMapper.selectWalkDeptById(deptId);
            int totalMemberCount = dept != null && dept.getMemberCount() != null ? dept.getMemberCount() : 0;
            int approvedCount = (int) deptMemberList.stream().filter(m -> "1".equals(m.getSignupStatus())).count();

            List<WalkScoreSummary> summaries = new ArrayList<>();
            long totalSteps = 0;
            int awardCount = 0;
            for (WalkMember m : deptMemberList) {
                WalkScoreSummary s = scoreSummaryMapper.selectByMember(activityId, m.getMemberId());
                if (s != null) {
                    summaries.add(s);
                }
                LambdaQueryWrapper<WalkStepDaily> stepLqw = Wrappers.lambdaQuery();
                stepLqw.eq(WalkStepDaily::getMemberId, m.getMemberId());
                stepLqw.eq(WalkStepDaily::getActivityId, activityId);
                totalSteps += stepDailyMapper.selectList(stepLqw).stream()
                    .mapToInt(WalkStepDaily::getEffectiveSteps).sum();

                LambdaQueryWrapper<WalkWinner> winnerLqw = Wrappers.lambdaQuery();
                winnerLqw.eq(WalkWinner::getActivityId, activityId);
                winnerLqw.eq(WalkWinner::getMemberId, m.getMemberId());
                List<WalkWinner> winners = winnerMapper.selectList(winnerLqw);
                if (!winners.isEmpty()) {
                    awardCount++;
                }
            }

            int signupCount = deptMemberList.size();
            double participationRate = totalMemberCount > 0 ? (double) signupCount / totalMemberCount * 100 : 0;
            double avgScore = summaries.stream().mapToInt(WalkScoreSummary::getTotalScore).average().orElse(0);
            double avgSteps = signupCount > 0 ? (double) totalSteps / signupCount : 0;

            WalkOrgRanking orgRanking = new WalkOrgRanking();
            orgRanking.setActivityId(activityId);
            orgRanking.setDeptId(deptId);
            orgRanking.setMemberCount(totalMemberCount);
            orgRanking.setSignupCount(signupCount);
            orgRanking.setApprovedCount(approvedCount);
            orgRanking.setParticipationRate(BigDecimal.valueOf(participationRate).setScale(2, RoundingMode.HALF_UP));
            orgRanking.setCheckinRate(BigDecimal.valueOf(avgScore > 0 ? Math.min(100, avgScore * 5) : 0).setScale(2, RoundingMode.HALF_UP));
            orgRanking.setAvgScore(BigDecimal.valueOf(avgScore).setScale(2, RoundingMode.HALF_UP));
            orgRanking.setAvgSteps(BigDecimal.valueOf(avgSteps).setScale(2, RoundingMode.HALF_UP));
            orgRanking.setAwardCount(awardCount);
            double orgScoreValue = participationRate * 0.3
                + (avgScore > 0 ? Math.min(100, avgScore * 5) : 0) * 0.3
                + avgScore * 0.2
                + Math.min(100, avgSteps / 1000) * 0.2;
            orgRanking.setOrgScore(BigDecimal.valueOf(orgScoreValue).setScale(2, RoundingMode.HALF_UP));
            rankings.add(orgRanking);
        }

        rankings.sort((a, b) -> {
            int scoreCmp = b.getOrgScore().compareTo(a.getOrgScore());
            if (scoreCmp != 0) {
                return scoreCmp;
            }
            return b.getParticipationRate().compareTo(a.getParticipationRate());
        });

        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRankNo(i + 1);
        }

        if (!rankings.isEmpty()) {
            rankings.forEach(r -> r.setRankingId(IdWorker.getId()));
            orgRankingMapper.batchInsertOrgRanking(rankings);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lockRankings(Long activityId) {
        WalkActivity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            return;
        }

        String lockDate = DateUtils.parseDateToStr(FormatsType.YYYY_MM_DD, activity.getLockTime());
        stepDailyMapper.updateLocked(activityId, lockDate);

        activityMapper.updateWalkActivityStatus(activityId, "4");
    }

    @Override
    public TableDataInfo<WalkOrgRankingVo> getOrgRankings(Long activityId, PageQuery pageQuery) {
        Page<WalkOrgRankingVo> page = pageQuery.build();
        WalkOrgRanking param = new WalkOrgRanking();
        param.setActivityId(activityId);
        IPage<WalkOrgRankingVo> result = orgRankingMapper.selectWalkOrgRankingList(page, param);
        for (WalkOrgRankingVo orgRanking : result.getRecords()) {
            WalkDeptVo dept = walkDeptMapper.selectWalkDeptById(orgRanking.getDeptId());
            if (dept != null) {
                orgRanking.setDeptName(dept.getDeptName());
            }
        }
        return TableDataInfo.build(result);
    }
}
