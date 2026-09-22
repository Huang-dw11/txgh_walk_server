package org.dromara.walk.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.walk.domain.WalkAward;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 奖项业务对象 walk_award
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkAward.class, reverseConvertGenerate = false)
public class WalkAwardBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 奖项ID
     */
    @NotNull(message = "奖项ID不能为空", groups = { EditGroup.class })
    private Long awardId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /**
     * 奖项类型（1=个人奖,2=集体奖）
     */
    @NotBlank(message = "奖项类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String awardType;

    /**
     * 奖项等级编码（1=一等奖,2=二等奖,3=三等奖,4=优秀奖,5=先进组织单位）
     */
    private String awardLevel;

    /**
     * 奖项名称
     */
    @NotBlank(message = "奖项名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String awardName;

    /**
     * 名额（0=若干名不限）
     */
    private Integer quota;

    /**
     * 奖品内容
     */
    private String prizeContent;

    /**
     * 奖品价值
     */
    private BigDecimal prizeValue;

    /**
     * 排序
     */
    private Integer sortOrder;

}
