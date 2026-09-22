package org.dromara.walk.service;

import org.dromara.walk.domain.WalkScoreRecord;
import org.dromara.walk.domain.WalkScoreSummary;

import java.util.List;

public interface IWalkScoreService {

    WalkScoreSummary getScoreSummary(Long memberId, Long activityId);

    void handleRealtimeScore(Long memberId, Long activityId, String recordDate);

    void settleDailyScore(Long memberId, Long activityId, String recordDate);

    void checkAndBreakChain(Long memberId, Long activityId);

    void refreshScoreSummary(Long memberId, Long activityId);

    List<WalkScoreSummary> getScoreRanking(Long activityId);

    List<WalkScoreRecord> getMemberScoreRecords(Long memberId, Long activityId);
}
