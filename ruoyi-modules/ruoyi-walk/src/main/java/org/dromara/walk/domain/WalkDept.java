package org.dromara.walk.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("walk_dept")
public class WalkDept extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门ID（关联sys_dept） */
    @TableId(value = "dept_id")
    private Long deptId;

    /** 会员总数 */
    private Integer memberCount;

    /** 机构级别（县级/镇级/单位级） */
    private String orgLevel;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 部门名称（来自sys_dept，非数据库字段） */
    @TableField(exist = false)
    private String deptName;

}
