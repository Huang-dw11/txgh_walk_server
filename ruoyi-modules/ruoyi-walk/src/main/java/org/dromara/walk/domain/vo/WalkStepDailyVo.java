package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkStepDaily;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkStepDaily.class)
public class WalkStepDailyVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @ExcelProperty(value = "记录ID")
    private Long recordId;

    /** 活动ID */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /** 会员ID */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /** 记录日期 */
    @ExcelProperty(value = "记录日期")
    private Date recordDate;

    /** 微信运动原始步数 */
    @ExcelProperty(value = "微信运动原始步数")
    private Integer wechatSteps;

    /** 有效步数 */
    @ExcelProperty(value = "有效步数")
    private Integer effectiveSteps;

    /** 是否达标（Y/N） */
    @ExcelProperty(value = "是否达标")
    private String isTarget;

    /** 是否打卡（Y/N） */
    @ExcelProperty(value = "是否打卡")
    private String isCheckin;

    /** 打卡时间 */
    @ExcelProperty(value = "打卡时间")
    private Date checkinTime;

    /** 是否锁定（Y=已锁定,N=可更新） */
    @ExcelProperty(value = "是否锁定")
    private String isLocked;

    /** 该天最终结果已处理（Y/N） */
    @ExcelProperty(value = "是否已结算")
    private String isSettled;

    /** 创建时间 */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
