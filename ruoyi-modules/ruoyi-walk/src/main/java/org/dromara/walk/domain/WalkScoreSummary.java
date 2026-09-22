package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 积分汇总对象 walk_score_summary
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_score_summary")
public class WalkScoreSummary extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 汇总ID
     */
    @TableId(value = "summary_id")
    private Long summaryId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 基础分合计
     */
    private Integer baseScore;

    /**
     * 7天奖励分合计
     */
    @TableField("reward_7days_score")
    private Integer reward7daysScore;

    /**
     * 14天奖励分合计
     */
    @TableField("reward_14days_score")
    private Integer reward14daysScore;

    /**
     * 21天全勤奖励分合计
     */
    @TableField("reward_21days_score")
    private Integer reward21daysScore;

    /**
     * 总积分
     */
    private Integer totalScore;

    /**
     * 累计达标打卡天数
     */
    private Integer totalCheckinDays;

    /**
     * 当前连续打卡天数
     */
    private Integer currentContinuousDays;

    /**
     * 历史最长连续打卡天数
     */
    private Integer maxContinuousDays;

    /**
     * 活动期累计有效步数
     */
    private Integer totalEffectiveSteps;

}
