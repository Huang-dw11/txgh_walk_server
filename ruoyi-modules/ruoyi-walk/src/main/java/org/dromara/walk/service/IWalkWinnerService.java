package org.dromara.walk.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.bo.WalkWinnerBo;
import org.dromara.walk.domain.vo.WalkWinnerVo;

import java.util.Collection;
import java.util.List;

public interface IWalkWinnerService {

    WalkWinnerVo queryById(Long winnerId);

    WalkWinnerVo selectWinnerByMember(Long activityId, Long memberId);

    TableDataInfo<WalkWinnerVo> queryPageList(WalkWinnerBo bo, PageQuery pageQuery);

    List<WalkWinnerVo> queryList(WalkWinnerBo bo);

    Boolean insertByBo(WalkWinnerBo bo);

    Boolean updateByBo(WalkWinnerBo bo);

    Boolean updateAddress(Long winnerId, String receiver, String phone, String address);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
