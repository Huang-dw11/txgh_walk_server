package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkRanking;
import org.dromara.walk.domain.vo.WalkRankingVo;

import java.util.List;

/**
 * 排行榜Mapper接口
 *
 * @author walk
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkRankingMapper extends BaseMapperPlus<WalkRanking, WalkRankingVo> {

    /**
     * 根据类型查询排行榜（分页）
     *
     * @param page        分页对象
     * @param activityId  活动ID
     * @param rankingType 排行榜类型
     * @param recordDate  记录日期
     * @return 排行榜分页
     */
    IPage<WalkRankingVo> selectRankingByType(IPage<WalkRankingVo> page,
                                             @Param("activityId") Long activityId,
                                             @Param("rankingType") String rankingType,
                                             @Param("recordDate") String recordDate);

    /**
     * 根据类型查询排行榜（全量，用于小程序端 / 导出）
     *
     * @param activityId  活动ID
     * @param rankingType 排行榜类型
     * @param recordDate  记录日期
     * @return 排行榜列表
     */
    List<WalkRankingVo> selectAllRankingByType(@Param("activityId") Long activityId,
                                               @Param("rankingType") String rankingType,
                                               @Param("recordDate") String recordDate);

    /**
     * 替换插入排行榜
     *
     * @param ranking 排行榜对象
     * @return 影响行数
     */
    int replaceWalkRanking(WalkRanking ranking);

    /**
     * 根据类型删除排行榜
     *
     * @param activityId  活动ID
     * @param rankingType 排行榜类型
     * @param recordDate  记录日期
     * @return 影响行数
     */
    int deleteRankingByType(@Param("activityId") Long activityId, @Param("rankingType") String rankingType, @Param("recordDate") String recordDate);

    /**
     * 批量插入排行榜
     *
     * @param rankings 排行榜列表
     * @return 影响行数
     */
    int batchInsertRanking(List<WalkRanking> rankings);

}
