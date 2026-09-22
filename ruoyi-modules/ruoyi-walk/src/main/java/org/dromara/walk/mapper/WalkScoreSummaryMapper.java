package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkScoreSummary;
import org.dromara.walk.domain.vo.WalkScoreSummaryVo;

import java.util.List;

/**
 * 积分汇总 Mapper 接口
 *
 * @author ruoyi
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkScoreSummaryMapper extends BaseMapperPlus<WalkScoreSummary, WalkScoreSummaryVo> {

    /**
     * 根据活动ID和成员ID查询积分汇总
     *
     * @param activityId 活动ID
     * @param memberId   成员ID
     * @return 积分汇总
     */
    WalkScoreSummary selectByMember(@Param("activityId") Long activityId, @Param("memberId") Long memberId);

    /**
     * 查询积分排行榜
     *
     * @param activityId 活动ID
     * @return 排行榜列表
     */
    List<WalkScoreSummary> selectScoreRanking(Long activityId);

    /**
     * 刷新积分汇总（根据积分记录重算总积分）
     *
     * @param activityId 活动ID
     * @param memberId   成员ID
     * @return 影响行数
     */
    int refreshScoreSummary(@Param("activityId") Long activityId, @Param("memberId") Long memberId);
}
