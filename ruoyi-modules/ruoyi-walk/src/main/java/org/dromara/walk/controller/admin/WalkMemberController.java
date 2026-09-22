package org.dromara.walk.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.walk.domain.bo.WalkMemberBo;
import org.dromara.walk.domain.vo.WalkMemberVo;
import org.dromara.walk.service.IWalkMemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/members")
@Tag(name = "后台会员管理")
public class WalkMemberController extends BaseController {

    private final IWalkMemberService memberService;

    @Operation(summary = "会员列表")
    @SaCheckPermission("walk:member:list")
    @GetMapping
    public TableDataInfo<WalkMemberVo> list(WalkMemberBo bo, PageQuery pageQuery) {
        return memberService.queryPageList(bo, pageQuery);
    }

    @Operation(summary = "按活动搜索已报名会员（用于下拉选择）")
    @SaCheckPermission("walk:winner:add")
    @GetMapping("/search")
    public R<List<WalkMemberVo>> search(@RequestParam Long activityId,
                                        @RequestParam(required = false, defaultValue = "") String keyword) {
        List<WalkMemberVo> list = memberService.searchByKeyword(activityId, keyword);
        return R.ok(list);
    }

    @Operation(summary = "导出会员列表")
    @SaCheckPermission("walk:member:export")
    @Log(title = "会员管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WalkMemberBo bo, HttpServletResponse response) {
        List<WalkMemberVo> list = memberService.queryList(bo);
        ExcelUtil.exportExcel(list, "报名列表", WalkMemberVo.class, response);
    }

    @Operation(summary = "会员详情")
    @SaCheckPermission("walk:member:list")
    @GetMapping("/{id}")
    public R<WalkMemberVo> getInfo(@NotNull(message = "会员ID不能为空") @PathVariable Long id) {
        return R.ok(memberService.queryById(id));
    }

    @Operation(summary = "修改会员")
    @SaCheckPermission("walk:member:edit")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public R<Void> edit(@PathVariable Long id, @Validated(EditGroup.class) @RequestBody WalkMemberBo bo) {
        bo.setMemberId(id);
        return toAjax(memberService.updateByBo(bo));
    }

    @Operation(summary = "变更报名状态")
    @SaCheckPermission("walk:member:edit")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return toAjax(memberService.updateMemberStatus(id, status) > 0);
    }

    @Operation(summary = "标记异常/作弊")
    @SaCheckPermission("walk:member:abnormal")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/abnormal")
    public R<Void> markAbnormal(@PathVariable Long id,
                                @RequestParam(required = false) String abnormalType,
                                @RequestParam(required = false) String desc,
                                @RequestParam(required = false) String handleBy,
                                @RequestParam(required = false) String handleAction) {
        return toAjax(memberService.markAbnormal(id, abnormalType, desc, handleBy, handleAction) > 0);
    }

    @Operation(summary = "批量标记异常/作弊")
    @SaCheckPermission("walk:member:abnormal")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PostMapping("/abnormal/batch")
    public R<Void> batchAbnormal(@RequestParam List<Long> memberIds,
                                 @RequestParam(required = false) String abnormalType,
                                 @RequestParam(required = false) String handleBy,
                                 @RequestParam(required = false) String handleAction) {
        return toAjax(memberService.batchMarkAbnormal(memberIds, abnormalType, handleBy, handleAction) > 0);
    }

    @Operation(summary = "取消异常标记")
    @SaCheckPermission("walk:member:abnormal")
    @Log(title = "会员管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/abnormal/clear")
    public R<Void> clearAbnormal(@PathVariable Long id) {
        return toAjax(memberService.clearAbnormal(id) > 0);
    }

    @Operation(summary = "删除会员")
    @SaCheckPermission("walk:member:remove")
    @Log(title = "会员管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(memberService.deleteWithValidByIds(List.of(ids), true));
    }
}
