package com.ccl.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ccl.constant.WeChatTemplateTypeEnum;
import com.ccl.entity.wexin.*;
import com.ccl.enums.PayTypeEnum;
import com.ccl.redis.RedisClient;
import com.ccl.service.TWxUserInfoService;
import com.ccl.service.WeChatMsgPushService;
import com.ccl.service.WxConfigService;
import com.ccl.util.R;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class WeChatMsgPushServiceImpl implements WeChatMsgPushService {

    @Resource
    private RedisClient redisClient;

    @Resource
    private TWxUserInfoService wxUserInfoService;

    @Resource
    private WxMpService wxService;

    @Resource
    private WxMaService wxMaService;

    @Resource
    private WxConfigService wxConfigService;

    /**
     * @return com.furen.common.utils.R<java.lang.String>
     * @Author liuc
     * @Description 获取小程序全局唯一后台接口调用凭据
     * @Date 2021/9/7 10:10
     * @Param []
     **/
    @Override
    public R<String> getAccessToken() {
        log.info("获取全局唯一后台接口调用凭据accessToken");
        String accessToken = "";
        try {
            accessToken = wxService.getAccessToken();
        } catch (WxErrorException e) {
            log.error("\n获取accessToken失败：{}", e);
        }
        return R.ok().resultData(accessToken);
    }

    /**
     * @return com.furen.common.utils.R<java.util.HashMap < java.lang.String, java.lang.Object>>
     * @Author liuc
     * @Description 根据用户公众号Openid获取信息 postForEntity
     * @Date 2021/9/7 10:18 TWxUserInfo
     * @Param [openId]
     **/
    @Override
    public R<TWxUserInfo> getUserInfoByPublicOpenid(String openId) {
        WxMpUser user;
        TWxUserInfo wxUserInfo = new TWxUserInfo();
        try {
            user = wxService.getUserService().userInfo(openId);
            wxUserInfo.setIsSubscribe(user.getSubscribe().equals(true) ? 1 : 0);
            wxUserInfo.setPublicOpenid(user.getOpenId());
            wxUserInfo.setNickname(user.getNickname());
            wxUserInfo.setSex(user.getSex());
            wxUserInfo.setLanguage(user.getLanguage());
            wxUserInfo.setCity(user.getCity());
            wxUserInfo.setProvince(user.getProvince());
            wxUserInfo.setCountry(user.getCountry());
            wxUserInfo.setHeadimgurl(user.getHeadImgUrl());
            wxUserInfo.setSubscribeTime(DateUtil.date(Long.valueOf(user.getSubscribeTime() + "000")));
            wxUserInfo.setUnionId(user.getUnionId());
            wxUserInfo.setRemark(user.getRemark());
            wxUserInfo.setGroupId(user.getGroupId());
            String tagidStr = Convert.toStr(user.getPrivileges());
            wxUserInfo.setTagidList(tagidStr);
            wxUserInfo.setSubscribeScene(user.getSubscribeScene());
            wxUserInfo.setQrScene(user.getQrScene());
            wxUserInfo.setQrSceneStr(user.getQrSceneStr());
        } catch (WxErrorException e) {
            log.error("\n获取用户信息错误：{}", e);
        }
        return R.ok().resultData(wxUserInfo);
    }

    /**
     * @return com.furen.common.utils.R
     * @Author liuc
     * @Description 公众号消息推送
     * @Date 2021/9/7 10:19
     * @Param [templateParams]
     **/
    @Async
    @Override
    public R wxPublicMsgPush(TemplateParams templateParams, WxConfigParams wxConfigParams) {
        log.info("\n公众号消息推送参数：{},微信配置", JSONUtil.parse(templateParams), JSONUtil.parse(wxConfigParams));
        R<TWxUserInfoVo> tWxUserInfoVoR = wxUserInfoService.checkUserIsSubscribe(templateParams.getMobile());
        if (!tWxUserInfoVoR.getSuccess()) {
            log.error("该用户没有关注公众号:{}", JSONUtil.parse(templateParams));
            return R.error().resultData("该用户没有关注公众号");
        }
        TWxUserInfoVo wxUserInfoVo = tWxUserInfoVoR.getResultData();
        List<WxMpTemplateData> data = new ArrayList<>();
        //模板内容
        List<KeyWordParams> valueList = templateParams.getValueList();
        //组装推送信息
        for (KeyWordParams keyWordParams : valueList) {
            data.add(new WxMpTemplateData(keyWordParams.getKeyWordType(), keyWordParams.getValue()));
        }
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(wxUserInfoVo.getPublicOpenid())
                .templateId(templateParams.getTemplateId())
                .miniProgram(new WxMpTemplateMessage.MiniProgram(wxConfigParams.getAppletAppid(), wxConfigParams.getAppletUrl(), false))
                .build();
        templateMessage.setData(data);

        String msgId;
        try {
            msgId = this.wxService.switchoverTo(wxConfigParams.getAppId()).getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            log.error("\n模板发送失败：{}", e);
            return R.error().resultData("模板发送失败");
        }
        return R.ok().resultData(msgId);
    }

    /**
     * @Author liuc
     * @Description 微信公众号消息推送
     * @Date 2022/3/31 9:18
     * @Param [weChatMsgPush]
     * @return com.furen.common.utils.R
     **/
    @Override
    public R weChatMpeMsgPush(WeChatMsgPush weChatMsgPush) {
        //微信配置信息
        WxConfigParams wxConfigParams = wxConfigService.selectWxConfigBySiteId(weChatMsgPush.getSiteName());
        if (wxConfigParams == null) {
            log.info("\n当前站点无微信配置信息");
            return R.error().message("当前站点无微信配置信息");
        }

        String templateType = WeChatTemplateTypeEnum.getValue(weChatMsgPush.getTemplateType());
        String payTypeStr = PayTypeEnum.getValue(weChatMsgPush.getPayType());
        //付款明细 支付方式 + 金额 + 赠送余额(0不展示)
        String keyWordParams2 = payTypeStr + "支付" + weChatMsgPush.getPayPrice() + "元";
        if (StrUtil.isNotBlank(weChatMsgPush.getGiftBalance()) && !weChatMsgPush.getGiftBalance().equals("0")) {
            keyWordParams2 = keyWordParams2 + ",赠送" + weChatMsgPush.getGiftBalance() + "元";
        }

        //公众号推送消息 thing对应公众号内的模板内容
        TemplateParams templateParams = new TemplateParams();
        List<KeyWordParams> valueList = new ArrayList<>();
        valueList.add(new KeyWordParams(String.valueOf(weChatMsgPush.getSiteName()), "thing2"));
        valueList.add(new KeyWordParams(keyWordParams2, "thing4"));
        valueList.add(new KeyWordParams(templateType, "thing3"));
        //充值时必定展示余额
        if (weChatMsgPush.getTemplateType().equals(WeChatTemplateTypeEnum.RECHARGE.getCode())) {
            valueList.add(new KeyWordParams(weChatMsgPush.getBalance(), "amount5"));
            templateParams.setTemplateId(wxConfigParams.getBalanceTemplateId());
        } else {
            //消费不等于null 并且支付方式为实体卡或者会员卡
            if (StringUtils.isNotBlank(weChatMsgPush.getBalance()) && (weChatMsgPush.getPayType().equals(PayTypeEnum.MEMBERPAY.getCode()) || weChatMsgPush.getPayType().equals(PayTypeEnum.MONEPAY.getCode()))) {
                valueList.add(new KeyWordParams(weChatMsgPush.getBalance(), "amount5"));
                templateParams.setTemplateId(wxConfigParams.getBalanceTemplateId());
            } else {
                templateParams.setTemplateId(wxConfigParams.getNoBalanceTemplateId());
            }
        }
        valueList.add(new KeyWordParams(weChatMsgPush.getPayTime(), "time10"));
        templateParams.setMobile(weChatMsgPush.getMobile());
        templateParams.setValueList(valueList);
        return wxPublicMsgPush(templateParams, wxConfigParams);
    }

    /**
     * @Author liuc
     * @Description 小程序服务订阅通知
     * @Date 2023/12/12 14:54
     * @Param [weChatMaMessageRequest]
     * @return com.furen.common.utils.R
     **/
    @Override
    public R weChatMaMessagePush(WeChatMaMessageRequest weChatMaMessageRequest) {
        log.info("服务订阅通知参数:{}", JSONUtil.parse(weChatMaMessageRequest));
        try {
            wxMaService.getMsgService().sendSubscribeMsg(WxMaSubscribeMessage.builder()
                    .templateId(weChatMaMessageRequest.getTemplateId())
                    .data(Lists.newArrayList(
                            new WxMaSubscribeMessage.MsgData("thing5", weChatMaMessageRequest.getKeyWordOne()),
                            new WxMaSubscribeMessage.MsgData("character_string6", weChatMaMessageRequest.getKeyWordTwo()),
                            new WxMaSubscribeMessage.MsgData("thing13", weChatMaMessageRequest.getKeyWordThree()),
                            new WxMaSubscribeMessage.MsgData("time2", weChatMaMessageRequest.getKeyWordFour()),
                            new WxMaSubscribeMessage.MsgData("time3", weChatMaMessageRequest.getKeyWordFive()))
                    )
                    .toUser(weChatMaMessageRequest.getOpenid())
                    .build());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    @Override
    public R wxPublicMsgPushTest(WeChatMsgPush weChatMsgPush) {
        String siteName = String.valueOf(weChatMsgPush.getSiteName());
        String templateType = WeChatTemplateTypeEnum.getValue(weChatMsgPush.getTemplateType());
        TemplateParams templateParams = new TemplateParams();
        List<KeyWordParams> valueList2 = new ArrayList<>();
        valueList2.add(new KeyWordParams(siteName));
        valueList2.add(new KeyWordParams(PayTypeEnum.getValue(weChatMsgPush.getPayType())));
        valueList2.add(new KeyWordParams(weChatMsgPush.getPayPrice()));
        valueList2.add(new KeyWordParams(weChatMsgPush.getPayTime()));
        templateParams.setTemplateId("lLxcigAGjHDTyb10QEROrYdO6B3KSrUjAKG0vLezqBk");
        templateParams.setMobile(weChatMsgPush.getMobile());
        templateParams.setValueList(valueList2);


        R<TWxUserInfoVo> tWxUserInfoVoR = wxUserInfoService.checkUserIsSubscribe(templateParams.getMobile());
        if (tWxUserInfoVoR.getSuccess()) {
            TWxUserInfoVo wxUserInfoVo = tWxUserInfoVoR.getResultData();
            List<WxMpTemplateData> data = new ArrayList<>();
            //模板内容
            List<KeyWordParams> valueList = templateParams.getValueList();

            int i = 1;
            //组装推送信息
            for (KeyWordParams keyWordParams : valueList) {
                data.add(new WxMpTemplateData(keyWordParams.getKeyWordType() + i, keyWordParams.getValue()));
                i++;
            }
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(wxUserInfoVo.getPublicOpenid())
                    .templateId(templateParams.getTemplateId())
                    .miniProgram(new WxMpTemplateMessage.MiniProgram("wxf4d3095010e4413c", "", false))
                    .build();
            templateMessage.setData(data);

            String msgId = null;
            try {
                msgId = this.wxService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            } catch (WxErrorException e) {
                log.error("\n模板发送失败：{}", e);
                return R.error().resultData("模板发送失败");
            }
            log.info("\n公众号通知：{}", templateParams.getMobile());
            return R.ok().resultData(msgId);
        }
        return R.error().resultData("该用户没有关注公众号");
    }

}
