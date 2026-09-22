package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkMember;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkMember.class, reverseConvertGenerate = false)
public class WalkMemberBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 会员ID */
    @NotNull(message = "会员ID不能为空", groups = { EditGroup.class })
    private Long memberId;

    /** 活动ID */
    @NotNull(message = "活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /** 微信openid */
    private String openid;

    /** 微信unionid */
    private String unionid;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String mobile;

    /** 姓名 */
    @NotBlank(message = "姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String realName;

    /** 所在单位（关联sys_dept） */
    @NotNull(message = "所在单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deptId;

    /** 报名提交时间 */
    private Date signupTime;

    /** 报名状态（0=待提交草稿,1=审核通过,2=已取消,3=已停用） */
    private String signupStatus;

    /** 微信步数授权状态（0=未授权,1=已授权） */
    private String stepAuthStatus;

    /** 小程序绑定状态（0=未绑定,1=已绑定） */
    private String miniprogramBindStatus;

    /** 是否工会会员（Y/N） */
    private String isUnionMember;

    /** 异常标记（0=正常,1=异常/作弊） */
    private String abnormalFlag;

    /** 异常类型（1=刷步作弊,2=报名信息异常,3=其他） */
    private String abnormalType;

    /** 异常描述 */
    private String abnormalDesc;

    /** 异常处理状态（0=待处理,1=已处理） */
    private String handleStatus;

    /** 处理动作（1=清零积分,2=取消评奖资格,3=停用账号） */
    private String handleAction;

    /** 异常处理人 */
    private String handleBy;

    /** 异常处理时间 */
    private Date handleTime;

}
