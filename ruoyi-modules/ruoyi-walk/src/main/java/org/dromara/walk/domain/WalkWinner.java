package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 中奖记录对象 walk_winner
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_winner")
public class WalkWinner extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 中奖记录ID
     */
    @TableId(value = "winner_id")
    private Long winnerId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 奖项ID
     */
    private Long awardId;

    /**
     * 会员ID（个人奖项）
     */
    private Long memberId;

    /**
     * 单位ID（集体奖项）
     */
    private Long deptId;

    /**
     * 奖项等级编码（冗余）
     */
    private String awardLevel;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货电话
     */
    private String receiverPhone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 中奖状态（0=待确认,1=已确认,2=已撤销）
     */
    private String winStatus;

    /**
     * 删除标志
     */
    @TableLogic
    private String delFlag;

    /**
     * 会员姓名（非数据库字段）
     */
    @TableField(exist = false)
    private String realName;

    /**
     * 单位名称（非数据库字段）
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 奖项名称（非数据库字段）
     */
    @TableField(exist = false)
    private String awardName;

    /**
     * 奖品内容（非数据库字段）
     */
    @TableField(exist = false)
    private String prizeContent;

}
