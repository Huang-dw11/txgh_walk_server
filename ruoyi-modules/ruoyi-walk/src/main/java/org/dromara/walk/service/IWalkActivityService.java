package org.dromara.walk.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.bo.WalkActivityBo;
import org.dromara.walk.domain.vo.WalkActivityVo;

import java.util.Collection;
import java.util.List;

public interface IWalkActivityService {

    WalkActivityVo queryById(Long activityId);

    WalkActivityVo selectCurrentActivity();

    TableDataInfo<WalkActivityVo> queryPageList(WalkActivityBo bo, PageQuery pageQuery);

    List<WalkActivityVo> queryList(WalkActivityBo bo);

    Boolean insertByBo(WalkActivityBo bo);

    Boolean updateByBo(WalkActivityBo bo);

    Boolean updateActivityStatus(Long activityId, String status);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void autoUpdateActivityStatus();
}
