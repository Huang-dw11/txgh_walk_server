package org.dromara.walk.job;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.common.log.SnailJobLog;
import com.aizuda.snailjob.model.dto.ExecuteResult;
import lombok.RequiredArgsConstructor;
import org.dromara.walk.domain.vo.WalkActivityVo;
import org.dromara.walk.service.IWalkActivityService;
import org.dromara.walk.service.IWalkRankingService;
import org.springframework.stereotype.Component;

@Component
@JobExecutor(name = "walkScoreRankingJob")
@RequiredArgsConstructor
public class WalkScoreRankingJob {

    private final IWalkActivityService activityService;
    private final IWalkRankingService rankingService;

    public ExecuteResult jobExecute(JobArgs jobArgs) {
        SnailJobLog.LOCAL.info("【积分排名定时任务】开始执行");
        try {
            WalkActivityVo activity = activityService.selectCurrentActivity();
            if (activity == null) {
                SnailJobLog.LOCAL.warn("【积分排名定时任务】未找到当前活动，跳过执行");
                return ExecuteResult.success("无当前活动，跳过");
            }

            Long activityId = activity.getActivityId();
            SnailJobLog.LOCAL.info("【积分排名定时任务】当前活动 activityId={}", activityId);

            rankingService.refreshTotalRankings(activityId);
            SnailJobLog.LOCAL.info("【积分排名定时任务】总排名刷新完成");

            rankingService.refreshOrgRankings(activityId);
            SnailJobLog.LOCAL.info("【积分排名定时任务】单位排名刷新完成");

            return ExecuteResult.success("排名刷新完成");
        } catch (Exception e) {
            SnailJobLog.LOCAL.error("【积分排名定时任务】执行失败：{}", e.getMessage(), e);
            return ExecuteResult.failure("排名刷新失败：" + e.getMessage());
        }
    }
}
