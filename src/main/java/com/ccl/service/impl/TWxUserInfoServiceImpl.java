package com.ccl.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ccl.entity.wexin.*;
import com.ccl.mapper.TWxUserInfoMapper;
import com.ccl.service.TWxUserInfoService;
import com.ccl.util.CodeEnum;
import com.ccl.util.R;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 微信用户信息表 服务实现类
 *
 * @author liuc
 * @since 2021-09-03
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class TWxUserInfoServiceImpl implements TWxUserInfoService {

    @Resource
    private TWxUserInfoMapper wxUserInfoMapper;

    @Resource
    private WxMaService wxMaService;

    /**
     * @return com.furen.common.utils.R
     * @Author liuc
     * @Description 订阅事件
     * @Date 2021/9/6 11:27
     * @Param [paramsMap]
     **/
    @Override
    public R subscribeForText(WxMpUser userWxInfo) {
        int count = 0;
        Date nowDate = new Date();
        TWxUserInfo wxUserInfo = copyTWxUserInfoVoToWxMpUser(userWxInfo);
        try {
            TWxUserInfoVo wxUserInfoVo = wxUserInfoMapper.selectWxUserInfoByUnionId(userWxInfo.getUnionId());
            wxUserInfo.setIsSubscribe(CodeEnum.COMMON_ONE.getCode());
            if (wxUserInfoVo == null) {
                wxUserInfo.setCreateTime(nowDate);
                wxUserInfo.setUpdateTime(nowDate);
                //第一次关注公众号并且未使用过小程序
                count = wxUserInfoMapper.insert(wxUserInfo);
            } else {
                //关注过小程序或之前关注过公众号
                wxUserInfo.setWxUserId(wxUserInfoVo.getWxUserId());
                wxUserInfo.setUpdateTime(nowDate);
                count = wxUserInfoMapper.updateById(wxUserInfo);
            }
        } catch (Exception e) {
            log.error("用户关注公众号失败", e);
        }
        if (count > 0) {
            log.info("用户关注公众号成功");
            return R.ok().resultData(count).message("更新微信用户信息成功");
        } else {
            log.info("用户关注公众号失败");
            return R.error().resultData(count).message("更新微信用户信息失败");
        }
    }


    /**
     * @Author liuc
     * @Description 取消订阅
     * @Date 2021/9/6 16:50
     * @Param [paramsMap]
     * @return com.furen.common.utils.R
     **/
    @Override
    public R unSubscribeForText(String openId) {
        //用户公众号openId
        TWxUserInfoVo wxUserInfoVo = new TWxUserInfoVo();
        wxUserInfoVo.setPublicOpenid(openId);
        wxUserInfoVo.setIsSubscribe(CodeEnum.COMMON_ZERO.getCode());
        wxUserInfoVo.setUpdateTime(new Date());
        int count = wxUserInfoMapper.updWxUserInfoByPublicOpenid(wxUserInfoVo);
        if (count > 0) {
            log.info("用户取消关注公众号成功");
            return R.ok().resultData(count).message("更新微信用户信息成功");
        } else {
            log.info("用户取消关注公众号失败");
            return R.error().resultData(count).message("更新微信用户信息失败");
        }
    }


    /**
     * @Author liuc
     * @Description 新增微信用户信息并且返回主键
     * @Date 2021/9/7 15:35
     * @Param [wxUserInfoVo]
     * @return com.furen.common.utils.R
     **/
    @Override
    public R<Integer> insertWxUserInfo(TWxUserInfoVo wxUserInfoVo) {
        Date nowDate = new Date();
        wxUserInfoVo.setCreateTime(nowDate);
        wxUserInfoVo.setUpdateTime(nowDate);
        Integer count = wxUserInfoMapper.insertWxUserInfo(wxUserInfoVo);
        if(count > 0){
            return R.ok().resultData(wxUserInfoVo.getWxUserId());
        }else {
            return R.error().resultData(wxUserInfoVo.getWxUserId()).message("信息新增失败");
        }

    }

    /**
     * @Author liuc
     * @Description 绑定微信用户信息
     * @Date 2021/9/7 16:29
     * @Param [appletOpenid, unionId]
     * @return com.furen.common.utils.R<java.lang.Integer>
     **/
    @Override
    public R<Integer> bindingWxUserInfo(TWxUserInfoVo wxUserInfoVo) {
        TWxUserInfoVo wxUserInfoVo2 = wxUserInfoMapper.selectWxUserInfoByUnionId(wxUserInfoVo.getUnionId());
        Integer wxUserId;
        Date nowDate = new Date();
        if (wxUserInfoVo2 == null) {
            wxUserInfoVo.setIsSubscribe(CodeEnum.COMMON_ZERO.getCode());
            wxUserInfoVo.setCreateTime(nowDate);
            wxUserInfoVo.setUpdateTime(nowDate);
            wxUserInfoMapper.insertWxUserInfo(wxUserInfoVo);
            wxUserId = wxUserInfoVo.getWxUserId();
        } else {
            TWxUserInfo wxUserInfo = new TWxUserInfo();
            wxUserInfo.setWxUserId(wxUserInfoVo2.getWxUserId());
            wxUserInfo.setAppletOpenid(wxUserInfoVo.getAppletOpenid());
            wxUserInfo.setUpdateTime(nowDate);
            wxUserInfoMapper.updateById(wxUserInfo);
            wxUserId = wxUserInfoVo2.getWxUserId();
        }
        return R.ok().resultData(wxUserId);
    }


    /**
     * @Author liuc
     * @Description 校验用户是否关注了公众号
     * @Date 2021/9/8 9:06
     * @Param [memberNum]
     * @return com.furen.common.utils.R<java.lang.Boolean>
     **/
    @Override
    public R<TWxUserInfoVo> checkUserIsSubscribe(String mobile) {
        TWxUserInfoVo wxUserInfoVo = wxUserInfoMapper.selectWxUserInfoByMobile(mobile);
        if (wxUserInfoVo != null && StrUtil.isNotEmpty(wxUserInfoVo.getPublicOpenid()) && wxUserInfoVo.getIsSubscribe() == CodeEnum.COMMON_ONE.getCode()) {
            return R.ok().resultData(wxUserInfoVo);
        }
        return R.error();
    }

    /**
     * @Author liuc
     * @Description 根据memberNum查询用户微信信息
     * @Date 2021/9/8 11:22
     * @Param [memberNum]
     * @return com.furen.member.vo.wxUserInfo.TWxUserInfoVo
     **/
    @Override
    public TWxUserInfoVo selectWxUserInfoByMobile(String mobile) {
        TWxUserInfoVo wxUserInfoVo = wxUserInfoMapper.selectWxUserInfoByMobile(mobile);
        return wxUserInfoVo;
    }

    /**
     * @Author liuc
     * @Description 解密code或者获取用户openid和unionid
     * @Date 2023/9/6 17:26
     * @Param [wxMaSessionRequest]
     * @return com.furen.common.utils.R<com.furen.member.vo.wxUserInfo.WxMaSessionResult>
     **/
    @Override
    public R<WxMaSessionResult> selectWxMaSessionResult(WxMaRequest wxMaRequest) {
        WxMaJscode2SessionResult wxMaJscode2SessionResult = new WxMaJscode2SessionResult();
        try {
            wxMaJscode2SessionResult = wxMaService.switchoverTo(wxMaRequest.getAppId()).getUserService().getSessionInfo(wxMaRequest.getJsCode());
        } catch (WxErrorException e) {
            log.info("\n解密code失败:{}", e.getMessage());
            e.printStackTrace();
        }
        log.info("\n获取用户openid和unionid:{}", JSONUtil.parse(wxMaJscode2SessionResult));
        WxMaSessionResult wxMaSessionResult = new WxMaSessionResult();
        BeanUtil.copyProperties(wxMaJscode2SessionResult, wxMaSessionResult);
        return R.ok().resultData(wxMaSessionResult);
    }

    /**
     * @Author liuc
     * @Description 解密用户手机号
     * @Date 2023/9/7 10:12
     * @Param [wxMaRequest]
     * @return com.furen.common.utils.R<cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo>
     **/
    @Override
    public R<WxMaPhoneNumberResult> decryptWxMaPhoneNumberInfo(WxMaRequest wxMaRequest) {
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.switchoverTo(wxMaRequest.getAppId()).getUserService().getPhoneNoInfo(wxMaRequest.getSessionKey(), wxMaRequest.getEncryptedData(), wxMaRequest.getIvStr());
        WxMaPhoneNumberResult wxMaPhoneNumberResult = new WxMaPhoneNumberResult();
        BeanUtil.copyProperties(phoneNoInfo, wxMaPhoneNumberResult);
        return R.ok().resultData(wxMaPhoneNumberResult);
    }


    /**
     * @Author liuc
     * @Description 转换
     * @Date 2021/9/23 15:40
     * @Param [user]
     * @return com.furen.member.entity.TWxUserInfo
     **/
    private TWxUserInfo copyTWxUserInfoVoToWxMpUser(WxMpUser user){
        TWxUserInfo wxUserInfo = new TWxUserInfo();
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
        return wxUserInfo;
    }
}
