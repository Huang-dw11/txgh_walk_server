package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 奖项对象 walk_award
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_award")
public class WalkAward extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 奖项ID
     */
    @TableId(value = "award_id")
    private Long awardId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 奖项类型（1=个人奖,2=集体奖）
     */
    private String awardType;

    /**
     * 奖项等级编码（1=一等奖,2=二等奖,3=三等奖,4=优秀奖,5=先进组织单位）
     */
    private String awardLevel;

    /**
     * 奖项名称
     */
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

    /**
     * 删除标志（0=存在,2=删除）
     */
    @TableLogic
    private String delFlag;

}
