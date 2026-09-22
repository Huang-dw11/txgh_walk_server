package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 排名对象 walk_ranking
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_ranking")
public class WalkRanking extends BaseEntity {

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
     * 排名类型（1=当日步数,2=总步数,3=积分）
     */
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
    @TableField(exist = false)
    private String realName;

    /**
     * 单位名称（来自walk_dept，非数据库字段）
     */
    @TableField(exist = false)
    private String deptName;

}
