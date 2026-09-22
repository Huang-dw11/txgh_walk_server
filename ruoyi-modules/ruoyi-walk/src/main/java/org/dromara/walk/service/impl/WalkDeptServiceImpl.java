package org.dromara.walk.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.WalkDept;
import org.dromara.walk.domain.bo.WalkDeptBo;
import org.dromara.walk.domain.vo.WalkDeptVo;
import org.dromara.walk.mapper.WalkDeptMapper;
import org.dromara.walk.service.IWalkDeptService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WalkDeptServiceImpl implements IWalkDeptService {

    private final WalkDeptMapper baseMapper;

    @Override
    public WalkDeptVo queryById(Long deptId) {
        return baseMapper.selectWalkDeptById(deptId);
    }

    @Override
    public TableDataInfo<WalkDeptVo> queryPageList(WalkDeptBo bo, PageQuery pageQuery) {
        List<WalkDeptVo> fullList = baseMapper.selectWalkDeptList(bo);
        Page<WalkDeptVo> page = pageQuery.build();
        int total = fullList.size();
        int current = (int) page.getCurrent();
        int size = (int) page.getSize();
        int fromIndex = Math.min((int) ((current - 1) * size), total);
        int toIndex = Math.min(fromIndex + size, total);
        page.setTotal(total);
        page.setRecords(fullList.subList(fromIndex, toIndex));
        return TableDataInfo.build(page);
    }

    @Override
    public List<WalkDeptVo> queryList(WalkDeptBo bo) {
        return baseMapper.selectWalkDeptList(bo);
    }

    @Override
    public Boolean insertByBo(WalkDeptBo bo) {
        WalkDept add = MapstructUtils.convert(bo, WalkDept.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setDeptId(add.getDeptId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(WalkDeptBo bo) {
        WalkDept update = MapstructUtils.convert(bo, WalkDept.class);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteByIds(ids) > 0;
    }
}
