package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkDept;
import org.dromara.walk.domain.bo.WalkDeptBo;
import org.dromara.walk.domain.vo.WalkDeptVo;

import java.util.Collection;
import java.util.List;

/**
 * 组织部门 数据层
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkDeptMapper extends BaseMapperPlus<WalkDept, WalkDeptVo> {

    /**
     * 根据部门ID查询组织部门
     */
    WalkDeptVo selectWalkDeptById(Long deptId);

    /**
     * 批量查询组织部门（含单位名称）
     */
    List<WalkDeptVo> selectWalkDeptByIds(@Param("deptIds") Collection<Long> deptIds);

    /**
     * 查询组织部门列表
     */
    List<WalkDeptVo> selectWalkDeptList(WalkDeptBo dept);

    /**
     * 查询未加入walk_dept的sys_dept（用于新增选择）
     */
    List<WalkDeptVo> selectAvailableDeptList(@Param("keyword") String keyword);

    /**
     * 物理删除组织部门
     */
    int deleteWalkDeptById(Long deptId);
}
