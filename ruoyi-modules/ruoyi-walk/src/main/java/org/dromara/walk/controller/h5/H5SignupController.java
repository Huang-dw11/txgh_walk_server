package org.dromara.walk.controller.h5;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.walk.domain.WalkMember;
import org.dromara.walk.service.IWalkMemberService;
import org.dromara.walk.utils.WxLoginHelper;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@SaIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/h5/signup")
@Tag(name = "H5报名")
public class H5SignupController {

    private final IWalkMemberService memberService;

    @Operation(summary = "提交报名")
    @PostMapping
    public R<Map<String, Object>> signup(@RequestBody WalkMember member) {
        boolean fromMiniProgram = StringUtils.isNotEmpty(member.getOpenid());
        log.info("【H5报名】提交报名：mobile={}, name={}, deptId={}, 小程序场景={}",
                member.getMobile(), member.getRealName(), member.getDeptId(), fromMiniProgram);

        if (fromMiniProgram) {
            WalkMember bound = memberService.selectByOpenid(member.getActivityId(), member.getOpenid());
            if (bound != null && !bound.getMobile().equals(member.getMobile())) {
                log.warn("【H5报名】openid 已绑定其他报名档案：openid={}, 已绑 mobile={}, 本次 mobile={}",
                        member.getOpenid(), bound.getMobile(), member.getMobile());
                return R.fail("该微信已绑定其他报名档案");
            }
        }

        WalkMember result = memberService.signup(member);
        log.info("【H5报名】报名成功：memberId={}", result.getMemberId());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("member", result);

        if (fromMiniProgram) {
            WxLoginHelper.login(result.getMemberId(), result.getOpenid(), result.getActivityId());
            String newToken = StpUtil.getTokenValue();
            data.put("token", newToken);
            log.info("【H5报名】小程序场景已签发新 token：memberId={}, openid={}",
                    result.getMemberId(), result.getOpenid());
        }
        return R.ok(data);
    }

    @Deprecated
    @Operation(summary = "保存草稿")
    @PutMapping("/draft")
    public R<WalkMember> saveDraft(@RequestBody WalkMember member) {
        return R.ok(memberService.saveDraft(member));
    }

    @Operation(summary = "查询报名状态")
    @GetMapping("/status")
    public R<WalkMember> getStatus(@RequestParam Long activityId, @RequestParam String mobile) {
        log.info("【H5报名】查询状态：activityId={}, mobile={}", activityId, mobile);
        WalkMember member = memberService.selectByMobile(activityId, mobile);
        if (member == null) {
            log.warn("【H5报名】未查询到报名信息：activityId={}, mobile={}", activityId, mobile);
            return R.fail("未查询到报名信息");
        }
        log.info("【H5报名】查询成功：memberId={}, name={}", member.getMemberId(), member.getRealName());
        return R.ok(member);
    }
}
