package org.dromara.walk.service;

import java.util.Map;

public interface IWeChatService {

    String getAccessToken();

    Map<String, Object> code2Session(String code);

    String decryptPhoneNumber(String code);

    Map<String, Object> decryptWeRunData(String sessionKey, String encryptedData, String iv);
}
