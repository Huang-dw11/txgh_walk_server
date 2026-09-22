package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_activity")
public class WalkActivity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    @TableId(value = "activity_id")
    private Long activityId;

    /** 活动名称 */
    private String activityName;

    /** 活动主题 */
    private String activityTheme;

    /** 活动开始时间 */
    private Date activityStartTime;

    /** 活动结束时间 */
    private Date activityEndTime;

    /** 报名开始时间 */
    private Date signupStartTime;

    /** 报名结束时间 */
    private Date signupEndTime;

    /** 缓冲期时长（天） */
    private Integer bufferDays;

    /** 锁榜时间 */
    private Date lockTime;

    /** 每日达标步数 */
    private Integer dailyTargetSteps;

    /** 单日有效步数上限 */
    private Integer dailyMaxSteps;

    /** 连续7天奖励分 */
    @TableField("reward_7days")
    private Integer reward7days;

    /** 连续14天奖励分 */
    @TableField("reward_14days")
    private Integer reward14days;

    /** 全程全勤奖励分 */
    @TableField("reward_21days")
    private Integer reward21days;

    /** 7天奖励单周期最多领取次数 */
    @TableField("reward_7days_max_count")
    private Integer reward7daysMaxCount;

    /** 14天奖励单周期最多领取次数 */
    @TableField("reward_14days_max_count")
    private Integer reward14daysMaxCount;

    /** 活动规则说明（富文本） */
    private String activityRule;

    /** 奖项设置说明（富文本） */
    private String awardSetting;

    /** 会员获奖资格说明 */
    private String memberQualificationDesc;

    /** 活动状态（0=未开始,1=报名中,2=进行中,3=缓冲期,4=已结束） */
    private String status;

    /** 是否当前活动（Y=是,N=否） */
    private String isCurrent;

    /** 删除标志（0=存在,2=删除） */
    @TableLogic
    private String delFlag;

}
