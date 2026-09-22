package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkActivity;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkActivity.class)
public class WalkActivityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /** 活动名称 */
    @ExcelProperty(value = "活动名称")
    private String activityName;

    /** 活动主题 */
    @ExcelProperty(value = "活动主题")
    private String activityTheme;

    /** 活动开始时间 */
    @ExcelProperty(value = "活动开始时间")
    private Date activityStartTime;

    /** 活动结束时间 */
    @ExcelProperty(value = "活动结束时间")
    private Date activityEndTime;

    /** 报名开始时间 */
    @ExcelProperty(value = "报名开始时间")
    private Date signupStartTime;

    /** 报名结束时间 */
    @ExcelProperty(value = "报名结束时间")
    private Date signupEndTime;

    /** 缓冲期时长（天） */
    @ExcelProperty(value = "缓冲期时长（天）")
    private Integer bufferDays;

    /** 锁榜时间 */
    @ExcelProperty(value = "锁榜时间")
    private Date lockTime;

    /** 每日达标步数 */
    @ExcelProperty(value = "每日达标步数")
    private Integer dailyTargetSteps;

    /** 单日有效步数上限 */
    @ExcelProperty(value = "单日有效步数上限")
    private Integer dailyMaxSteps;

    /** 连续7天奖励分 */
    @ExcelProperty(value = "连续7天奖励分")
    private Integer reward7days;

    /** 连续14天奖励分 */
    @ExcelProperty(value = "连续14天奖励分")
    private Integer reward14days;

    /** 全程全勤奖励分 */
    @ExcelProperty(value = "全程全勤奖励分")
    private Integer reward21days;

    /** 7天奖励单周期最多领取次数 */
    @ExcelProperty(value = "7天奖励单周期最多领取次数")
    private Integer reward7daysMaxCount;

    /** 14天奖励单周期最多领取次数 */
    @ExcelProperty(value = "14天奖励单周期最多领取次数")
    private Integer reward14daysMaxCount;

    /** 活动规则说明（富文本） */
    private String activityRule;

    /** 奖项设置说明（富文本） */
    private String awardSetting;

    /** 会员获奖资格说明 */
    private String memberQualificationDesc;

    /** 活动状态（0=未开始,1=报名中,2=进行中,3=缓冲期,4=已结束） */
    @ExcelProperty(value = "活动状态")
    @ExcelDictFormat(dictType = "walk_activity_status")
    private String status;

    /** 是否当前活动（Y=是,N=否） */
    @ExcelProperty(value = "是否当前活动")
    private String isCurrent;

    /** 创建时间 */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
