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
import org.dromara.walk.domain.bo.WalkWinnerBo;
import org.dromara.walk.domain.vo.WalkWinnerVo;
import org.dromara.walk.service.IWalkWinnerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/winners")
@Tag(name = "后台中奖名单管理")
public class WalkWinnerController extends BaseController {

    private final IWalkWinnerService winnerService;

    @Operation(summary = "中奖名单列表")
    @SaCheckPermission("walk:winner:list")
    @GetMapping
    public TableDataInfo<WalkWinnerVo> list(WalkWinnerBo bo, PageQuery pageQuery) {
        return winnerService.queryPageList(bo, pageQuery);
    }

    @Operation(summary = "导出中奖名单")
    @SaCheckPermission("walk:winner:export")
    @Log(title = "中奖名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WalkWinnerBo bo, HttpServletResponse response) {
        List<WalkWinnerVo> list = winnerService.queryList(bo);
        ExcelUtil.exportExcel(list, "中奖名单", WalkWinnerVo.class, response);
    }

    @Operation(summary = "中奖详情")
    @SaCheckPermission("walk:winner:list")
    @GetMapping("/{id}")
    public R<WalkWinnerVo> getInfo(@NotNull(message = "中奖记录ID不能为空") @PathVariable Long id) {
        return R.ok(winnerService.queryById(id));
    }

    @Operation(summary = "标记中奖")
    @SaCheckPermission("walk:winner:add")
    @Log(title = "中奖名单", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WalkWinnerBo bo) {
        return toAjax(winnerService.insertByBo(bo));
    }

    @Operation(summary = "修改中奖记录")
    @SaCheckPermission("walk:winner:edit")
    @Log(title = "中奖名单", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R<Void> edit(@PathVariable Long id, @Validated(EditGroup.class) @RequestBody WalkWinnerBo bo) {
        bo.setWinnerId(id);
        return toAjax(winnerService.updateByBo(bo));
    }

    @Operation(summary = "删除中奖记录")
    @SaCheckPermission("walk:winner:remove")
    @Log(title = "中奖名单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(winnerService.deleteWithValidByIds(List.of(ids), true));
    }
}
