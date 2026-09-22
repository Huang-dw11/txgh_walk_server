package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.walk.domain.*;
import org.dromara.walk.domain.vo.WalkDeptVo;
import org.dromara.walk.mapper.*;
import org.dromara.walk.service.IWalkStatsService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkStatsServiceImpl implements IWalkStatsService {

    private final WalkMemberMapper memberMapper;
    private final WalkStepDailyMapper stepDailyMapper;
    private final WalkScoreRecordMapper scoreRecordMapper;
    private final WalkDeptMapper walkDeptMapper;

    @Override
    public Map<String, Object> getSignupStats(Long activityId) {
        LambdaQueryWrapper<WalkMember> lqw = Wrappers.lambdaQuery();
        lqw.eq(WalkMember::getActivityId, activityId);
        List<WalkMember> allMembers = memberMapper.selectList(lqw);

        long total = allMembers.size();
        long approved = allMembers.stream().filter(m -> "1".equals(m.getSignupStatus())).count();
        long draft = allMembers.stream().filter(m -> "0".equals(m.getSignupStatus())).count();
        long cancelled = allMembers.stream().filter(m -> "2".equals(m.getSignupStatus())).count();
        long disabled = allMembers.stream().filter(m -> "3".equals(m.getSignupStatus())).count();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", total);
        stats.put("approved", approved);
        stats.put("draft", draft);
        stats.put("cancelled", cancelled);
        stats.put("disabled", disabled);
        return stats;
    }

    @Override
    public List<Map<String, Object>> getSignupStatsByDept(Long activityId) {
        LambdaQueryWrapper<WalkMember> lqw = Wrappers.lambdaQuery();
        lqw.eq(WalkMember::getActivityId, activityId);
        List<WalkMember> members = memberMapper.selectList(lqw);
        Map<Long, List<WalkMember>> deptGroups = members.stream()
            .collect(Collectors.groupingBy(WalkMember::getDeptId));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, List<WalkMember>> entry : deptGroups.entrySet()) {
            Long deptId = entry.getKey();
            List<WalkMember> deptMembers = entry.getValue();
            WalkDeptVo dept = walkDeptMapper.selectWalkDeptById(deptId);
            Map<String, Object> deptStats = new LinkedHashMap<>();
            deptStats.put("deptId", deptId);
            deptStats.put("deptName", dept != null ? dept.getDeptName() : "");
            deptStats.put("signupCount", deptMembers.size());
            deptStats.put("approvedCount", deptMembers.stream().filter(m -> "1".equals(m.getSignupStatus())).count());
            result.add(deptStats);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getCheckinDetail(Long activityId, Long deptId) {
        LambdaQueryWrapper<WalkMember> memberLqw = Wrappers.lambdaQuery();
        memberLqw.eq(WalkMember::getActivityId, activityId);
        List<WalkMember> members = memberMapper.selectList(memberLqw);
        if (deptId != null) {
            members = members.stream().filter(m -> deptId.equals(m.getDeptId())).collect(Collectors.toList());
        }

        List<Map<String, Object>> details = new ArrayList<>();
        for (WalkMember member : members) {
            LambdaQueryWrapper<WalkStepDaily> stepLqw = Wrappers.lambdaQuery();
            stepLqw.eq(WalkStepDaily::getMemberId, member.getMemberId());
            stepLqw.eq(WalkStepDaily::getActivityId, activityId);
            List<WalkStepDaily> steps = stepDailyMapper.selectList(stepLqw);

            for (WalkStepDaily step : steps) {
                Map<String, Object> detail = new LinkedHashMap<>();
                detail.put("memberId", member.getMemberId());
                detail.put("realName", member.getRealName());
                detail.put("mobile", member.getMobile());
                detail.put("recordDate", step.getRecordDate());
                detail.put("wechatSteps", step.getWechatSteps());
                detail.put("effectiveSteps", step.getEffectiveSteps());
                detail.put("isTarget", step.getIsTarget());
                detail.put("isCheckin", step.getIsCheckin());
                detail.put("isLocked", step.getIsLocked());
                details.add(detail);
            }
        }
        return details;
    }

    @Override
    public List<Map<String, Object>> getScoreDetail(Long activityId, String mobile) {
        WalkMember member = memberMapper.selectByMobile(activityId, mobile);
        if (member == null) {
            return Collections.emptyList();
        }
        Long memberId = member.getMemberId();
        String realName = member.getRealName();

        List<WalkScoreRecord> records = scoreRecordMapper.selectMemberScoreRecords(memberId, activityId);
        List<Map<String, Object>> details = new ArrayList<>();
        for (WalkScoreRecord record : records) {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("recordId", record.getRecordId());
            String scoreType = record.getScoreType();
            detail.put("scoreType", scoreType);
            String scoreTypeName = switch (scoreType) {
                case "1" -> "每日基础分";
                case "2" -> "连续7天奖励";
                case "3" -> "连续14天奖励";
                case "4" -> "21天全勤奖励";
                default -> "";
            };
            detail.put("scoreTypeName", scoreTypeName);
            detail.put("scoreValue", record.getScoreValue());
            detail.put("relatedDate", record.getRelatedDate());
            detail.put("realName", realName);
            detail.put("mobile", mobile);
            details.add(detail);
        }
        return details;
    }
}
