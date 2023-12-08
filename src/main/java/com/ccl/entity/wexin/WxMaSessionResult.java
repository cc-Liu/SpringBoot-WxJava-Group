package com.ccl.entity.wexin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liuc
 * @Date: 2023/9/6 17:23
 * @Description:
 */
@Data
public class WxMaSessionResult {

    @ApiModelProperty(value = "sessionKey")
    private String sessionKey;

    @ApiModelProperty(value = "用户openId")
    private String openid;

    @ApiModelProperty(value = "用户unionid")
    private String unionid;
}
