package org.dromara.walk.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.walk.service.IWeChatService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeChatServiceImpl implements IWeChatService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WxMaService wxMaService;

    @Override
    public String getAccessToken() {
        try {
            String token = wxMaService.getAccessToken();
            log.info("获取微信 access_token 成功");
            return token;
        } catch (WxErrorException e) {
            log.error("获取微信 access_token 失败", e);
            throw new ServiceException("获取微信 access_token 失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> code2Session(String code) {
        log.info("请求微信 code2Session，code={}", code);
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            log.info("微信 code2Session 成功，openid={}", session.getOpenid());

            Map<String, Object> result = new HashMap<>();
            result.put("openid", session.getOpenid());
            result.put("session_key", session.getSessionKey());
            return result;
        } catch (WxErrorException e) {
            log.error("微信 code2Session 失败：{}，code={}", e.getMessage(), code);
            throw new ServiceException("微信登录失败：" + e.getMessage());
        }
    }

    @Override
    public String decryptPhoneNumber(String code) {
        log.info("请求微信 getPhoneNumber，code={}", code);
        try {
            String phoneNumber = wxMaService.getUserService().getPhoneNoInfo(code).getPhoneNumber();
            log.info("微信 getPhoneNumber 成功，手机号={}", phoneNumber);
            return phoneNumber;
        } catch (WxErrorException e) {
            log.error("微信 getPhoneNumber 失败：{}", e.getMessage());
            throw new ServiceException("获取手机号失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> decryptWeRunData(String sessionKey, String encryptedData, String iv) {
        log.info("开始解密微信运动步数数据");
        try {
            byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] dataBytes = Base64.getDecoder().decode(encryptedData);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(dataBytes);

            String json = new String(decrypted, StandardCharsets.UTF_8);
            log.info("微信运动步数解密成功，JSON 内容：{}", json);

            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error("微信运动步数解密失败", e);
            throw new ServiceException("微信运动步数解密失败：" + e.getMessage());
        }
    }
}
