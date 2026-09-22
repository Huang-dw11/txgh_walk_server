package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkAward;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 奖项视图对象 walk_award
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkAward.class)
public class WalkAwardVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 奖项ID
     */
    @ExcelProperty(value = "奖项ID")
    private Long awardId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /**
     * 奖项类型（1=个人奖,2=集体奖）
     */
    @ExcelProperty(value = "奖项类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "walk_award_type")
    private String awardType;

    /**
     * 奖项等级编码（1=一等奖,2=二等奖,3=三等奖,4=优秀奖,5=先进组织单位）
     */
    @ExcelProperty(value = "奖项等级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "walk_award_level")
    private String awardLevel;

    /**
     * 奖项名称
     */
    @ExcelProperty(value = "奖项名称")
    private String awardName;

    /**
     * 名额（0=若干名不限）
     */
    @ExcelProperty(value = "名额")
    private Integer quota;

    /**
     * 奖品内容
     */
    @ExcelProperty(value = "奖品内容")
    private String prizeContent;

    /**
     * 奖品价值
     */
    @ExcelProperty(value = "奖品价值")
    private BigDecimal prizeValue;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
