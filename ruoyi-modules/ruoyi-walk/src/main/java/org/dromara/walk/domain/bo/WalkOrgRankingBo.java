package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkOrgRanking;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 单位排名业务对象 walk_org_ranking
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkOrgRanking.class, reverseConvertGenerate = false)
public class WalkOrgRankingBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 排名ID
     */
    @NotNull(message = "排名ID不能为空", groups = EditGroup.class)
    private Long rankingId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long activityId;

    /**
     * 单位ID
     */
    @NotNull(message = "单位ID不能为空", groups = {AddGroup.class, EditGroup.class})
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
    private String deptName;

}
