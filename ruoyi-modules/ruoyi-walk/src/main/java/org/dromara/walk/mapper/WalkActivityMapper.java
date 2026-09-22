package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkActivity;
import org.dromara.walk.domain.vo.WalkActivityVo;

/**
 * 活动信息 数据层
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkActivityMapper extends BaseMapperPlus<WalkActivity, WalkActivityVo> {

    /**
     * 查询当前生效的活动
     */
    WalkActivity selectCurrentActivity();

    /**
     * 更新活动状态
     */
    int updateWalkActivityStatus(@Param("activityId") Long activityId, @Param("status") String status);
}
