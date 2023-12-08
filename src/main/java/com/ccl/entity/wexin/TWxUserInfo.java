package com.ccl.entity.wexin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信用户信息表
 *
 * @author liuc
 * @since 2021-09-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TWxUserInfo对象", description="微信用户信息表")
public class TWxUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer wxUserId;

    @ApiModelProperty(value = "小程序openid")
    private String appletOpenid;

    @ApiModelProperty(value = "微信公众号openid")
    private String publicOpenid;

    @ApiModelProperty(value = "用户的昵称")
    private String nickname;

    @ApiModelProperty(value = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知")
    private Integer sex;

    @ApiModelProperty(value = "用户所在城市")
    private String city;

    @ApiModelProperty(value = "用户所在国家")
    private String country;

    @ApiModelProperty(value = "用户所在省份")
    private String province;

    @ApiModelProperty(value = "用户的语言，简体中文为zh_CN")
    private String language;

    @ApiModelProperty(value = "用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。")
    private String headimgurl;

    @ApiModelProperty(value = "用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间")
    private Date subscribeTime;

    @ApiModelProperty(value = "只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。")
    private String unionId;

    @ApiModelProperty(value = "公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注")
    private String remark;

    @ApiModelProperty(value = "用户所在的分组ID（兼容旧的用户分组接口）")
    private Integer groupId;

    @ApiModelProperty(value = "用户被打上的标签ID列表")
    private String tagidList;

    @ApiModelProperty(value = "返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他")
    private String subscribeScene;

    @ApiModelProperty(value = "二维码扫码场景（开发者自定义）")
    private String qrScene;

    @ApiModelProperty(value = "二维码扫码场景描述（开发者自定义）")
    private String qrSceneStr;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "用户是否关注了该公众号 0：关注 1：未关注")
    private Integer isSubscribe;
}
