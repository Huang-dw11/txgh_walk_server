package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkStepSyncLog;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 步数同步日志视图对象 walk_step_sync_log
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkStepSyncLog.class)
public class WalkStepSyncLogVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @ExcelProperty(value = "日志ID")
    private Long logId;

    /**
     * 会员ID
     */
    @ExcelProperty(value = "会员ID")
    private Long memberId;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "活动ID")
    private Long activityId;

    /**
     * 同步的目标日期
     */
    @ExcelProperty(value = "同步的目标日期")
    private Date syncDate;

    /**
     * 同步步数值
     */
    @ExcelProperty(value = "同步步数值")
    private Integer syncSteps;

    /**
     * 同步类型（1=当日同步,2=历史补录）
     */
    @ExcelProperty(value = "同步类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "walk_sync_type")
    private String syncType;

    /**
     * 同步时间
     */
    @ExcelProperty(value = "同步时间")
    private Date syncTime;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
