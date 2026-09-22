package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkRanking;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 排名视图对象 walk_ranking
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkRanking.class)
public class WalkRankingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排名ID
     */
    @ExcelProperty(value = "排名ID")
    private Long rankingId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /**
     * 排名类型（1=当日步数,2=总步数,3=积分）
     */
    @ExcelProperty(value = "排名类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "walk_ranking_type")
    private String rankingType;

    /**
     * 记录日期（当日排名用，总排名为空）
     */
    @ExcelProperty(value = "记录日期")
    private Date recordDate;

    /**
     * 会员ID
     */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 排名名次
     */
    @ExcelProperty(value = "排名名次")
    private Integer rankNo;

    /**
     * 排名依据值（步数或积分）
     */
    @ExcelProperty(value = "排名依据值")
    private Integer rankValue;

    /**
     * 所在单位（冗余）
     */
    @ExcelProperty(value = "所在单位")
    private Long deptId;

    /**
     * 会员姓名（来自walk_member，非数据库字段）
     */
    @ExcelProperty(value = "会员姓名")
    private String realName;

    /**
     * 单位名称（来自walk_dept，非数据库字段）
     */
    @ExcelProperty(value = "单位名称")
    private String deptName;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

}
