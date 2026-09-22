package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkScoreSummary;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分汇总视图对象 walk_score_summary
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkScoreSummary.class)
public class WalkScoreSummaryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 汇总ID
     */
    @ExcelProperty(value = "汇总ID")
    private Long summaryId;

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
     * 基础分合计
     */
    @ExcelProperty(value = "基础分合计")
    private Integer baseScore;

    /**
     * 7天奖励分合计
     */
    @ExcelProperty(value = "7天奖励分合计")
    private Integer reward7daysScore;

    /**
     * 14天奖励分合计
     */
    @ExcelProperty(value = "14天奖励分合计")
    private Integer reward14daysScore;

    /**
     * 21天全勤奖励分合计
     */
    @ExcelProperty(value = "21天全勤奖励分合计")
    private Integer reward21daysScore;

    /**
     * 总积分
     */
    @ExcelProperty(value = "总积分")
    private Integer totalScore;

    /**
     * 累计达标打卡天数
     */
    @ExcelProperty(value = "累计达标打卡天数")
    private Integer totalCheckinDays;

    /**
     * 当前连续打卡天数
     */
    @ExcelProperty(value = "当前连续打卡天数")
    private Integer currentContinuousDays;

    /**
     * 历史最长连续打卡天数
     */
    @ExcelProperty(value = "历史最长连续打卡天数")
    private Integer maxContinuousDays;

    /**
     * 活动期累计有效步数
     */
    @ExcelProperty(value = "活动期累计有效步数")
    private Integer totalEffectiveSteps;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

}
