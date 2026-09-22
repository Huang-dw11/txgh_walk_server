package org.dromara.walk.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.bo.WalkAwardBo;
import org.dromara.walk.domain.vo.WalkAwardVo;

import java.util.Collection;
import java.util.List;

public interface IWalkAwardService {

    WalkAwardVo queryById(Long awardId);

    TableDataInfo<WalkAwardVo> queryPageList(WalkAwardBo bo, PageQuery pageQuery);

    List<WalkAwardVo> queryList(WalkAwardBo bo);

    List<WalkAwardVo> selectAwardsByActivity(Long activityId);

    Boolean insertByBo(WalkAwardBo bo);

    Boolean updateByBo(WalkAwardBo bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
