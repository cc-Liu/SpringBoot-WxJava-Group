package com.ccl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ccl.constant.WechatInterface;
import com.ccl.entity.vo.BaseSysCodeVo;
import com.ccl.entity.wexin.WxConfigParams;
import com.ccl.redis.RedisClient;
import com.ccl.service.WxConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: liuc
 * @Date: 2023/12/7 17:11
 * @Description:
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class WxConfigServiceImpl implements WxConfigService {

    @Resource
    private RedisClient redisClient;

    /**
     * @Author liuc
     * @Description 查询微信配置信息，如果没有则查询出来缓存到redis中
     * @Date 2022/3/24 16:12
     * @Param []
     * @return com.furen.member.weixin.WxConfigParams
     **/
    public WxConfigParams selectWxConfigValue() {
        Map<Object, Object> mapData = redisClient.getMapData(WechatInterface.SYS_CONFIG, WechatInterface.WX_CONFIG);
        if(mapData == null || mapData.size() <= 0){
            List<BaseSysCodeVo> wxDataList = new ArrayList<>();//从redis中取出微信配置
            Map<String, Object> map = Optional.ofNullable(wxDataList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(BaseSysCodeVo::getCode, BaseSysCodeVo::getName));
            redisClient.addMap("sys_config",WechatInterface.WX_CONFIG, map);
            mapData = redisClient.getMapData(WechatInterface.SYS_CONFIG, WechatInterface.WX_CONFIG);
        }
        WxConfigParams wxConfigParams = BeanUtil.fillBeanWithMap(mapData, new WxConfigParams(), true, false);
        return wxConfigParams;
    }

    /**
     * @Author liuc
     * @Description 查询微信配置信息集合，如果没有则查询出来缓存到redis中
     * @Date 2023/9/18 10:31
     * @Param []
     * @return java.util.List<com.furen.member.weixin.WxConfigParams>
     **/
    @Override
    public List<WxConfigParams> selectWxConfigValueList() {
        Map<Object, Object> wxSiteConfigMapData = redisClient.getMapData(WechatInterface.SYS_CONFIG, "wxSiteConfig");
        List<Object> wxSiteConfigList = wxSiteConfigMapData.values().stream().collect(Collectors.toList());

        List<WxConfigParams> wxConfigParamsList = new ArrayList<>();

        wxSiteConfigList.forEach(wscl -> {

            String wxRedisKey = WechatInterface.WX_CONFIG + "_" + wscl;

            Map<Object, Object> mapData = redisClient.getMapData(WechatInterface.SYS_CONFIG, wxRedisKey);
            if (mapData == null || mapData.size() <= 0) {
                List<BaseSysCodeVo> wxDataList = new ArrayList<>();//从redis中取出微信配置
                if(CollectionUtils.isEmpty(wxDataList)){
                    return;
                }
                Map<String, Object> map = Optional.ofNullable(wxDataList).orElse(new ArrayList<>()).stream().collect(Collectors.toMap(BaseSysCodeVo::getCode, BaseSysCodeVo::getName));
                redisClient.addMap("sys_config",wxRedisKey, map);
                mapData = redisClient.getMapData(WechatInterface.SYS_CONFIG, wxRedisKey);
            }
            WxConfigParams wxConfigParams = BeanUtil.fillBeanWithMap(mapData, new WxConfigParams(), true, false);
            wxConfigParamsList.add(wxConfigParams);
        });
        return wxConfigParamsList;
    }

    /**
     * @Author liuc
     * @Description 根据站点id去查询微信配置
     * @Date 2023/9/18 19:18
     * @Param [siteId]
     * @return java.lang.String
     **/
    @Override
    public WxConfigParams selectWxConfigBySiteId(Integer siteId) {
        List<WxConfigParams> wxConfigParamsList = selectWxConfigValueList();
        if (CollectionUtils.isEmpty(wxConfigParamsList)) {
            return null;
        }
        for (WxConfigParams wxConfigParams : wxConfigParamsList) {
            List<Integer> siteIdList = Arrays.stream(wxConfigParams.getSiteId().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            if (siteIdList.contains(siteId)) {
                return wxConfigParams;
            }
        }
        return null;
    }

    /**
     * @Author liuc
     * @Description 根据appId去查询微信配置
     * @Date 2023/9/19 17:21
     * @Param [appId]
     * @return com.furen.member.weixin.WxConfigParams
     **/
    @Override
    public WxConfigParams selectWxConfigByAppId(String appId) {
        List<WxConfigParams> wxConfigParamsList = selectWxConfigValueList();
        return wxConfigParamsList.stream().filter(wcp -> wcp.getAppId().equals(appId)).findFirst().get();
    }
}
