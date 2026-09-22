package org.dromara.walk.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.walk.domain.vo.WalkOrgRankingVo;
import org.dromara.walk.domain.vo.WalkRankingVo;
import org.dromara.walk.service.IWalkRankingService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/rankings")
@Tag(name = "后台排行榜管理")
public class WalkRankingController extends BaseController {

    private final IWalkRankingService rankingService;

    @Operation(summary = "当日步数排名")
    @SaCheckPermission("walk:ranking:list")
    @GetMapping("/daily")
    public TableDataInfo<WalkRankingVo> dailyRanking(@RequestParam Long activityId,
                                                     @RequestParam(required = false) String date,
                                                     PageQuery pageQuery) {
        return rankingService.getDailyStepRanking(activityId, date, pageQuery);
    }

    @Operation(summary = "总步数排名")
    @SaCheckPermission("walk:ranking:list")
    @GetMapping("/total-steps")
    public TableDataInfo<WalkRankingVo> totalStepRanking(@RequestParam Long activityId, PageQuery pageQuery) {
        return rankingService.getTotalStepRanking(activityId, pageQuery);
    }

    @Operation(summary = "积分排名")
    @SaCheckPermission("walk:ranking:list")
    @GetMapping("/score")
    public TableDataInfo<WalkRankingVo> scoreRanking(@RequestParam Long activityId, PageQuery pageQuery) {
        return rankingService.getScoreRanking(activityId, pageQuery);
    }

    @Operation(summary = "单位排名")
    @SaCheckPermission("walk:ranking:list")
    @GetMapping("/org")
    public TableDataInfo<WalkOrgRankingVo> orgRanking(@RequestParam Long activityId, PageQuery pageQuery) {
        return rankingService.getOrgRankings(activityId, pageQuery);
    }

    @Operation(summary = "手动刷新排行榜")
    @SaCheckPermission("walk:ranking:refresh")
    @Log(title = "排行榜管理", businessType = BusinessType.OTHER)
    @PostMapping("/refresh")
    public R<Void> refresh(@RequestParam Long activityId) {
        rankingService.refreshTotalRankings(activityId);
        return R.ok();
    }

    @Operation(summary = "手动锁榜")
    @SaCheckPermission("walk:ranking:lock")
    @Log(title = "排行榜管理", businessType = BusinessType.OTHER)
    @PostMapping("/lock")
    public R<Void> lock(@RequestParam Long activityId) {
        rankingService.lockRankings(activityId);
        return R.ok();
    }
}
