package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkMember;
import org.dromara.walk.domain.bo.WalkMemberBo;
import org.dromara.walk.domain.vo.WalkMemberVo;

import java.util.Collection;
import java.util.List;

/**
 * 参与成员 数据层
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkMemberMapper extends BaseMapperPlus<WalkMember, WalkMemberVo> {

    /**
     * 根据手机号查询成员
     */
    WalkMember selectByMobile(@Param("activityId") Long activityId, @Param("mobile") String mobile);

    /**
     * 根据openid查询成员
     */
    WalkMember selectByOpenid(@Param("activityId") Long activityId, @Param("openid") String openid);

    /**
     * 根据部门查询成员列表
     */
    List<WalkMember> selectListByDept(@Param("activityId") Long activityId, @Param("deptId") Long deptId);

    /**
     * 按关键词搜索当前活动已报名会员（姓名/手机号模糊匹配）
     * 仅返回 signup_status=1 的审核通过会员，限制 20 条
     *
     * @param activityId 活动ID
     * @param keyword    关键词（姓名或手机号片段）
     * @return 匹配会员列表（精简字段）
     */
    List<WalkMemberVo> searchByKeyword(@Param("activityId") Long activityId, @Param("keyword") String keyword);

    /**
     * 分页查询会员列表（JOIN sys_dept 获取单位名称）
     */
    IPage<WalkMemberVo> selectMemberVoPage(@Param("page") Page<Object> page, @Param("bo") WalkMemberBo bo);

    /**
     * 查询会员列表（JOIN sys_dept 获取单位名称）
     */
    List<WalkMemberVo> selectMemberVoList(@Param("bo") WalkMemberBo bo);

    /**
     * 更新绑定信息
     */
    int updateBind(WalkMember member);

    /**
     * 更新异常信息
     */
    int updateAbnormal(WalkMember member);
}
