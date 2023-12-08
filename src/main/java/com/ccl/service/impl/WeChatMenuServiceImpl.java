package com.ccl.service.impl;

import com.ccl.constant.WechatInterface;
import com.ccl.entity.vo.WeChatMenuVo;
import com.ccl.entity.wexin.WeChatMenuRequest;
import com.ccl.entity.wexin.WxConfigParams;
import com.ccl.mapper.WeChatMenuMapper;
import com.ccl.service.WeChatMenuService;
import com.ccl.service.WxConfigService;
import com.ccl.util.R;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信菜单管理 服务实现类
 *
 * @author liuc
 * @since 2021-09-17
 */
@Slf4j
@Service
public class WeChatMenuServiceImpl implements WeChatMenuService {

    @Resource
    private WeChatMenuMapper weChatMenuMapper;

    @Resource
    private WxMpService wxService;

    @Resource
    private WxConfigService wxConfigService;

    /**
     * @return com.furen.common.utils.R
     * @Author liuc
     * @Description 创建微信菜单
     * @Date 2021/9/17 10:04
     * @Param []
     **/
    @Override
    public R createWeChatMenu(WeChatMenuRequest weChatMenuRequest) {
        List<WeChatMenuVo> firstWeChatMenu = weChatMenuMapper.selectFirstWeChatMenu();
        List<WeChatMenuVo> secondWeChatMenu = weChatMenuMapper.selectSecondWeChatMenu();
        //将子菜单关联到主菜单中
        for (WeChatMenuVo firstWeChat : firstWeChatMenu) {
            for (WeChatMenuVo secondWeChat : secondWeChatMenu) {
                if (secondWeChat.getMenuLevel() == firstWeChat.getMenuId()) {
                    firstWeChat.setWeChatMenuVoList(secondWeChatMenu);
                }
            }
        }

        WxMenu menu = new WxMenu();
        List<WxMenuButton> mainButtonsList = new ArrayList<>(); //主菜单
        List<WxMenuButton> subButtonsList = new ArrayList<>();  //子菜单

        WxMenuButton mainButtons = new WxMenuButton();
        WxMenuButton subButtons = new WxMenuButton();

        for (WeChatMenuVo firstWeChat : firstWeChatMenu) {
            //主菜单
            mainButtons = matchMenuType(firstWeChat,weChatMenuRequest);

            if (!CollectionUtils.isEmpty(firstWeChat.getWeChatMenuVoList())) {
                //子菜单
                for (WeChatMenuVo weChatMenuVo : firstWeChat.getWeChatMenuVoList()) {
                    subButtons = matchMenuType(weChatMenuVo,weChatMenuRequest);
                    subButtonsList.add(subButtons);
                    mainButtons.setSubButtons(subButtonsList);
                }
            }
            mainButtonsList.add(mainButtons);
        }
        menu.setButtons(mainButtonsList);
        log.info("\n数据：{}", menu);
        String result = null;
        try {
            result = wxService.switchoverTo(weChatMenuRequest.getAppletOpenid()).getMenuService().menuCreate(menu);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("\n自定义菜单创建失败：{}", e);
            return R.error().resultData(e);
        }
        return R.ok().resultData(result);
    }

    /**
     * @Author liuc
     * @Description 自定义菜单删除接口
     * @Date 2021/9/24 16:52
     **/
    @Override
    public R delWeChatMenu(WeChatMenuRequest weChatMenuRequest) {
        try {
            wxService.switchoverTo(weChatMenuRequest.getAppletOpenid()).getMenuService().menuDelete();
        } catch (WxErrorException e) {
            log.error("\n自定义菜单删除失败：{}", e);
        }
        return R.ok().resultData("自定义菜单删除成功");
    }

    /**
     * @Author liuc
     * @Description 菜单类型匹配
     * @Date 2021/9/27 16:48
     * @Param [menuType]
     * @return java.lang.String
     **/
    private WxMenuButton matchMenuType(WeChatMenuVo weChatMenuVo, WeChatMenuRequest weChatMenuRequest) {
        WxConfigParams wxConfigParams = wxConfigService.selectWxConfigByAppId(weChatMenuRequest.getAppletOpenid());
        WxMenuButton wxMenuButton = new WxMenuButton();
        //菜单的响应动作类型匹配
        String menuTypeStr = WxConsts.MenuButtonType.VIEW;
        switch (weChatMenuVo.getMenuType()) {
            case 1://1 url类型
                menuTypeStr = WxConsts.MenuButtonType.VIEW;
                break;
            case 2://2 回复图片类型
                menuTypeStr = WxConsts.MenuButtonType.CLICK;
                break;
            case 3://3 小程序
                menuTypeStr = WxConsts.MenuButtonType.MINIPROGRAM;
                break;
            default:
                break;
        }
        if (weChatMenuVo.getMenuType() != 0) {
            switch (weChatMenuVo.getMenuType()) {
                case 1://1 url类型
                    wxMenuButton.setUrl(weChatMenuVo.getMenuUrl());
                    break;
                case 2://2 回复图片类型
                    wxMenuButton.setKey(weChatMenuVo.getMenuEventkey());
                    break;
                case 3://3 小程序
                    wxMenuButton.setUrl(WechatInterface.MENU_URL);
                    wxMenuButton.setAppId(wxConfigParams.getAppletAppid());
                    wxMenuButton.setPagePath(weChatMenuVo.getMenuUrl());
                    break;
                default:
                    break;
            }
        }

        wxMenuButton.setName(weChatMenuVo.getMenuName());
        wxMenuButton.setType(menuTypeStr);
        return wxMenuButton;
    }

}
