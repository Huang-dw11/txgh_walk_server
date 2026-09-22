package org.dromara.walk.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.walk.domain.bo.WalkActivityBo;
import org.dromara.walk.domain.vo.WalkActivityVo;
import org.dromara.walk.service.IWalkActivityService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/activities")
@Tag(name = "后台活动管理")
public class WalkActivityController extends BaseController {

    private final IWalkActivityService activityService;

    @Operation(summary = "活动列表")
    @SaCheckPermission("walk:activity:list")
    @GetMapping
    public TableDataInfo<WalkActivityVo> list(WalkActivityBo bo, PageQuery pageQuery) {
        return activityService.queryPageList(bo, pageQuery);
    }

    @Operation(summary = "导出活动列表")
    @SaCheckPermission("walk:activity:export")
    @Log(title = "活动管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WalkActivityBo bo, HttpServletResponse response) {
        List<WalkActivityVo> list = activityService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动数据", WalkActivityVo.class, response);
    }

    @Operation(summary = "活动详情")
    @SaCheckPermission("walk:activity:list")
    @GetMapping("/{id}")
    public R<WalkActivityVo> getInfo(@NotNull(message = "活动ID不能为空") @PathVariable Long id) {
        return R.ok(activityService.queryById(id));
    }

    @Operation(summary = "获取当前活动")
    @GetMapping("/current")
    public R<WalkActivityVo> getCurrentActivity() {
        return R.ok(activityService.selectCurrentActivity());
    }

    @Operation(summary = "新增活动")
    @SaCheckPermission("walk:activity:add")
    @Log(title = "活动管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WalkActivityBo bo) {
        return toAjax(activityService.insertByBo(bo));
    }

    @Operation(summary = "修改活动")
    @SaCheckPermission("walk:activity:edit")
    @Log(title = "活动管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R<Void> edit(@PathVariable Long id, @Validated(EditGroup.class) @RequestBody WalkActivityBo bo) {
        bo.setActivityId(id);
        return toAjax(activityService.updateByBo(bo));
    }

    @Operation(summary = "变更活动状态")
    @SaCheckPermission("walk:activity:edit")
    @Log(title = "活动管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return toAjax(activityService.updateActivityStatus(id, status));
    }

    @Operation(summary = "删除活动")
    @SaCheckPermission("walk:activity:remove")
    @Log(title = "活动管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(activityService.deleteWithValidByIds(List.of(ids), true));
    }
}
