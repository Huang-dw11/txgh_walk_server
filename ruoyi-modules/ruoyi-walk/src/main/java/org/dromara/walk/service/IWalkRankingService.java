package org.dromara.walk.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.vo.WalkOrgRankingVo;
import org.dromara.walk.domain.vo.WalkRankingVo;

import java.util.List;

public interface IWalkRankingService {

    /**
     * 当日步数排名（分页，admin 用）
     */
    TableDataInfo<WalkRankingVo> getDailyStepRanking(Long activityId, String recordDate, PageQuery pageQuery);

    /**
     * 总步数排名（分页，admin 用）
     */
    TableDataInfo<WalkRankingVo> getTotalStepRanking(Long activityId, PageQuery pageQuery);

    /**
     * 积分排名（分页，admin 用）
     */
    TableDataInfo<WalkRankingVo> getScoreRanking(Long activityId, PageQuery pageQuery);

    /**
     * 单位排名（分页，admin 用）
     */
    TableDataInfo<WalkOrgRankingVo> getOrgRankings(Long activityId, PageQuery pageQuery);

    // ===== 全量版（wx 小程序端 / 数据导出用）=====

    /**
     * 当日步数排名（全量）
     */
    List<WalkRankingVo> getDailyStepRankingList(Long activityId, String recordDate);

    /**
     * 总步数排名（全量）
     */
    List<WalkRankingVo> getTotalStepRankingList(Long activityId);

    /**
     * 积分排名（全量）
     */
    List<WalkRankingVo> getScoreRankingList(Long activityId);

    void refreshTotalRankings(Long activityId);

    void refreshOrgRankings(Long activityId);

    void lockRankings(Long activityId);
}
