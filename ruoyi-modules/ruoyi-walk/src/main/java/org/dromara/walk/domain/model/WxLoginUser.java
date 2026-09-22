package org.dromara.walk.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.domain.model.LoginUser;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class WxLoginUser extends LoginUser {

    @Serial
    private static final long serialVersionUID = 1L;

    private String openid;

    private Long activityId;

    public WxLoginUser() {
        setUserType("xcx");
    }
}
