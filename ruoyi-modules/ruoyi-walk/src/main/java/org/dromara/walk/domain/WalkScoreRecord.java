package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * 积分记录对象 walk_score_record
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_score_record")
public class WalkScoreRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "record_id")
    private Long recordId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 积分类型（1=每日基础分,2=连续7天奖励,3=连续14天奖励,4=21天全勤奖励）
     */
    private String scoreType;

    /**
     * 积分变动值（正数）
     */
    private Integer scoreValue;

    /**
     * 关联日期
     */
    private Date relatedDate;

    /**
     * 关联连续打卡记录ID（奖励分用）
     */
    private Long continuousRecordId;

}
