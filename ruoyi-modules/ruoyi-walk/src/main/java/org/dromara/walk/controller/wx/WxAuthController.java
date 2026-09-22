package org.dromara.walk.controller.wx;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.walk.domain.WalkMember;
import org.dromara.walk.domain.vo.WalkActivityVo;
import org.dromara.walk.service.IWalkActivityService;
import org.dromara.walk.service.IWalkMemberService;
import org.dromara.walk.service.IWeChatService;
import org.dromara.walk.service.impl.WeChatSessionCache;
import org.dromara.walk.utils.WxLoginHelper;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wx/auth")
@Tag(name = "小程序登录授权")
public class WxAuthController {

    private final IWeChatService weChatService;
    private final IWalkMemberService memberService;
    private final IWalkActivityService activityService;
    private final WeChatSessionCache sessionCache;

    @SaIgnore
    @Operation(summary = "微信登录")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, Object> params) {
        Long activityId = Long.valueOf(params.get("activityId").toString());
        String code = (String) params.get("code");

        log.info("【微信登录】收到请求：activityId={}, code={}", activityId, code);

        WalkActivityVo activity = activityService.queryById(activityId);
        if (activity == null) {
            log.warn("【微信登录】活动不存在或已下线：activityId={}", activityId);
            return R.fail("活动不存在或已下线");
        }

        Map<String, Object> sessionMap = weChatService.code2Session(code);
        String openid = (String) sessionMap.get("openid");
        String sessionKey = (String) sessionMap.get("session_key");

        log.info("【微信登录】code2Session 成功：openid={}", openid);

        sessionCache.put(openid, sessionKey);

        WalkMember boundMember = memberService.selectByOpenid(activityId, openid);
        Long memberId = (boundMember != null) ? boundMember.getMemberId() : null;

        WxLoginHelper.login(memberId, openid, activityId);
        String token = StpUtil.getTokenValue();

        if (memberId != null) {
            log.info("【微信登录】已绑定会员 memberId={}，跳过绑定手机号", memberId);
        }
        log.info("【微信登录】登录成功，openid={}, activityId={}", openid, activityId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", token);
        result.put("openid", openid);
        if (boundMember != null) {
            result.put("memberId", memberId);
        }
        return R.ok(result);
    }

    @Operation(summary = "绑定手机号")
    @PostMapping("/bind-phone")
    public R<Map<String, Object>> bindPhone(@RequestBody Map<String, Object> params) {
        Long activityId = WxLoginHelper.getActivityId();
        String code = (String) params.get("code");

        if (activityId == null) {
            log.warn("【绑定手机号】无法获取 activityId，会话可能已过期");
            return R.fail("会话已过期，请重新登录");
        }

        log.info("【绑定手机号】收到请求：activityId={}", activityId);

        String openid = WxLoginHelper.getOpenid();
        if (openid == null) {
            log.warn("【绑定手机号】无法获取 openid，会话可能已过期");
            return R.fail("会话已过期，请重新登录");
        }

        log.info("【绑定手机号】openid={}", openid);

        String mobile = weChatService.decryptPhoneNumber(code);
        log.info("【绑定手机号】获取手机号成功：{}", mobile);

        WalkMember member = memberService.selectByMobile(activityId, mobile);
        if (member == null) {
            log.warn("【绑定手机号】手机号 {} 未找到报名记录，activityId={}", mobile, activityId);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("mobile", mobile);
            return R.fail("该手机号尚未报名，请先报名", data);
        }

        if (!"1".equals(member.getSignupStatus())) {
            log.warn("【绑定手机号】报名状态异常：memberId={}, signupStatus={}",
                    member.getMemberId(), member.getSignupStatus());
            return R.fail("报名状态异常，请联系管理员");
        }

        memberService.bindWechat(member.getMemberId(), openid, "1");
        member.setOpenid(openid);
        member.setMiniprogramBindStatus("1");

        WxLoginHelper.login(member.getMemberId(), openid, activityId);
        String newToken = StpUtil.getTokenValue();

        log.info("【绑定手机号】绑定成功：memberId={}, openid={}, mobile={}",
                member.getMemberId(), openid, mobile);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("member", member);
        result.put("token", newToken);
        return R.ok(result);
    }

    @SaIgnore
    @Operation(summary = "获取手机号")
    @PostMapping("/get-phone")
    public R<Map<String, Object>> getPhone(@RequestBody Map<String, Object> params) {
        String code = (String) params.get("code");
        log.info("【获取手机号】收到请求");
        String mobile = weChatService.decryptPhoneNumber(code);
        log.info("【获取手机号】成功：{}", mobile);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("mobile", mobile);
        return R.ok(result);
    }
}
