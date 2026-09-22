package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkStepSyncLog;
import org.dromara.walk.domain.vo.WalkStepSyncLogVo;

import java.util.List;
import java.util.Map;

/**
 * 步数同步日志 Mapper 接口
 *
 * @author ruoyi
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkStepSyncLogMapper extends BaseMapperPlus<WalkStepSyncLog, WalkStepSyncLogVo> {

    /**
     * 按日期查询最近同步时间
     *
     * @param memberId   成员ID
     * @param activityId 活动ID
     * @return 同步日期与最近同步时间列表
     */
    List<Map<String, Object>> selectLatestSyncTimeByDate(@Param("memberId") Long memberId, @Param("activityId") Long activityId);

    /**
     * 按日期统计同步记录数
     *
     * @param memberId   成员ID
     * @param activityId 活动ID
     * @param syncDate   同步日期（yyyy-MM-dd）
     * @return 记录数
     */
    int countByDate(@Param("memberId") Long memberId, @Param("activityId") Long activityId, @Param("syncDate") String syncDate);
}
