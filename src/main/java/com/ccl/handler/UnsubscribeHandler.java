package com.ccl.handler;

import com.ccl.service.TWxUserInfoService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
public class UnsubscribeHandler extends AbstractHandler {

    @Resource
    private TWxUserInfoService wxUserInfoService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String openId = wxMessage.getFromUser();
        log.info("取消关注用户 OPENID: " + openId);
        //更新本地数据库为取消关注状态
        wxUserInfoService.unSubscribeForText(openId);
        return null;
    }

}
