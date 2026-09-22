package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkContinuousCheckin;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 连续打卡记录视图对象 walk_continuous_checkin
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkContinuousCheckin.class)
public class WalkContinuousCheckinVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @ExcelProperty(value = "记录ID")
    private Long recordId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /**
     * 会员ID
     */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 本段连续打卡起始日期
     */
    @ExcelProperty(value = "本段连续打卡起始日期")
    private Date startDate;

    /**
     * 连续打卡天数
     */
    @ExcelProperty(value = "连续打卡天数")
    private Integer continuousDays;

    /**
     * 是否已中断（Y=已中断,N=进行中）
     */
    @ExcelProperty(value = "是否已中断")
    private String isBroken;

    /**
     * 中断日期
     */
    @ExcelProperty(value = "中断日期")
    private Date brokenDate;

    /**
     * 本段已领7天奖励次数
     */
    @ExcelProperty(value = "本段已领7天奖励次数")
    private Integer reward7daysCount;

    /**
     * 本段已领14天奖励次数
     */
    @ExcelProperty(value = "本段已领14天奖励次数")
    private Integer reward14daysCount;

    /**
     * 本段是否已领21天全勤奖励（Y/N）
     */
    @ExcelProperty(value = "本段是否已领21天全勤奖励")
    private String reward21daysReceived;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
