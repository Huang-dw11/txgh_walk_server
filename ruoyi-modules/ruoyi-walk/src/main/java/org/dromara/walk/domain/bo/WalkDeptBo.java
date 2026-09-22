package org.dromara.walk.domain.bo;

import org.dromara.walk.domain.WalkDept;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = WalkDept.class, reverseConvertGenerate = false)
public class WalkDeptBo extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 部门ID（关联sys_dept） */
    @NotNull(message = "部门ID不能为空", groups = { EditGroup.class })
    private Long deptId;

    /** 会员总数 */
    private Integer memberCount;

    /** 机构级别（县级/镇级/单位级） */
    private String orgLevel;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 部门名称（来自sys_dept，查询条件） */
    private String deptName;

}
