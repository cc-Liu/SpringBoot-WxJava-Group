package com.ccl.entity.wexin;

import lombok.Data;

/**
 * @Auther: liuc
 * @Date: 2023/9/8 13:44
 * @Description:
 */
@Data
public class UserTokenRequest {

    private String phone;

    private String openid;
}
