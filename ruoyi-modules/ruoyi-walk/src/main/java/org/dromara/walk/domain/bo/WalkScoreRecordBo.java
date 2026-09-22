package org.dromara.walk.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.walk.domain.WalkScoreRecord;

import java.io.Serial;
import java.util.Date;

/**
 * 积分记录业务对象 walk_score_record
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkScoreRecord.class, reverseConvertGenerate = false)
public class WalkScoreRecordBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @NotNull(message = "记录ID不能为空", groups = { EditGroup.class })
    private Long recordId;

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
     * 积分类型（1=每日基础分,2=连续7天奖励,3=连续14天奖励,4=21天全勤奖励）
     */
    @NotBlank(message = "积分类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String scoreType;

    /**
     * 积分变动值（正数）
     */
    @NotNull(message = "积分变动值不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer scoreValue;

    /**
     * 关联日期
     */
    private Date relatedDate;

    /**
     * 关联连续打卡记录ID（奖励分用）
     */
    private Long continuousRecordId;

}
