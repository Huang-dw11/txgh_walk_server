package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.*;
import org.dromara.walk.domain.bo.WalkMemberBo;
import org.dromara.walk.domain.vo.WalkMemberVo;
import org.dromara.walk.mapper.*;
import org.dromara.walk.service.IWalkMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkMemberServiceImpl implements IWalkMemberService {

    private final WalkMemberMapper baseMapper;
    private final WalkScoreRecordMapper scoreRecordMapper;
    private final WalkScoreSummaryMapper scoreSummaryMapper;
    private final WalkWinnerMapper winnerMapper;
    private final WalkActivityMapper activityMapper;

    @Override
    public WalkMemberVo queryById(Long memberId) {
        return baseMapper.selectVoById(memberId);
    }

    @Override
    public WalkMember selectByMobile(Long activityId, String mobile) {
        return baseMapper.selectByMobile(activityId, mobile);
    }

    @Override
    public WalkMember selectByOpenid(Long activityId, String openid) {
        return baseMapper.selectByOpenid(activityId, openid);
    }

    @Override
    public List<WalkMemberVo> searchByKeyword(Long activityId, String keyword) {
        return baseMapper.searchByKeyword(activityId, keyword);
    }

    @Override
    public TableDataInfo<WalkMemberVo> queryPageList(WalkMemberBo bo, PageQuery pageQuery) {
        Page<Object> page = pageQuery.build();
        IPage<WalkMemberVo> result = baseMapper.selectMemberVoPage(page, bo);
        return TableDataInfo.build(result);
    }

    @Override
    public List<WalkMemberVo> queryList(WalkMemberBo bo) {
        return baseMapper.selectMemberVoList(bo);
    }

    private LambdaQueryWrapper<WalkMember> buildQueryWrapper(WalkMemberBo bo) {
        LambdaQueryWrapper<WalkMember> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityId() != null, WalkMember::getActivityId, bo.getActivityId());
        lqw.eq(bo.getDeptId() != null, WalkMember::getDeptId, bo.getDeptId());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), WalkMember::getRealName, bo.getRealName());
        lqw.eq(StringUtils.isNotBlank(bo.getMobile()), WalkMember::getMobile, bo.getMobile());
        lqw.eq(StringUtils.isNotBlank(bo.getSignupStatus()), WalkMember::getSignupStatus, bo.getSignupStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getAbnormalFlag()), WalkMember::getAbnormalFlag, bo.getAbnormalFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getOpenid()), WalkMember::getOpenid, bo.getOpenid());
        lqw.orderByDesc(WalkMember::getSignupTime);
        return lqw;
    }

    @Override
    public WalkMember signup(WalkMember member) {
        WalkActivity activity = activityMapper.selectById(member.getActivityId());
        if (activity == null) {
            throw new ServiceException("活动不存在");
        }
        String status = activity.getStatus();
        if (!"1".equals(status) && !"2".equals(status)) {
            if ("0".equals(status)) {
                throw new ServiceException("报名尚未开始");
            } else if ("3".equals(status)) {
                throw new ServiceException("活动已结束，报名通道已关闭");
            } else if ("4".equals(status)) {
                throw new ServiceException("活动已结束");
            }
            throw new ServiceException("当前不在报名时间范围内");
        }

        boolean withOpenid = StringUtils.isNotEmpty(member.getOpenid());

        WalkMember existing = baseMapper.selectByMobile(member.getActivityId(), member.getMobile());

        if (existing != null && !"0".equals(existing.getSignupStatus())) {
            if (withOpenid && StringUtils.isEmpty(existing.getOpenid())) {
                existing.setOpenid(member.getOpenid());
                existing.setStepAuthStatus("0");
                baseMapper.updateBind(existing);
                return existing;
            }
            throw new ServiceException("该手机号已报名，不可重复提交");
        }

        if (existing != null && "0".equals(existing.getSignupStatus())) {
            existing.setRealName(member.getRealName());
            existing.setDeptId(member.getDeptId());
            existing.setSignupStatus("1");
            existing.setSignupTime(DateUtils.getNowDate());
            baseMapper.updateById(existing);
            if (withOpenid && StringUtils.isEmpty(existing.getOpenid())) {
                existing.setOpenid(member.getOpenid());
                existing.setStepAuthStatus("0");
                baseMapper.updateBind(existing);
            }
            return existing;
        }

        member.setSignupStatus("1");
        member.setSignupTime(DateUtils.getNowDate());
        if (withOpenid) {
            member.setMiniprogramBindStatus("1");
        }
        baseMapper.insert(member);
        return member;
    }

    @Override
    public WalkMember saveDraft(WalkMember member) {
        WalkMember existing = baseMapper.selectByMobile(member.getActivityId(), member.getMobile());
        if (existing != null && "0".equals(existing.getSignupStatus())) {
            existing.setRealName(member.getRealName());
            existing.setDeptId(member.getDeptId());
            baseMapper.updateById(existing);
            return existing;
        }
        if (existing == null) {
            member.setSignupStatus("0");
            baseMapper.insert(member);
            return member;
        }
        throw new ServiceException("已提交报名，无法修改草稿");
    }

    @Override
    public int bindWechat(Long memberId, String openid, String stepAuthStatus) {
        WalkMember member = new WalkMember();
        member.setMemberId(memberId);
        member.setOpenid(openid);
        member.setStepAuthStatus(stepAuthStatus);
        return baseMapper.updateBind(member);
    }

    @Override
    public Boolean updateByBo(WalkMemberBo bo) {
        WalkMember update = MapstructUtils.convert(bo, WalkMember.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public int updateMemberStatus(Long memberId, String status) {
        WalkMember member = new WalkMember();
        member.setMemberId(memberId);
        member.setSignupStatus(status);
        return baseMapper.updateById(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int markAbnormal(Long memberId, String abnormalType, String abnormalDesc, String handleBy, String handleAction) {
        WalkMember member = baseMapper.selectById(memberId);
        if (member == null) {
            throw new ServiceException("会员不存在");
        }

        member.setAbnormalFlag("1");
        member.setAbnormalType(abnormalType);
        member.setAbnormalDesc(abnormalDesc);
        member.setHandleBy(handleBy);
        member.setHandleTime(DateUtils.getNowDate());

        if (StringUtils.isNotBlank(handleAction)) {
            member.setHandleAction(handleAction);
            member.setHandleStatus("1");
        } else {
            member.setHandleStatus("0");
        }

        int rows = baseMapper.updateAbnormal(member);

        if (StringUtils.isNotBlank(handleAction)) {
            Long activityId = member.getActivityId();
            switch (handleAction) {
                case "1" -> clearScore(activityId, memberId);
                case "2" -> cancelAwardEligibility(activityId, memberId);
                case "3" -> disableAccount(member);
                default -> log.warn("未知的异常处理动作：handleAction={}, memberId={}", handleAction, memberId);
            }
        }

        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchMarkAbnormal(List<Long> memberIds, String abnormalType, String handleBy, String handleAction) {
        int count = 0;
        for (Long memberId : memberIds) {
            count += markAbnormal(memberId, abnormalType, null, handleBy, handleAction);
        }
        return count;
    }

    @Override
    public int clearAbnormal(Long memberId) {
        return baseMapper.update(null,
            new LambdaUpdateWrapper<WalkMember>()
                .eq(WalkMember::getMemberId, memberId)
                .set(WalkMember::getAbnormalFlag, "0")
                .set(WalkMember::getAbnormalType, null)
                .set(WalkMember::getAbnormalDesc, null)
                .set(WalkMember::getHandleStatus, null)
                .set(WalkMember::getHandleAction, null)
                .set(WalkMember::getHandleBy, null)
                .set(WalkMember::getHandleTime, null)
        );
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    private void clearScore(Long activityId, Long memberId) {
        scoreRecordMapper.delete(new LambdaQueryWrapper<WalkScoreRecord>()
            .eq(WalkScoreRecord::getActivityId, activityId)
            .eq(WalkScoreRecord::getMemberId, memberId));

        WalkScoreSummary summary = scoreSummaryMapper.selectByMember(activityId, memberId);
        if (summary != null) {
            summary.setBaseScore(0);
            summary.setReward7daysScore(0);
            summary.setReward14daysScore(0);
            summary.setReward21daysScore(0);
            summary.setTotalScore(0);
            summary.setCurrentContinuousDays(0);
            scoreSummaryMapper.updateById(summary);
        }
        log.info("【异常处理】已清零积分：activityId={}, memberId={}", activityId, memberId);
    }

    private void cancelAwardEligibility(Long activityId, Long memberId) {
        winnerMapper.delete(new LambdaQueryWrapper<WalkWinner>()
            .eq(WalkWinner::getActivityId, activityId)
            .eq(WalkWinner::getMemberId, memberId));
        log.info("【异常处理】已取消评奖资格：activityId={}, memberId={}", activityId, memberId);
    }

    private void disableAccount(WalkMember member) {
        member.setSignupStatus("3");
        baseMapper.updateById(member);
        log.info("【异常处理】已停用账号：memberId={}", member.getMemberId());
    }
}
