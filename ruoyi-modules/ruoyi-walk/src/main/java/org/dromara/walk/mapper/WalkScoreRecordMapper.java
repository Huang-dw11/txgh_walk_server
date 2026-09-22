package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkScoreRecord;
import org.dromara.walk.domain.vo.WalkScoreRecordVo;

import java.util.List;

/**
 * 积分记录Mapper接口
 *
 * @author walk
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkScoreRecordMapper extends BaseMapperPlus<WalkScoreRecord, WalkScoreRecordVo> {

    /**
     * 查询唯一积分记录
     *
     * @param memberId   成员ID
     * @param scoreType  积分类型
     * @param relatedDate 关联日期
     * @return 积分记录
     */
    WalkScoreRecord selectScoreRecordUnique(@Param("memberId") Long memberId, @Param("scoreType") String scoreType, @Param("relatedDate") String relatedDate);

    /**
     * 查询成员积分记录列表
     *
     * @param memberId   成员ID
     * @param activityId 活动ID
     * @return 积分记录列表
     */
    List<WalkScoreRecord> selectMemberScoreRecords(@Param("memberId") Long memberId, @Param("activityId") Long activityId);

}
