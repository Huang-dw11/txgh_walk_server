package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkContinuousCheckin;
import org.dromara.walk.domain.vo.WalkContinuousCheckinVo;

/**
 * 连续签到 Mapper 接口
 *
 * @author ruoyi
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkContinuousCheckinMapper extends BaseMapperPlus<WalkContinuousCheckin, WalkContinuousCheckinVo> {

    /**
     * 查询当前未断签的连续签到记录
     *
     * @param memberId   成员ID
     * @param activityId 活动ID
     * @return 连续签到记录
     */
    WalkContinuousCheckin selectCurrentContinuous(@Param("memberId") Long memberId, @Param("activityId") Long activityId);

    /**
     * 断签处理
     *
     * @param record 连续签到记录
     * @return 影响行数
     */
    int breakContinuousCheckin(WalkContinuousCheckin record);
}
