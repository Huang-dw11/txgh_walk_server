package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.WalkAward;
import org.dromara.walk.domain.WalkMember;
import org.dromara.walk.domain.WalkWinner;
import org.dromara.walk.domain.bo.WalkWinnerBo;
import org.dromara.walk.domain.vo.WalkAwardVo;
import org.dromara.walk.domain.vo.WalkDeptVo;
import org.dromara.walk.domain.vo.WalkWinnerVo;
import org.dromara.walk.mapper.WalkAwardMapper;
import org.dromara.walk.mapper.WalkDeptMapper;
import org.dromara.walk.mapper.WalkMemberMapper;
import org.dromara.walk.mapper.WalkWinnerMapper;
import org.dromara.walk.service.IWalkWinnerService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkWinnerServiceImpl implements IWalkWinnerService {

    private final WalkWinnerMapper baseMapper;
    private final WalkMemberMapper memberMapper;
    private final WalkDeptMapper walkDeptMapper;
    private final WalkAwardMapper awardMapper;

    @Override
    public WalkWinnerVo queryById(Long winnerId) {
        WalkWinnerVo vo = baseMapper.selectVoById(winnerId);
        if (vo != null) {
            enrichWinnerList(java.util.Collections.singletonList(vo));
        }
        return vo;
    }

    @Override
    public WalkWinnerVo selectWinnerByMember(Long activityId, Long memberId) {
        WalkWinner winner = baseMapper.selectWinnerByMember(activityId, memberId);
        if (winner == null) {
            return null;
        }
        WalkWinnerVo vo = MapstructUtils.convert(winner, WalkWinnerVo.class);
        if (winner.getAwardId() != null) {
            WalkAwardVo award = awardMapper.selectVoById(winner.getAwardId());
            if (award != null) {
                vo.setAwardName(award.getAwardName());
                vo.setPrizeContent(award.getPrizeContent());
            }
        }
        return vo;
    }

    @Override
    public TableDataInfo<WalkWinnerVo> queryPageList(WalkWinnerBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WalkWinner> lqw = buildQueryWrapper(bo);
        Page<WalkWinnerVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<WalkWinnerVo> records = result.getRecords();
        enrichWinnerList(records);
        return TableDataInfo.build(result);
    }

    @Override
    public List<WalkWinnerVo> queryList(WalkWinnerBo bo) {
        LambdaQueryWrapper<WalkWinner> lqw = buildQueryWrapper(bo);
        List<WalkWinnerVo> list = baseMapper.selectVoList(lqw);
        enrichWinnerList(list);
        return list;
    }

    private LambdaQueryWrapper<WalkWinner> buildQueryWrapper(WalkWinnerBo bo) {
        LambdaQueryWrapper<WalkWinner> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityId() != null, WalkWinner::getActivityId, bo.getActivityId());
        lqw.eq(bo.getMemberId() != null, WalkWinner::getMemberId, bo.getMemberId());
        lqw.eq(bo.getDeptId() != null, WalkWinner::getDeptId, bo.getDeptId());
        lqw.eq(StringUtils.isNotBlank(bo.getAwardLevel()), WalkWinner::getAwardLevel, bo.getAwardLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getWinStatus()), WalkWinner::getWinStatus, bo.getWinStatus());
        if (StringUtils.isNotBlank(bo.getRealName())) {
            String kw = bo.getRealName().trim();
            lqw.and(w -> w
                .apply("EXISTS (SELECT 1 FROM walk_member m WHERE m.member_id = walk_winner.member_id " +
                    "AND m.del_flag = '0' AND m.real_name LIKE CONCAT('%', {0}, '%'))", kw)
                .or().like(WalkWinner::getReceiver, kw)
            );
        }
        if (StringUtils.isNotBlank(bo.getMobile())) {
            String kw = bo.getMobile().trim();
            lqw.and(w -> w
                .apply("EXISTS (SELECT 1 FROM walk_member m WHERE m.member_id = walk_winner.member_id " +
                    "AND m.del_flag = '0' AND m.mobile LIKE CONCAT('%', {0}, '%'))", kw)
                .or().like(WalkWinner::getReceiverPhone, kw)
            );
        }
        lqw.orderByDesc(WalkWinner::getWinnerId);
        return lqw;
    }

    private void enrichWinnerList(List<WalkWinnerVo> list) {
        if (list.isEmpty()) return;
        Set<Long> deptIds = new HashSet<>();
        Set<Long> awardIds = new HashSet<>();
        Map<Long, WalkMember> memberMap = new HashMap<>();
        for (WalkWinnerVo w : list) {
            if (w.getAwardId() != null) {
                awardIds.add(w.getAwardId());
            }
            if (w.getMemberId() != null) {
                WalkMember member = memberMapper.selectById(w.getMemberId());
                if (member != null) {
                    memberMap.put(w.getMemberId(), member);
                    w.setRealName(member.getRealName());
                    if (member.getDeptId() != null) {
                        deptIds.add(member.getDeptId());
                    }
                }
            }
            if (w.getDeptId() != null) {
                deptIds.add(w.getDeptId());
            }
        }
        if (!awardIds.isEmpty()) {
            Map<Long, WalkAward> awardMap = awardMapper.selectBatchIds(awardIds).stream()
                .filter(a -> a.getAwardId() != null)
                .collect(Collectors.toMap(WalkAward::getAwardId, a -> a));
            for (WalkWinnerVo w : list) {
                if (w.getAwardId() != null) {
                    WalkAward award = awardMap.get(w.getAwardId());
                    if (award != null) {
                        w.setAwardName(award.getAwardName());
                        w.setPrizeContent(award.getPrizeContent());
                    }
                }
            }
        }
        if (!deptIds.isEmpty()) {
            Map<Long, String> deptNameMap = walkDeptMapper.selectWalkDeptByIds(deptIds).stream()
                .filter(d -> d.getDeptId() != null && d.getDeptName() != null)
                .collect(Collectors.toMap(WalkDeptVo::getDeptId, WalkDeptVo::getDeptName));
            for (WalkWinnerVo w : list) {
                if (w.getDeptId() != null) {
                    w.setDeptName(deptNameMap.get(w.getDeptId()));
                } else if (w.getMemberId() != null) {
                    WalkMember m = memberMap.get(w.getMemberId());
                    if (m != null && m.getDeptId() != null) {
                        w.setDeptName(deptNameMap.get(m.getDeptId()));
                    }
                }
            }
        }
    }

    @Override
    public Boolean insertByBo(WalkWinnerBo bo) {
        // 校验奖项存在且属于该活动
        WalkAward award = awardMapper.selectById(bo.getAwardId());
        if (award == null) {
            throw new ServiceException("奖项不存在");
        }
        if (!award.getActivityId().equals(bo.getActivityId())) {
            throw new ServiceException("奖项不属于当前活动");
        }

        if ("1".equals(award.getAwardType())) {
            // 个人奖：校验会员存在、属于该活动、已审核通过
            if (bo.getMemberId() == null) {
                throw new ServiceException("个人奖项必须选择会员");
            }
            WalkMember member = memberMapper.selectById(bo.getMemberId());
            if (member == null) {
                throw new ServiceException("会员不存在");
            }
            if (!member.getActivityId().equals(bo.getActivityId())) {
                throw new ServiceException("会员不属于当前活动");
            }
            if (!"1".equals(member.getSignupStatus())) {
                throw new ServiceException("会员未通过审核");
            }
            // 校验不重复中奖（同一活动同一会员只能有一条中奖记录）
            WalkWinner existing = baseMapper.selectWinnerByMember(bo.getActivityId(), bo.getMemberId());
            if (existing != null) {
                throw new ServiceException("该会员已录入中奖记录，不可重复录入");
            }
        } else if ("2".equals(award.getAwardType())) {
            // 集体奖：校验单位存在
            if (bo.getDeptId() == null) {
                throw new ServiceException("集体奖项必须选择单位");
            }
            var dept = walkDeptMapper.selectWalkDeptById(bo.getDeptId());
            if (dept == null) {
                throw new ServiceException("单位不存在");
            }
        }

        WalkWinner add = MapstructUtils.convert(bo, WalkWinner.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setWinnerId(add.getWinnerId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(WalkWinnerBo bo) {
        WalkWinner update = MapstructUtils.convert(bo, WalkWinner.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean updateAddress(Long winnerId, String receiver, String phone, String address) {
        WalkWinner winner = new WalkWinner();
        winner.setWinnerId(winnerId);
        winner.setReceiver(receiver);
        winner.setReceiverPhone(phone);
        winner.setAddress(address);
        return baseMapper.updateWalkWinnerAddress(winner) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
