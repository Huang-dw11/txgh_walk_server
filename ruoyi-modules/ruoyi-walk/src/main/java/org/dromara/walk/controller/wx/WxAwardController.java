package org.dromara.walk.controller.wx;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.walk.domain.vo.WalkAwardVo;
import org.dromara.walk.domain.vo.WalkWinnerVo;
import org.dromara.walk.service.IWalkAwardService;
import org.dromara.walk.service.IWalkWinnerService;
import org.dromara.common.web.core.BaseController;
import org.dromara.walk.utils.WxLoginHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wx")
@Tag(name = "小程序奖项")
public class WxAwardController extends BaseController {

    private final IWalkAwardService awardService;
    private final IWalkWinnerService winnerService;

    @Operation(summary = "奖项列表")
    @GetMapping("/awards")
    public R<List<WalkAwardVo>> awards() {
        Long activityId = WxLoginHelper.getActivityId();
        List<WalkAwardVo> awards = awardService.selectAwardsByActivity(activityId);
        return R.ok(awards);
    }

    @Operation(summary = "我的获奖")
    @GetMapping("/winners/me")
    public R<WalkWinnerVo> myWinner() {
        Long memberId = WxLoginHelper.getMemberId();
        Long activityId = WxLoginHelper.getActivityId();
        WalkWinnerVo winner = winnerService.selectWinnerByMember(activityId, memberId);
        if (winner != null && !"1".equals(winner.getWinStatus())) {
            winner = null;
        }
        return R.ok(winner);
    }

    @Operation(summary = "填写收货地址")
    @PutMapping("/winners/me/address")
    public R<Void> updateAddress(@RequestParam Long winnerId,
                                   @RequestParam String receiver,
                                   @RequestParam String phone,
                                   @RequestParam String address) {
        Long memberId = WxLoginHelper.getMemberId();
        Long activityId = WxLoginHelper.getActivityId();
        WalkWinnerVo winner = winnerService.selectWinnerByMember(activityId, memberId);
        if (winner == null || !"1".equals(winner.getWinStatus()) || !winner.getWinnerId().equals(winnerId)) {
            log.warn("【填写地址】越权校验失败：当前会员={}, winnerId={}, 实际winnerId={}",
                    memberId, winnerId, winner != null ? winner.getWinnerId() : null);
            return R.fail("无权修改他人地址");
        }
        return toAjax(winnerService.updateAddress(winnerId, receiver, phone, address));
    }
}
