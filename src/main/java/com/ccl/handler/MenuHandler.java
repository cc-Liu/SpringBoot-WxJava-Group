package com.ccl.handler;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType;

/**
 * @Author liuc
 * @Description 自定义菜单事件
 * @Date 2021/9/23 16:06
 **/
@Slf4j
@Component
public class MenuHandler extends AbstractHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        String msg = String.format("type:%s, event:%s, key:%s",
            wxMessage.getMsgType(), wxMessage.getEvent(),
            wxMessage.getEventKey());
        log.info("\n自定义菜单事件msgType:{},event:{},eventKey:{}", wxMessage.getMsgType(), wxMessage.getEvent(), wxMessage.getEventKey());
        switch (wxMessage.getEvent()) {
            case EventType.VIEW:
                log.info("\n用户点击菜单VIEW：{}", JSONUtil.parse(context));
                break;
            case EventType.CLICK:
                log.info("\n用户点击菜单CLICK：{}", JSONUtil.parse(context));
                break;
            default:
                break;
        }
        return WxMpXmlOutMessage.TEXT().content(msg)
            .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
            .build();
    }

}
