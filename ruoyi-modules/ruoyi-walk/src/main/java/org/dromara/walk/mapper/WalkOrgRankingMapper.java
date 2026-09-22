package org.dromara.walk.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.walk.domain.WalkOrgRanking;
import org.dromara.walk.domain.vo.WalkOrgRankingVo;

import java.util.List;

/**
 * 机构排名 Mapper 接口
 *
 * @author ruoyi
 */
@InterceptorIgnore(dataPermission = "true", tenantLine = "true")
public interface WalkOrgRankingMapper extends BaseMapperPlus<WalkOrgRanking, WalkOrgRankingVo> {

    /**
     * 查询机构排名列表（分页）
     *
     * @param page    分页对象
     * @param ranking 查询条件
     * @return 机构排名分页
     */
    IPage<WalkOrgRankingVo> selectWalkOrgRankingList(IPage<WalkOrgRankingVo> page, @Param("ranking") WalkOrgRanking ranking);

    /**
     * 查询机构排名列表（全量，用于导出）
     *
     * @param ranking 查询条件
     * @return 机构排名列表
     */
    List<WalkOrgRankingVo> selectAllWalkOrgRankingList(@Param("ranking") WalkOrgRanking ranking);

    /**
     * 根据活动ID删除机构排名（物理删除）
     *
     * @param activityId 活动ID
     * @return 影响行数
     */
    int deleteOrgRankingByActivity(Long activityId);

    /**
     * 批量新增机构排名
     *
     * @param rankings 机构排名列表
     * @return 影响行数
     */
    int batchInsertOrgRanking(List<WalkOrgRanking> rankings);
}
