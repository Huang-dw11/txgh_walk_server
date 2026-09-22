package org.dromara.walk.controller.admin;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.walk.service.IWalkStatsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/stats")
@Tag(name = "后台数据统计")
public class WalkStatsController extends BaseController {

    private final IWalkStatsService statsService;

    @Operation(summary = "报名统计")
    @SaCheckPermission("walk:stats:list")
    @GetMapping("/signup")
    public R<Map<String, Object>> signupStats(@RequestParam Long activityId) {
        return R.ok(statsService.getSignupStats(activityId));
    }

    @Operation(summary = "按单位报名统计")
    @SaCheckPermission("walk:stats:list")
    @GetMapping("/signup-by-dept")
    public R<List<Map<String, Object>>> signupByDept(@RequestParam Long activityId) {
        return R.ok(statsService.getSignupStatsByDept(activityId));
    }

    @Operation(summary = "打卡明细")
    @SaCheckPermission("walk:stats:list")
    @GetMapping("/checkin-detail")
    public R<List<Map<String, Object>>> checkinDetail(@RequestParam Long activityId,
                                                       @RequestParam(required = false) Long deptId) {
        return R.ok(statsService.getCheckinDetail(activityId, deptId));
    }

    @Operation(summary = "积分明细")
    @SaCheckPermission("walk:stats:list")
    @GetMapping("/score-detail")
    public R<List<Map<String, Object>>> scoreDetail(@RequestParam Long activityId, @RequestParam String mobile) {
        return R.ok(statsService.getScoreDetail(activityId, mobile));
    }
}
