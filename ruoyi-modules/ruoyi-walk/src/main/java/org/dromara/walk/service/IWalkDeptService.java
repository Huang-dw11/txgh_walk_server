package org.dromara.walk.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.bo.WalkDeptBo;
import org.dromara.walk.domain.vo.WalkDeptVo;

import java.util.Collection;
import java.util.List;

public interface IWalkDeptService {

    WalkDeptVo queryById(Long deptId);

    TableDataInfo<WalkDeptVo> queryPageList(WalkDeptBo bo, PageQuery pageQuery);

    List<WalkDeptVo> queryList(WalkDeptBo bo);

    Boolean insertByBo(WalkDeptBo bo);

    Boolean updateByBo(WalkDeptBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
