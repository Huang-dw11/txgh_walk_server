package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 步数同步日志对象 walk_step_sync_log
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_step_sync_log")
public class WalkStepSyncLog extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "log_id")
    private Long logId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 同步的目标日期
     */
    private Date syncDate;

    /**
     * 同步步数值
     */
    private Integer syncSteps;

    /**
     * 同步类型（1=当日同步,2=历史补录）
     */
    private String syncType;

    /**
     * 同步时间
     */
    private Date syncTime;

}
