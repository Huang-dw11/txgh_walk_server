package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkContinuousCheckin;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 连续打卡记录业务对象 walk_continuous_checkin
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkContinuousCheckin.class, reverseConvertGenerate = false)
public class WalkContinuousCheckinBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @NotNull(message = "记录ID不能为空", groups = EditGroup.class)
    private Long recordId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long activityId;

    /**
     * 会员ID
     */
    @NotNull(message = "会员ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long memberId;

    /**
     * 本段连续打卡起始日期
     */
    private Date startDate;

    /**
     * 连续打卡天数
     */
    private Integer continuousDays;

    /**
     * 是否已中断（Y=已中断,N=进行中）
     */
    private String isBroken;

    /**
     * 中断日期
     */
    private Date brokenDate;

    /**
     * 本段已领7天奖励次数
     */
    private Integer reward7daysCount;

    /**
     * 本段已领14天奖励次数
     */
    private Integer reward14daysCount;

    /**
     * 本段是否已领21天全勤奖励（Y/N）
     */
    private String reward21daysReceived;

}
