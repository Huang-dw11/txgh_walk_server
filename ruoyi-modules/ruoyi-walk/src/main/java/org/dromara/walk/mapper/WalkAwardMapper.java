package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkAward;
import org.dromara.walk.domain.vo.WalkAwardVo;

import java.util.List;

/**
 * 奖品Mapper接口
 *
 * @author walk
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkAwardMapper extends BaseMapperPlus<WalkAward, WalkAwardVo> {

    /**
     * 根据活动查询奖品列表
     *
     * @param activityId 活动ID
     * @return 奖品列表
     */
    List<WalkAwardVo> selectAwardsByActivity(Long activityId);

}
