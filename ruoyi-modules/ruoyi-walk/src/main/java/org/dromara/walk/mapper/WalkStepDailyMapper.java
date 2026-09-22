package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkStepDaily;
import org.dromara.walk.domain.vo.WalkStepDailyVo;

import java.util.List;

/**
 * 每日步数 数据层
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkStepDailyMapper extends BaseMapperPlus<WalkStepDaily, WalkStepDailyVo> {

    /**
     * 查询成员某日步数记录
     */
    WalkStepDaily selectMemberStepByDate(@Param("memberId") Long memberId, @Param("activityId") Long activityId, @Param("recordDate") String recordDate);

    /**
     * 查询某日步数排行榜
     */
    List<WalkStepDaily> selectDailyRanking(@Param("activityId") Long activityId, @Param("recordDate") String recordDate);

    /**
     * 锁定指定日期及之前的步数记录
     */
    int updateLocked(@Param("activityId") Long activityId, @Param("recordDate") String recordDate);

    /**
     * 新增或更新每日步数（存在则更新）
     */
    int upsertWalkStepDaily(WalkStepDaily stepDaily);

    /**
     * 新增或更新每日步数（自动生成雪花ID）
     */
    default int upsertWalkStepDailyWithId(WalkStepDaily stepDaily) {
        if (stepDaily.getRecordId() == null) {
            stepDaily.setRecordId(IdWorker.getId());
        }
        return upsertWalkStepDaily(stepDaily);
    }

    /**
     * 查询指定日期范围内未结算的步数记录
     */
    List<WalkStepDaily> selectUnsettledByDate(@Param("memberId") Long memberId, @Param("activityId") Long activityId, @Param("activityStart") String activityStart, @Param("beforeDate") String beforeDate);

    /**
     * 标记某日步数为已结算
     */
    int markSettled(@Param("memberId") Long memberId, @Param("activityId") Long activityId, @Param("recordDate") String recordDate);
}
