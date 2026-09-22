package org.dromara.walk.domain.vo;

import org.dromara.walk.domain.WalkDept;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WalkDept.class)
public class WalkDeptVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门ID（关联sys_dept） */
    @ExcelProperty(value = "部门ID")
    private Long deptId;

    /** 会员总数 */
    @ExcelProperty(value = "会员总数")
    private Integer memberCount;

    /** 机构级别（县级/镇级/单位级） */
    @ExcelProperty(value = "机构级别")
    @ExcelDictFormat(dictType = "walk_org_level")
    private String orgLevel;

    /** 联系人 */
    @ExcelProperty(value = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @ExcelProperty(value = "联系电话")
    private String contactPhone;

    /** 部门名称（来自sys_dept，非数据库字段） */
    @ExcelProperty(value = "部门名称")
    private String deptName;

    /** 父级部门ID（来自sys_dept，用于树形展示） */
    private Long parentId;

    /** 创建时间 */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

}
