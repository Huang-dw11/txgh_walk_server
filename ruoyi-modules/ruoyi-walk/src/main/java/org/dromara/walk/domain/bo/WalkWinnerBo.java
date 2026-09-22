package org.dromara.walk.domain.bo;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.walk.domain.WalkWinner;

import java.io.Serial;

/**
 * 中奖记录业务对象 walk_winner
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkWinner.class, reverseConvertGenerate = false)
public class WalkWinnerBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 中奖记录ID
     */
    @NotNull(message = "中奖记录ID不能为空", groups = { EditGroup.class })
    private Long winnerId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long activityId;

    /**
     * 奖项ID
     */
    @NotNull(message = "奖项ID不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 查询用：姓名关键词
     */
    private String realName;

    /**
     * 查询用：手机号关键词
     */
    private String mobile;

}
