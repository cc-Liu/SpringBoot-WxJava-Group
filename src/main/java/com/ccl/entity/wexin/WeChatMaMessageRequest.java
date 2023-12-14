package com.ccl.entity.wexin;

import lombok.Data;

/**
 * @Auther: liuc
 * @Date: 2023/12/12 14:47
 * @Description: 小程序订阅消息
 */
@Data
public class WeChatMaMessageRequest {

    //用户openid
    private String openid;

    //订单号号
    private String orderNum;

    //模板id
    private String templateId;

    //参数1
    private String keyWordOne;

    //参数2
    private String keyWordTwo;

    //参数3
    private String keyWordThree;

    //参数4
    private String keyWordFour;

    //参数5
    private String keyWordFive;

}
