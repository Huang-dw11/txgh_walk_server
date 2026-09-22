package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 单位排名对象 walk_org_ranking
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_org_ranking")
public class WalkOrgRanking extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排名ID
     */
    @TableId(value = "ranking_id")
    private Long rankingId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 单位ID
     */
    private Long deptId;

    /**
     * 会员总数
     */
    private Integer memberCount;

    /**
     * 报名人数
     */
    private Integer signupCount;

    /**
     * 审核通过人数
     */
    private Integer approvedCount;

    /**
     * 参与率(%)
     */
    private BigDecimal participationRate;

    /**
     * 打卡率(%)
     */
    private BigDecimal checkinRate;

    /**
     * 平均积分
     */
    private BigDecimal avgScore;

    /**
     * 平均步数
     */
    private BigDecimal avgSteps;

    /**
     * 获奖人数
     */
    private Integer awardCount;

    /**
     * 量化计分
     */
    private BigDecimal orgScore;

    /**
     * 单位排名
     */
    private Integer rankNo;

    /**
     * 单位名称（非数据库字段）
     */
    @TableField(exist = false)
    private String deptName;

}
