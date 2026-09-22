package org.dromara.walk.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.walk.domain.WalkMember;
import org.dromara.walk.domain.bo.WalkMemberBo;
import org.dromara.walk.domain.vo.WalkMemberVo;

import java.util.Collection;
import java.util.List;

public interface IWalkMemberService {

    WalkMemberVo queryById(Long memberId);

    WalkMember selectByMobile(Long activityId, String mobile);

    WalkMember selectByOpenid(Long activityId, String openid);

    /**
     * 按关键词搜索当前活动已报名会员
     *
     * @param activityId 活动ID
     * @param keyword    关键词（姓名或手机号片段）
     * @return 匹配会员列表
     */
    List<WalkMemberVo> searchByKeyword(Long activityId, String keyword);

    TableDataInfo<WalkMemberVo> queryPageList(WalkMemberBo bo, PageQuery pageQuery);

    List<WalkMemberVo> queryList(WalkMemberBo bo);

    WalkMember signup(WalkMember member);

    WalkMember saveDraft(WalkMember member);

    int bindWechat(Long memberId, String openid, String stepAuthStatus);

    Boolean updateByBo(WalkMemberBo bo);

    int updateMemberStatus(Long memberId, String status);

    int markAbnormal(Long memberId, String abnormalType, String abnormalDesc, String handleBy, String handleAction);

    int batchMarkAbnormal(List<Long> memberIds, String abnormalType, String handleBy, String handleAction);

    int clearAbnormal(Long memberId);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
