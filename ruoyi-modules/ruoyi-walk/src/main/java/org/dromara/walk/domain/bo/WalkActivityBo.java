package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkActivity;
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
@AutoMapper(target = WalkActivity.class, reverseConvertGenerate = false)
public class WalkActivityBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    @NotNull(message = "活动ID不能为空", groups = { EditGroup.class })
    private Long activityId;

    /** 活动名称 */
    @NotBlank(message = "活动名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String activityName;

    /** 活动主题 */
    private String activityTheme;

    /** 活动开始时间 */
    @NotNull(message = "活动开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date activityStartTime;

    /** 活动结束时间 */
    @NotNull(message = "活动结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date activityEndTime;

    /** 报名开始时间 */
    @NotNull(message = "报名开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date signupStartTime;

    /** 报名结束时间 */
    @NotNull(message = "报名结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
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
    private Integer reward7days;

    /** 连续14天奖励分 */
    private Integer reward14days;

    /** 全程全勤奖励分 */
    private Integer reward21days;

    /** 7天奖励单周期最多领取次数 */
    private Integer reward7daysMaxCount;

    /** 14天奖励单周期最多领取次数 */
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

}
