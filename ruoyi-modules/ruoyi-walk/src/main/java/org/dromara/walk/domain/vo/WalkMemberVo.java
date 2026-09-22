package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkMember;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkMember.class)
public class WalkMemberVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 会员ID */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /** 活动ID */
    private Long activityId;

    /** 微信openid */
    private String openid;

    /** 微信unionid */
    private String unionid;

    /** 手机号 */
    @ExcelProperty(value = "手机号")
    private String mobile;

    /** 姓名 */
    @ExcelProperty(value = "姓名")
    private String realName;

    /** 所在单位（关联sys_dept） */
    private Long deptId;

    /** 单位名称（非数据库字段） */
    @ExcelProperty(value = "单位名称")
    private String deptName;

    /** 报名提交时间 */
    @ExcelProperty(value = "报名提交时间")
    private Date signupTime;

    /** 报名状态（0=待提交草稿,1=审核通过,2=已取消,3=已停用） */
    @ExcelProperty(value = "报名状态")
    @ExcelDictFormat(dictType = "walk_signup_status")
    private String signupStatus;

    /** 微信步数授权状态（0=未授权,1=已授权） */
    @ExcelDictFormat(readConverterExp = "0=未授权,1=已授权")
    private String stepAuthStatus;

    /** 小程序绑定状态（0=未绑定,1=已绑定） */
    private String miniprogramBindStatus;

    /** 是否工会会员（Y/N） */
    @ExcelDictFormat(readConverterExp = "Y=是,N=否")
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

    /** 创建时间 */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
