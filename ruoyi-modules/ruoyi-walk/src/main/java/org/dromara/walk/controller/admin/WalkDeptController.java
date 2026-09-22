package org.dromara.walk.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.walk.domain.bo.WalkDeptBo;
import org.dromara.walk.domain.vo.WalkDeptVo;
import org.dromara.walk.mapper.WalkDeptMapper;
import org.dromara.walk.service.IWalkDeptService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/depts")
@Tag(name = "后台组织机构管理")
public class WalkDeptController extends BaseController {

    private final IWalkDeptService walkDeptService;
    private final WalkDeptMapper walkDeptMapper;

    @Operation(summary = "组织机构列表（分页）")
    @SaCheckPermission("walk:dept:list")
    @GetMapping
    public TableDataInfo<WalkDeptVo> list(WalkDeptBo bo, PageQuery pageQuery) {
        return walkDeptService.queryPageList(bo, pageQuery);
    }

    @Operation(summary = "组织机构列表（不分页，树形用）")
    @SaCheckPermission("walk:dept:list")
    @GetMapping("/list")
    public R<List<WalkDeptVo>> listAll(WalkDeptBo bo) {
        return R.ok(walkDeptService.queryList(bo));
    }

    @Operation(summary = "按关键词搜索单位（用于下拉选择）")
    @SaCheckPermission("walk:winner:add")
    @GetMapping("/search")
    public R<List<WalkDeptVo>> search(@RequestParam(required = false, defaultValue = "") String keyword) {
        WalkDeptBo bo = new WalkDeptBo();
        bo.setDeptName(keyword);
        List<WalkDeptVo> list = walkDeptMapper.selectWalkDeptList(bo);
        List<WalkDeptVo> limited = list.stream().limit(20).collect(Collectors.toList());
        return R.ok(limited);
    }

    @Operation(summary = "可添加的单位列表（未加入walk_dept的sys_dept）")
    @SaCheckPermission("walk:dept:add")
    @GetMapping("/available")
    public R<List<WalkDeptVo>> available(@RequestParam(required = false, defaultValue = "") String keyword) {
        return R.ok(walkDeptMapper.selectAvailableDeptList(keyword));
    }

    @Operation(summary = "组织机构详情")
    @SaCheckPermission("walk:dept:list")
    @GetMapping("/{id}")
    public R<WalkDeptVo> getInfo(@NotNull(message = "部门ID不能为空") @PathVariable Long id) {
        return R.ok(walkDeptService.queryById(id));
    }

    @Operation(summary = "新增组织机构")
    @SaCheckPermission("walk:dept:add")
    @Log(title = "组织机构管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WalkDeptBo bo) {
        return toAjax(walkDeptService.insertByBo(bo));
    }

    @Operation(summary = "修改组织机构")
    @SaCheckPermission("walk:dept:edit")
    @Log(title = "组织机构管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R<Void> edit(@PathVariable Long id, @Validated(EditGroup.class) @RequestBody WalkDeptBo bo) {
        bo.setDeptId(id);
        return toAjax(walkDeptService.updateByBo(bo));
    }

    @Operation(summary = "删除组织机构")
    @SaCheckPermission("walk:dept:remove")
    @Log(title = "组织机构管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(walkDeptService.deleteWithValidByIds(List.of(ids), true));
    }
}
