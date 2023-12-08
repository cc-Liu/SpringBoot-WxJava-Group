package com.ccl.service;


import com.ccl.entity.wexin.TWxUserInfoVo;
import com.ccl.entity.wexin.WxMaPhoneNumberResult;
import com.ccl.entity.wexin.WxMaRequest;
import com.ccl.entity.wexin.WxMaSessionResult;
import com.ccl.util.R;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 微信用户信息表 服务类
 *
 * @author liuc
 * @since 2021-09-03
 */
public interface TWxUserInfoService{

    R subscribeForText(WxMpUser userWxInfo);

    R unSubscribeForText(String openId);

    R<Integer> insertWxUserInfo(TWxUserInfoVo wxUserInfoVo);

    R<Integer> bindingWxUserInfo(TWxUserInfoVo wxUserInfoVo);

    R<TWxUserInfoVo> checkUserIsSubscribe(String mobile);

    TWxUserInfoVo selectWxUserInfoByMobile(String mobile);

    R<WxMaSessionResult> selectWxMaSessionResult(WxMaRequest wxMaRequest);

    R<WxMaPhoneNumberResult> decryptWxMaPhoneNumberInfo(WxMaRequest wxMaRequest);
}
