package com.ccl.entity.wexin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: liuc
 * @Date: 2023/9/12 14:35
 * @Description:
 */
@Data
@ApiModel(value="weChatMenuRequest对象", description="创建微信菜单参数")
public class WeChatMenuRequest {

    @ApiModelProperty(value = "公众号标识openid")
    private String appletOpenid;

}
