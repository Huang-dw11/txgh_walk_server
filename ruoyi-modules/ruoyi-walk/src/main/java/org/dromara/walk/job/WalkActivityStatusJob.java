package org.dromara.walk.job;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.common.log.SnailJobLog;
import com.aizuda.snailjob.model.dto.ExecuteResult;
import lombok.RequiredArgsConstructor;
import org.dromara.walk.service.IWalkActivityService;
import org.springframework.stereotype.Component;

@Component
@JobExecutor(name = "walkActivityStatusJob")
@RequiredArgsConstructor
public class WalkActivityStatusJob {

    private final IWalkActivityService activityService;

    public ExecuteResult jobExecute(JobArgs jobArgs) {
        SnailJobLog.LOCAL.info("【活动状态定时任务】开始执行");
        try {
            activityService.autoUpdateActivityStatus();
            SnailJobLog.LOCAL.info("【活动状态定时任务】执行完毕");
            return ExecuteResult.success("活动状态自动流转完成");
        } catch (Exception e) {
            SnailJobLog.LOCAL.error("【活动状态定时任务】执行失败：{}", e.getMessage(), e);
            return ExecuteResult.failure("活动状态自动流转失败：" + e.getMessage());
        }
    }
}
