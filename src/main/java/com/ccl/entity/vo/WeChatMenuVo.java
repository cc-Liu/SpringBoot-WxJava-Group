package com.ccl.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 微信菜单管理
 *
 * @author liuc
 * @since 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WeChatMenu对象", description="微信菜单管理")
public class WeChatMenuVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer menuId;

    @ApiModelProperty(value = "view菜单对应的url或者是图片对应的url")
    private String menuUrl;

    @ApiModelProperty(value = "菜单对应的eventkey")
    private String menuEventkey;

    @ApiModelProperty(value = "菜单的名字")
    private String menuName;

    @ApiModelProperty(value = "包含图片和media以及返回菜单的文字内容")
    private String menuValue;

    @ApiModelProperty(value = "菜单顺序")
    private Integer menuOrder;

    @ApiModelProperty(value = "0表示作为一级菜单，二级菜单为一级菜单的主键")
    private Integer menuLevel;

    @ApiModelProperty(value = "菜单类型(拥有二级菜单的底部菜单为0) 0 底部菜单 1 url类型、2 回复文字 类型，3 回复图片类型 4 小程序")
    private Integer menuType;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

    @ApiModelProperty(value = "菜单是否删除 0：否 1:是")
    private Integer isDeleted;

    private List<WeChatMenuVo> weChatMenuVoList;
}
