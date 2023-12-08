package com.ccl.handler;

import cn.hutool.json.JSONUtil;
import com.ccl.builder.TextBuilder;
import com.ccl.entity.wexin.WxConfigParams;
import com.ccl.service.TWxUserInfoService;
import com.ccl.service.WxConfigService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author liuc
 * @Description 关注事件
 * @Date 2021/9/23 15:24
 **/
@Slf4j
@Component
public class SubscribeHandler extends AbstractHandler {

    @Resource
    private TWxUserInfoService wxUserInfoService;

    @Resource
    private WxConfigService wxConfigService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = wxMpService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                log.info("新用户关注:{}", JSONUtil.parse(userWxInfo));
                //添加关注用户到本地数据库
                wxUserInfoService.subscribeForText(userWxInfo);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.info("\n关注失败,错误信息：{},错误码：{}",e.getMessage(),e.getError());
            if (e.getError().getErrorCode() == 48001) {
                log.error("该公众号没有获取用户信息权限！");
            }
        }


        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = this.handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }


        WxMpConfigStorage wxMpConfigStorage = wxMpService.getWxMpConfigStorage();
        WxConfigParams wxConfigParams = wxConfigService.selectWxConfigByAppId(wxMpConfigStorage.getAppId());
        try {
            return new TextBuilder().build(wxConfigParams.getWxFollowReply(), wxMessage, wxMpService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
        throws Exception {
        log.info("扫码关注");
        //TODO
        return null;
    }

}
