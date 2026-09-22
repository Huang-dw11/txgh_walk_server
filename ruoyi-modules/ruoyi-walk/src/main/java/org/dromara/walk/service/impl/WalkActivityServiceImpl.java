package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.WalkActivity;
import org.dromara.walk.domain.bo.WalkActivityBo;
import org.dromara.walk.domain.vo.WalkActivityVo;
import org.dromara.walk.mapper.WalkActivityMapper;
import org.dromara.walk.service.IWalkActivityService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkActivityServiceImpl implements IWalkActivityService {

    private final WalkActivityMapper baseMapper;

    @Override
    public WalkActivityVo queryById(Long activityId) {
        return baseMapper.selectVoById(activityId);
    }

    @Override
    public WalkActivityVo selectCurrentActivity() {
        WalkActivity activity = baseMapper.selectCurrentActivity();
        if (activity == null) {
            return null;
        }
        return MapstructUtils.convert(activity, WalkActivityVo.class);
    }

    @Override
    public TableDataInfo<WalkActivityVo> queryPageList(WalkActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WalkActivity> lqw = buildQueryWrapper(bo);
        Page<WalkActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<WalkActivityVo> queryList(WalkActivityBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<WalkActivity> buildQueryWrapper(WalkActivityBo bo) {
        LambdaQueryWrapper<WalkActivity> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getActivityName()), WalkActivity::getActivityName, bo.getActivityName());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), WalkActivity::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getIsCurrent()), WalkActivity::getIsCurrent, bo.getIsCurrent());
        lqw.orderByDesc(WalkActivity::getActivityStartTime);
        return lqw;
    }

    @Override
    public Boolean insertByBo(WalkActivityBo bo) {
        WalkActivity add = MapstructUtils.convert(bo, WalkActivity.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setActivityId(add.getActivityId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(WalkActivityBo bo) {
        WalkActivity update = MapstructUtils.convert(bo, WalkActivity.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean updateActivityStatus(Long activityId, String status) {
        return baseMapper.updateWalkActivityStatus(activityId, status) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }

    @Override
    public void autoUpdateActivityStatus() {
        WalkActivity activity = baseMapper.selectCurrentActivity();
        if (activity == null) {
            return;
        }

        Date now = DateUtils.getNowDate();
        String currentStatus = activity.getStatus();

        if ("0".equals(currentStatus) && now.after(activity.getSignupStartTime())) {
            baseMapper.updateWalkActivityStatus(activity.getActivityId(), "1");
        }
        if ("1".equals(currentStatus) && now.after(activity.getActivityStartTime())) {
            baseMapper.updateWalkActivityStatus(activity.getActivityId(), "2");
        }
        if ("2".equals(currentStatus) && now.after(activity.getActivityEndTime())) {
            baseMapper.updateWalkActivityStatus(activity.getActivityId(), "3");
        }
        if ("3".equals(currentStatus) && activity.getLockTime() != null && now.after(activity.getLockTime())) {
            baseMapper.updateWalkActivityStatus(activity.getActivityId(), "4");
        }
    }
}
