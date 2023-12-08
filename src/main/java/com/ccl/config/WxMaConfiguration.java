package com.ccl.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.ccl.entity.wexin.WxConfigParams;
import com.ccl.service.WxConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: liuc
 * @Date: 2023/9/7 08:58
 * @Description: 微信小程序配置
 */
@Slf4j
@AllArgsConstructor
@Configuration
public class WxMaConfiguration {

    @Resource
    private WxConfigService wxConfigService;

    @Bean
    public WxMaService wxMaService() {
        /*WxConfigParams wxConfigParams = weChatMsgPushService.selectWxConfigValue();
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxConfigParams.getAppletAppid());
        config.setSecret(wxConfigParams.getAppletSecret());
        config.setToken(wxConfigParams.getToken());
        config.setAesKey(wxConfigParams.getAesKey());
        wxMaService.setWxMaConfig(config);
        return wxMaService;*/
        List<WxConfigParams> wxConfigParamsList = wxConfigService.selectWxConfigValueList();
        WxMaService maService = new WxMaServiceImpl();
        maService.setMultiConfigs(
                wxConfigParamsList.stream()
                        .map(a -> {
                            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                            config.setAppid(a.getAppletAppid());
                            config.setSecret(a.getAppletSecret());
                            config.setToken(a.getToken());
                            config.setAesKey(a.getAesKey());
                            return config;
                        }).collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o)));
        return maService;
    }
}
