package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkStepDaily;
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
@AutoMapper(target = WalkStepDaily.class, reverseConvertGenerate = false)
public class WalkStepDailyBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @NotNull(message = "记录ID不能为空", groups = { EditGroup.class })
    private Long recordId;

    /** 活动ID */
    @NotNull(message = "活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /** 会员ID */
    @NotNull(message = "会员ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long memberId;

    /** 记录日期 */
    @NotNull(message = "记录日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date recordDate;

    /** 微信运动原始步数 */
    private Integer wechatSteps;

    /** 有效步数 */
    private Integer effectiveSteps;

    /** 是否达标（Y/N） */
    private String isTarget;

    /** 是否打卡（Y/N） */
    private String isCheckin;

    /** 打卡时间 */
    private Date checkinTime;

    /** 是否锁定（Y=已锁定,N=可更新） */
    private String isLocked;

    /** 该天最终结果已处理（Y/N） */
    private String isSettled;

}
