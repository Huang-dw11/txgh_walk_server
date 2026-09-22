package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkOrgRanking;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 单位排名视图对象 walk_org_ranking
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkOrgRanking.class)
public class WalkOrgRankingVo implements Serializable {

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
     * 单位ID
     */
    @ExcelProperty(value = "单位ID")
    private Long deptId;

    /**
     * 会员总数
     */
    @ExcelProperty(value = "会员总数")
    private Integer memberCount;

    /**
     * 报名人数
     */
    @ExcelProperty(value = "报名人数")
    private Integer signupCount;

    /**
     * 审核通过人数
     */
    @ExcelProperty(value = "审核通过人数")
    private Integer approvedCount;

    /**
     * 参与率(%)
     */
    @ExcelProperty(value = "参与率(%)")
    private BigDecimal participationRate;

    /**
     * 打卡率(%)
     */
    @ExcelProperty(value = "打卡率(%)")
    private BigDecimal checkinRate;

    /**
     * 平均积分
     */
    @ExcelProperty(value = "平均积分")
    private BigDecimal avgScore;

    /**
     * 平均步数
     */
    @ExcelProperty(value = "平均步数")
    private BigDecimal avgSteps;

    /**
     * 获奖人数
     */
    @ExcelProperty(value = "获奖人数")
    private Integer awardCount;

    /**
     * 量化计分
     */
    @ExcelProperty(value = "量化计分")
    private BigDecimal orgScore;

    /**
     * 单位排名
     */
    @ExcelProperty(value = "单位排名")
    private Integer rankNo;

    /**
     * 单位名称（非数据库字段）
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
