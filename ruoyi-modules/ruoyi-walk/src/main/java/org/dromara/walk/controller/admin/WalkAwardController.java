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
import org.dromara.walk.domain.bo.WalkAwardBo;
import org.dromara.walk.domain.vo.WalkAwardVo;
import org.dromara.walk.service.IWalkAwardService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/awards")
@Tag(name = "后台奖项管理")
public class WalkAwardController extends BaseController {

    private final IWalkAwardService awardService;

    @Operation(summary = "奖项列表")
    @SaCheckPermission("walk:award:list")
    @GetMapping
    public TableDataInfo<WalkAwardVo> list(WalkAwardBo bo, PageQuery pageQuery) {
        return awardService.queryPageList(bo, pageQuery);
    }

    @Operation(summary = "按活动查询奖项")
    @SaCheckPermission("walk:award:list")
    @GetMapping("/activity/{activityId}")
    public R<List<WalkAwardVo>> listByActivity(@PathVariable Long activityId) {
        return R.ok(awardService.selectAwardsByActivity(activityId));
    }

    @Operation(summary = "奖项详情")
    @SaCheckPermission("walk:award:list")
    @GetMapping("/{id}")
    public R<WalkAwardVo> getInfo(@NotNull(message = "奖项ID不能为空") @PathVariable Long id) {
        return R.ok(awardService.queryById(id));
    }

    @Operation(summary = "新增奖项")
    @SaCheckPermission("walk:award:add")
    @Log(title = "奖项管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WalkAwardBo bo) {
        return toAjax(awardService.insertByBo(bo));
    }

    @Operation(summary = "修改奖项")
    @SaCheckPermission("walk:award:edit")
    @Log(title = "奖项管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R<Void> edit(@PathVariable Long id, @Validated(EditGroup.class) @RequestBody WalkAwardBo bo) {
        bo.setAwardId(id);
        return toAjax(awardService.updateByBo(bo));
    }

    @Operation(summary = "删除奖项")
    @SaCheckPermission("walk:award:remove")
    @Log(title = "奖项管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(awardService.deleteWithValidByIds(List.of(ids), true));
    }
}
