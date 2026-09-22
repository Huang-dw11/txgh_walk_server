package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkWinner;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 中奖记录视图对象 walk_winner
 *
 * @author walk
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkWinner.class)
public class WalkWinnerVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 中奖记录ID
     */
    @ExcelProperty(value = "中奖记录ID")
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
    @ExcelProperty(value = "奖项等级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "walk_award_level")
    private String awardLevel;

    /**
     * 收货人
     */
    @ExcelProperty(value = "收货人")
    private String receiver;

    /**
     * 收货电话
     */
    @ExcelProperty(value = "收货电话")
    private String receiverPhone;

    /**
     * 收货地址
     */
    @ExcelProperty(value = "收货地址")
    private String address;

    /**
     * 中奖状态（0=待确认,1=已确认,2=已撤销）
     */
    @ExcelProperty(value = "中奖状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "walk_win_status")
    private String winStatus;

    /**
     * 会员姓名（非数据库字段）
     */
    @ExcelProperty(value = "会员姓名")
    private String realName;

    /**
     * 单位名称（非数据库字段）
     */
    @ExcelProperty(value = "单位名称")
    private String deptName;

    /**
     * 奖项名称（非数据库字段）
     */
    @ExcelProperty(value = "奖项名称")
    private String awardName;

    /**
     * 奖品内容（非数据库字段）
     */
    @ExcelProperty(value = "奖品内容")
    private String prizeContent;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
