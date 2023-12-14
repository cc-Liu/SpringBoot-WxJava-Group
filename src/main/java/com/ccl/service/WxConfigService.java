package com.ccl.service;

import com.ccl.entity.wexin.WxConfigParams;

import java.util.List;

public interface WxConfigService {

    WxConfigParams selectWxConfigValue();

    List<WxConfigParams> selectWxConfigValueList();

    WxConfigParams selectWxConfigBySiteId(Integer siteId);

    WxConfigParams selectWxConfigByAppId(String appId);
}
