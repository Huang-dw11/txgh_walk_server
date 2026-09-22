package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkWinner;
import org.dromara.walk.domain.vo.WalkWinnerVo;

/**
 * 中奖者Mapper接口
 *
 * @author walk
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkWinnerMapper extends BaseMapperPlus<WalkWinner, WalkWinnerVo> {

    /**
     * 根据成员查询中奖记录
     *
     * @param activityId 活动ID
     * @param memberId   成员ID
     * @return 中奖记录
     */
    WalkWinner selectWinnerByMember(@Param("activityId") Long activityId, @Param("memberId") Long memberId);

    /**
     * 更新中奖者收货地址
     *
     * @param winner 中奖者对象
     * @return 影响行数
     */
    int updateWalkWinnerAddress(WalkWinner winner);

}
