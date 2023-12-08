package com.ccl.service;


import com.ccl.entity.wexin.TWxUserInfo;
import com.ccl.entity.wexin.TemplateParams;
import com.ccl.entity.wexin.WeChatMsgPush;
import com.ccl.entity.wexin.WxConfigParams;
import com.ccl.util.R;

public interface WeChatMsgPushService {

    //获取小程序全局唯一后台接口调用凭据
    R<String> getAccessToken();

    //根据用户公众号Openid获取信息
    R<TWxUserInfo> getUserInfoByPublicOpenid(String openId);

    //公众号消息推送
    R wxPublicMsgPush(TemplateParams templateParams, WxConfigParams wxConfigParams);

    //公众号消息推送(参数组装)
    R weChatMpeMsgPush(WeChatMsgPush weChatMsgPush);

    WxConfigParams selectWxConfigValue();

    R wxPublicMsgPushTest(WeChatMsgPush weChatMsgPush);

}
