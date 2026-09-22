package org.dromara.walk.utils;

import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.walk.domain.model.WxLoginUser;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WxLoginHelper {

    public static WxLoginUser getWxLoginUser() {
        return LoginHelper.getLoginUser();
    }

    public static Long getMemberId() {
        return LoginHelper.getUserId();
    }

    public static String getOpenid() {
        WxLoginUser wxUser = getWxLoginUser();
        return wxUser != null ? wxUser.getOpenid() : null;
    }

    public static Long getActivityId() {
        WxLoginUser wxUser = getWxLoginUser();
        return wxUser != null ? wxUser.getActivityId() : null;
    }

    public static void login(Long memberId, String openid, Long activityId) {
        WxLoginUser wxUser = new WxLoginUser();
        wxUser.setUserId(memberId != null ? memberId : 0L);
        wxUser.setOpenid(openid);
        wxUser.setActivityId(activityId);
        SaLoginParameter model = new SaLoginParameter();
        model.setExtra(LoginHelper.CLIENT_KEY, "be7052a7e4f802c20df10a8d131adb12");
        LoginHelper.login(wxUser, model);
    }
}
