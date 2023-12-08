package com.ccl.entity.wexin;

import lombok.Data;

/**
 * @Auther: liuc
 * @Date: 2023/9/7 11:10
 * @Description:
 */
@Data
public class UserTokenResult {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private String expires_in;

    private String scope;
}
