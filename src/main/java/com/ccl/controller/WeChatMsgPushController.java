package com.ccl.controller;

import com.ccl.entity.wexin.TWxUserInfo;
import com.ccl.entity.wexin.WeChatMaMessageRequest;
import com.ccl.entity.wexin.WeChatMsgPush;
import com.ccl.entity.wexin.WxConfigParams;
import com.ccl.service.WeChatMsgPushService;
import com.ccl.service.WxConfigService;
import com.ccl.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 微信小程序订阅消息推送接口
 * @Author admin
 * @Date 2021/6/1 10:46
 * @Version 1.0
 */
@Api(value = "微信小程序接口", tags = {"订阅消息推送接口"})
@Slf4j
@RestController
@RequestMapping("/wxMsgPush")
public class WeChatMsgPushController {

    @Resource
    private WeChatMsgPushService weChatMsgPushService;

    @Resource
    private WxConfigService wxConfigService;

    @ApiOperation("获取小程序全局唯一后台接口调用凭据")
    @PostMapping("/getAccessToken")
    public R getAccessToken() {
        return weChatMsgPushService.getAccessToken();
    }

    @ApiOperation("获取用户基本信息(UnionID机制)")
    @PostMapping("/getUserInfoByPublicOpenid")
    public R<TWxUserInfo> getUserInfoByPublicOpenid(@RequestParam String openId) {
        R<TWxUserInfo> wxUserInfo = weChatMsgPushService.getUserInfoByPublicOpenid(openId);
        return R.ok().resultData(wxUserInfo);
    }

    @ApiOperation("查询微信配置并且缓存到redis中")
    @PostMapping("/selectWxConfigValue")
    public R selectWxConfigValue() {
        WxConfigParams wxConfigParams = wxConfigService.selectWxConfigValue();
        return R.ok().resultData(wxConfigParams);
    }

    @ApiOperation("查询多个微信配置并且缓存到redis中")
    @GetMapping("/selectWxConfigValueList")
    public R selectWxConfigValueList() {
        List<WxConfigParams> wxConfigParamsList = wxConfigService.selectWxConfigValueList();
        return R.ok().resultData(wxConfigParamsList);
    }

    @ApiOperation("微信公众号消息推送")
    @PostMapping("/weChatMpeMsgPush")
    public R weChatMpeMsgPush(@RequestBody WeChatMsgPush weChatMsgPush) {
        return weChatMsgPushService.weChatMpeMsgPush(weChatMsgPush);
    }

    /**
     * @Author liuc
     * @Description 小程序服务订阅通知
     * @Date 2023/12/12 14:55
     * @Param [weChatMaMessageRequest]
     * @return com.furen.common.utils.R
     **/
    @PostMapping("/weChatMaMessagePush")
    public R weChatMaMessagePush(@RequestBody WeChatMaMessageRequest weChatMaMessageRequest) {
        return weChatMsgPushService.weChatMaMessagePush(weChatMaMessageRequest);
    }


    @ApiOperation("微信公众号消息推送测试方法")
    @PostMapping("/weChatMpeMsgPushTest")
    public R<TWxUserInfo> weChatMpeMsgPushTest(@RequestBody WeChatMsgPush weChatMsgPush) {
        R r = weChatMsgPushService.wxPublicMsgPushTest(weChatMsgPush);
        return R.ok().resultData(r);
    }

}