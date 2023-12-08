package com.ccl.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccl.entity.vo.WeChatMenuVo;
import com.ccl.entity.wexin.WeChatMenu;

import java.util.List;


/**
 * 微信菜单管理 Mapper 接口
 *
 * @author liuc
 * @since 2021-09-17
 */
public interface WeChatMenuMapper extends BaseMapper<WeChatMenu> {

    List<WeChatMenu> selectWeChatMenuList();

    List<WeChatMenuVo> selectFirstWeChatMenu();

    List<WeChatMenuVo> selectSecondWeChatMenu();
}
