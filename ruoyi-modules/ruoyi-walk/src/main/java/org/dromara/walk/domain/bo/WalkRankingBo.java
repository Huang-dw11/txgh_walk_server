package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkRanking;
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
 * 排名业务对象 walk_ranking
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkRanking.class, reverseConvertGenerate = false)
public class WalkRankingBo extends BaseEntity {

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
     * 排名类型（1=当日步数,2=总步数,3=积分）
     */
    @NotBlank(message = "排名类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String rankingType;

    /**
     * 记录日期（当日排名用，总排名为空）
     */
    private Date recordDate;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 排名名次
     */
    private Integer rankNo;

    /**
     * 排名依据值（步数或积分）
     */
    private Integer rankValue;

    /**
     * 所在单位（冗余）
     */
    private Long deptId;

    /**
     * 会员姓名（来自walk_member，非数据库字段）
     */
    private String realName;

    /**
     * 单位名称（来自walk_dept，非数据库字段）
     */
    private String deptName;

}
