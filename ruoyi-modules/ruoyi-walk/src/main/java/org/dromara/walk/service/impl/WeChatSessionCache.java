package org.dromara.walk.service.impl;

import org.dromara.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class WeChatSessionCache {

    private static final String KEY_PREFIX = "walk:wechat:session:";
    private static final Duration TTL = Duration.ofDays(1);

    public void put(String openid, String sessionKey) {
        RedisUtils.setCacheObject(KEY_PREFIX + openid, sessionKey, TTL);
    }

    public String get(String openid) {
        return RedisUtils.getCacheObject(KEY_PREFIX + openid);
    }

    public void remove(String openid) {
        RedisUtils.deleteObject(KEY_PREFIX + openid);
    }
}
