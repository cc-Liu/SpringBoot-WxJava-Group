package com.ccl.controller;


import com.ccl.entity.wexin.TWxUserInfoVo;
import com.ccl.entity.wexin.WxMaPhoneNumberResult;
import com.ccl.entity.wexin.WxMaRequest;
import com.ccl.entity.wexin.WxMaSessionResult;
import com.ccl.service.TWxUserInfoService;
import com.ccl.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 微信用户信息表 前端控制器
 *
 * @author liuc
 * @since 2021-09-03
 */
@Api(value = "微信用户信息控制层", tags = {"微信用户信息操作接口"})
@Slf4j
@RestController
@RequestMapping("/wxUserInfo")
public class TWxUserInfoController {

    @Resource
    private TWxUserInfoService wxUserInfoService;

    /**
     * @Author liuc
     * @Description 取消订阅事件
     * @Date 2021/9/7 14:28
     * @Param [paramsMap]
     * @return com.furen.common.utils.R
     **/
    @ApiOperation(value = "取消订阅事件")
    @GetMapping("/unSubscribeForText")
    public R unSubscribeForText(@RequestParam String openId) {
        return wxUserInfoService.unSubscribeForText(openId);
    }

    /**
     * @Author liuc
     * @Description 新增微信用户信息并且返回主键
     * @Date 2021/9/8 9:16
     * @Param [wxUserInfoVo]
     * @return com.furen.common.utils.R
     **/
    @ApiOperation(value = "新增微信用户信息并且返回主键")
    @PostMapping("/insertWxUserInfo")
    public R insertWxUserInfo(@RequestBody TWxUserInfoVo wxUserInfoVo) {
        return wxUserInfoService.insertWxUserInfo(wxUserInfoVo);
    }

    /**
     * @Author liuc
     * @Description 校验用户是否关注了公众号
     * @Date 2021/9/8 9:17
     * @Param [memberNum]
     * @return com.furen.common.utils.R<java.lang.Boolean>
     **/
    @ApiOperation(value = "校验用户是否关注了公众号")
    @GetMapping("/checkUserIsSubscribe")
    public R<TWxUserInfoVo> checkUserIsSubscribe(@RequestParam String mobile) {
        return wxUserInfoService.checkUserIsSubscribe(mobile);
    }

    @ApiOperation(value = "解密code或者获取用户openid和unionid")
    @PostMapping("/selectWxMaSessionResult")
    public R<WxMaSessionResult> selectWxMaSessionResult(@RequestBody WxMaRequest wxMaRequest) {
        return wxUserInfoService.selectWxMaSessionResult(wxMaRequest);
    }

    @ApiOperation(value = "解密用户手机号")
    @PostMapping("/decryptWxMaPhoneNumberInfo")
    public R<WxMaPhoneNumberResult> decryptWxMaPhoneNumberInfo(@RequestBody WxMaRequest wxMaRequest) {
        return wxUserInfoService.decryptWxMaPhoneNumberInfo(wxMaRequest);
    }

}

