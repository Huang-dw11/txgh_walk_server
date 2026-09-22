package org.dromara.walk.service;

import org.dromara.walk.domain.WalkStepDaily;

import java.util.List;
import java.util.Map;

public interface IWalkStepService {

    WalkStepDaily syncSteps(Long memberId, Long activityId, Integer wechatSteps);

    WalkStepDaily syncStepsByDate(Long memberId, Long activityId, String recordDate, Integer wechatSteps);

    WalkStepDaily checkin(Long memberId, Long activityId);

    WalkStepDaily getTodaySteps(Long memberId, Long activityId);

    List<WalkStepDaily> getDailyRanking(Long activityId, String recordDate);

    List<WalkStepDaily> getMemberStepHistory(Long memberId, Long activityId);

    void autoCheckin(Long memberId, Long activityId);

    Map<String, Object> getHomeData(Long memberId, Long activityId);
}
