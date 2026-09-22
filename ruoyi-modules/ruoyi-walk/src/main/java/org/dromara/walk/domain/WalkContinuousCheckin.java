package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 连续打卡记录对象 walk_continuous_checkin
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_continuous_checkin")
public class WalkContinuousCheckin extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "record_id")
    private Long recordId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 会员ID
     */
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
    @TableField("reward_7days_count")
    private Integer reward7daysCount;

    /**
     * 本段已领14天奖励次数
     */
    @TableField("reward_14days_count")
    private Integer reward14daysCount;

    /**
     * 本段是否已领21天全勤奖励（Y/N）
     */
    @TableField("reward_21days_received")
    private String reward21daysReceived;

}
