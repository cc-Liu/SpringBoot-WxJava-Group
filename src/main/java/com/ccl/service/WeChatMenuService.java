package com.ccl.service;


import com.ccl.entity.wexin.WeChatMenuRequest;
import com.ccl.util.R;

/**
 * 微信菜单管理 服务类
 *
 * @author liuc
 * @since 2021-09-17
 */
public interface WeChatMenuService{

    R createWeChatMenu(WeChatMenuRequest weChatMenuRequest);

    R delWeChatMenu(WeChatMenuRequest weChatMenuRequest);
}
