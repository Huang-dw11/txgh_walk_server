package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkStepSyncLog;
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
 * 步数同步日志业务对象 walk_step_sync_log
 *
 * @author walk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkStepSyncLog.class, reverseConvertGenerate = false)
public class WalkStepSyncLogBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @NotNull(message = "日志ID不能为空", groups = EditGroup.class)
    private Long logId;

    /**
     * 会员ID
     */
    @NotNull(message = "会员ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long memberId;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long activityId;

    /**
     * 同步的目标日期
     */
    @NotNull(message = "同步的目标日期不能为空", groups = {AddGroup.class, EditGroup.class})
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
