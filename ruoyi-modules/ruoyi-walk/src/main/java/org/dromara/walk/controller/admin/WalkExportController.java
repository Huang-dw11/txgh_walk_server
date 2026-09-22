package org.dromara.walk.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.walk.domain.bo.WalkMemberBo;
import org.dromara.walk.domain.bo.WalkWinnerBo;
import org.dromara.walk.domain.vo.WalkMemberVo;
import org.dromara.walk.domain.vo.WalkRankingVo;
import org.dromara.walk.domain.vo.WalkWinnerVo;
import org.dromara.walk.service.IWalkMemberService;
import org.dromara.walk.service.IWalkRankingService;
import org.dromara.walk.service.IWalkWinnerService;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/exports")
@Tag(name = "后台数据导出")
public class WalkExportController extends BaseController {

    private final IWalkMemberService memberService;
    private final IWalkRankingService rankingService;
    private final IWalkWinnerService winnerService;

    @Operation(summary = "导出报名列表")
    @SaCheckPermission("walk:export:members")
    @Log(title = "数据导出", businessType = BusinessType.EXPORT)
    @GetMapping("/members")
    public void exportMembers(@RequestParam Long activityId, HttpServletResponse response) {
        WalkMemberBo bo = new WalkMemberBo();
        bo.setActivityId(activityId);
        List<WalkMemberVo> list = memberService.queryList(bo);
        ExcelUtil.exportExcel(list, "报名列表", WalkMemberVo.class, response);
    }

    @Operation(summary = "导出排行榜")
    @SaCheckPermission("walk:export:rankings")
    @Log(title = "数据导出", businessType = BusinessType.EXPORT)
    @GetMapping("/rankings")
    public void exportRankings(@RequestParam Long activityId,
                               @RequestParam String rankingType,
                               HttpServletResponse response) {
        String sheetName;
        List<WalkRankingVo> list;
        switch (rankingType) {
            case "daily" -> {
                list = rankingService.getDailyStepRankingList(activityId, null);
                sheetName = "当日步数排名";
            }
            case "total" -> {
                list = rankingService.getTotalStepRankingList(activityId);
                sheetName = "总步数排名";
            }
            case "score" -> {
                list = rankingService.getScoreRankingList(activityId);
                sheetName = "积分排名";
            }
            default -> {
                return;
            }
        }
        ExcelUtil.exportExcel(list, sheetName, WalkRankingVo.class, response);
    }

    @Operation(summary = "导出中奖名单")
    @SaCheckPermission("walk:export:winners")
    @Log(title = "数据导出", businessType = BusinessType.EXPORT)
    @GetMapping("/winners")
    public void exportWinners(@RequestParam Long activityId, HttpServletResponse response) {
        WalkWinnerBo bo = new WalkWinnerBo();
        bo.setActivityId(activityId);
        List<WalkWinnerVo> list = winnerService.queryList(bo);
        ExcelUtil.exportExcel(list, "中奖名单", WalkWinnerVo.class, response);
    }
}
