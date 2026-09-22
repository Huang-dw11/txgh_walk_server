package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkScoreRecord;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 积分记录视图对象 walk_score_record
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkScoreRecord.class)
public class WalkScoreRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @ExcelProperty(value = "记录ID")
    private Long recordId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /**
     * 会员ID
     */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 积分类型（1=每日基础分,2=连续7天奖励,3=连续14天奖励,4=21天全勤奖励）
     */
    @ExcelProperty(value = "积分类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "walk_score_type")
    private String scoreType;

    /**
     * 积分变动值（正数）
     */
    @ExcelProperty(value = "积分变动值")
    private Integer scoreValue;

    /**
     * 关联日期
     */
    @ExcelProperty(value = "关联日期")
    private Date relatedDate;

    /**
     * 关联连续打卡记录ID（奖励分用）
     */
    @ExcelProperty(value = "关联连续打卡记录ID")
    private Long continuousRecordId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
