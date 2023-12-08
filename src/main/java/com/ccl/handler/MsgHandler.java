package com.ccl.handler;

import com.ccl.builder.TextBuilder;
import com.ccl.entity.wexin.WxConfigParams;
import com.ccl.service.WxConfigService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
public class MsgHandler extends AbstractHandler {

    @Resource
    private WxConfigService wxConfigService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {

        if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        /*try {
            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服")
                && weixinService.getKefuService().kfOnlineList()
                .getKfOnlineList().size() > 0) {
                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE()
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        }*/

        //TODO 组装回复消息
        //String content = "收到信息内容：" + JsonUtils.toJsonWx(wxMessage);
        //String content = wxMessage.getContent();
        //return new TextBuilder().build(content, wxMessage, weixinService);

        try {

            //上传素材
           /* BufferedInputStream inputStream = FileUtil.getInputStream("D:\\IDEAProject\\simp_member\\simp_member_app\\src\\main\\resources\\dog.jpeg");
            WxMediaUploadResult wxMediaUploadResult = weixinService.getMaterialService()
                    .mediaUpload(WxConsts.MediaFileType.IMAGE, "jpeg", inputStream);*/

           //回复图片
            /*return WxMpXmlOutMessage
                    .IMAGE()
                    .mediaId("Mhxts83LzQVoTDjCgTQc_TQlbzrdHXc4Dxiz1ywsHddr5142DgWTwo2gzDAQGi81")
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();*/

            WxMpConfigStorage wxMpConfigStorage = wxMpService.getWxMpConfigStorage();
            WxConfigParams wxConfigParams = wxConfigService.selectWxConfigByAppId(wxMpConfigStorage.getAppId());
            return new TextBuilder().build(wxConfigParams.getAutoReply(), wxMessage, wxMpService);
        } catch (Exception e) {
            logger.error("返回图片信息失败：{}",e);
            e.printStackTrace();
        }
        return null;
    }

}
