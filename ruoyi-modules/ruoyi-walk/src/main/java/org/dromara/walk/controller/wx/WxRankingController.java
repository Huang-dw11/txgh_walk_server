package org.dromara.walk.controller.wx;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.walk.domain.vo.WalkRankingVo;
import org.dromara.walk.service.IWalkRankingService;
import org.dromara.walk.utils.WxLoginHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wx/rankings")
@Tag(name = "小程序排行榜")
public class WxRankingController {

    private final IWalkRankingService rankingService;

    @Operation(summary = "当日步数排名")
    @GetMapping("/daily")
    public R<List<WalkRankingVo>> dailyRanking(@RequestParam(required = false) String date) {
        Long activityId = WxLoginHelper.getActivityId();
        log.info("【排行榜】当日步数排名：activityId={}, date={}", activityId, date);
        List<WalkRankingVo> rankings = rankingService.getDailyStepRankingList(activityId, date);
        log.info("【排行榜】当日步数排名返回 {} 条记录", rankings.size());
        return R.ok(rankings);
    }

    @Operation(summary = "总步数排名")
    @GetMapping("/total-steps")
    public R<List<WalkRankingVo>> totalStepRanking() {
        Long activityId = WxLoginHelper.getActivityId();
        log.info("【排行榜】总步数排名：activityId={}", activityId);
        List<WalkRankingVo> rankings = rankingService.getTotalStepRankingList(activityId);
        log.info("【排行榜】总步数排名返回 {} 条记录", rankings.size());
        return R.ok(rankings);
    }

    @Operation(summary = "积分排名")
    @GetMapping("/score")
    public R<List<WalkRankingVo>> scoreRanking() {
        Long activityId = WxLoginHelper.getActivityId();
        log.info("【排行榜】积分排名：activityId={}", activityId);
        List<WalkRankingVo> rankings = rankingService.getScoreRankingList(activityId);
        log.info("【排行榜】积分排名返回 {} 条记录", rankings.size());
        return R.ok(rankings);
    }
}
