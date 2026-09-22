package org.dromara.walk.service;

import java.util.List;
import java.util.Map;

public interface IWalkStatsService {

    Map<String, Object> getSignupStats(Long activityId);

    List<Map<String, Object>> getSignupStatsByDept(Long activityId);

    List<Map<String, Object>> getCheckinDetail(Long activityId, Long deptId);

    List<Map<String, Object>> getScoreDetail(Long activityId, String mobile);
}
