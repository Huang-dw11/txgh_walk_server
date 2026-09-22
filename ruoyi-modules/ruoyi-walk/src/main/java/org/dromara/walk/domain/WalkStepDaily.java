package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_step_daily")
public class WalkStepDaily extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    @TableId(value = "record_id")
    private Long recordId;

    /** 活动ID */
    private Long activityId;

    /** 会员ID */
    private Long memberId;

    /** 记录日期 */
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
