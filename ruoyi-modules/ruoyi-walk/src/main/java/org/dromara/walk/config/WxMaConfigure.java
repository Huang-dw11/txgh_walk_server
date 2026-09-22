package org.dromara.walk.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMaProperty.class)
public class WxMaConfigure {

    private final WxMaProperty properties;

    @Bean
    public WxMaService wxMaService() {
        List<WxMaProperty.Config> configs = this.properties.getConfigs();
        if (configs == null || configs.isEmpty()) {
            throw new WxRuntimeException("微信小程序配置缺失，请在 application.yml 中配置 wx.miniapp.configs");
        }

        WxMaService maService = new WxMaServiceImpl();
        maService.setMultiConfigs(
            configs.stream()
                .map(a -> {
                    WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                    config.setAppid(a.getAppid());
                    config.setSecret(a.getSecret());
                    config.setToken(a.getToken());
                    config.setAesKey(a.getAesKey());
                    config.setMsgDataFormat(a.getMsgDataFormat());
                    return config;
                })
                .collect(Collectors.toMap(
                    WxMaDefaultConfigImpl::getAppid,
                    a -> a,
                    (o, n) -> o
                ))
        );

        log.info("微信小程序 WxJava SDK 初始化完成，已加载 {} 个配置", configs.size());
        return maService;
    }
}
