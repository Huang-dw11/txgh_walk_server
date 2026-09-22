package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.WalkAward;
import org.dromara.walk.domain.bo.WalkAwardBo;
import org.dromara.walk.domain.vo.WalkAwardVo;
import org.dromara.walk.mapper.WalkAwardMapper;
import org.dromara.walk.service.IWalkAwardService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkAwardServiceImpl implements IWalkAwardService {

    private final WalkAwardMapper baseMapper;

    @Override
    public WalkAwardVo queryById(Long awardId) {
        return baseMapper.selectVoById(awardId);
    }

    @Override
    public TableDataInfo<WalkAwardVo> queryPageList(WalkAwardBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WalkAward> lqw = buildQueryWrapper(bo);
        Page<WalkAwardVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<WalkAwardVo> queryList(WalkAwardBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public List<WalkAwardVo> selectAwardsByActivity(Long activityId) {
        return baseMapper.selectAwardsByActivity(activityId);
    }

    private LambdaQueryWrapper<WalkAward> buildQueryWrapper(WalkAwardBo bo) {
        LambdaQueryWrapper<WalkAward> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getActivityId() != null, WalkAward::getActivityId, bo.getActivityId());
        lqw.eq(StringUtils.isNotBlank(bo.getAwardType()), WalkAward::getAwardType, bo.getAwardType());
        lqw.eq(StringUtils.isNotBlank(bo.getAwardLevel()), WalkAward::getAwardLevel, bo.getAwardLevel());
        lqw.orderByAsc(WalkAward::getSortOrder);
        return lqw;
    }

    @Override
    public Boolean insertByBo(WalkAwardBo bo) {
        WalkAward add = MapstructUtils.convert(bo, WalkAward.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setAwardId(add.getAwardId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(WalkAwardBo bo) {
        WalkAward update = MapstructUtils.convert(bo, WalkAward.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
