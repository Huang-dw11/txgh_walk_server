package org.dromara.walk.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.walk.domain.WalkScoreSummary;

import java.io.Serial;

/**
 * 积分汇总业务对象 walk_score_summary
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkScoreSummary.class, reverseConvertGenerate = false)
public class WalkScoreSummaryBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 汇总ID
     */
    @NotNull(message = "汇总ID不能为空", groups = { EditGroup.class })
    private Long summaryId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /**
     * 会员ID
     */
    @NotNull(message = "会员ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long memberId;

    /**
     * 基础分合计
     */
    private Integer baseScore;

    /**
     * 7天奖励分合计
     */
    private Integer reward7daysScore;

    /**
     * 14天奖励分合计
     */
    private Integer reward14daysScore;

    /**
     * 21天全勤奖励分合计
     */
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
