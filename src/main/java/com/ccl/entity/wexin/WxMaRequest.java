package com.ccl.entity.wexin;

import lombok.Data;

/**
 * @Auther: liuc
 * @Date: 2023/9/6 17:24
 * @Description:
 */
@Data
public class WxMaRequest {

    private String jsCode;

    private String sessionKey;

    private String encryptedData;

    private String ivStr;

    private String appId;
}
