package com.ccl.constant;

/**
 * @Author liuc
 * @Description 微信官方提供的功能性接口常量
 * @Date 2021/9/16 15:58
 * @Param
 * @return
 **/
public class WechatInterface {

    /**
     * redis文件夹前缀
     */
    public static final String REDIS_FOLDER = "wxConfig:";

    public static final String WX_CONFIG = "wxConfig";

    public static final String SYS_CONFIG = "sys_config";


    //小程序配置参数
    /**
     * 小程序appid
     */
    public static final String APPLET_APPID = "wxConfig:applet_appid";

    /**
     * 微获取小程序全局唯一后台接口调用凭据
     */
    public static final String ACCESS_TOKEN = "wxConfig:accessToken";

    /**
     * 小程序跳转路径
     */
    public static final String APPLET_URL = "wxConfig:applet_url";

    /**
     * 小程序secret
     */
    public static final String APPLET_SECRET = "wxConfig:applet_secret";


    //公众号配置参数
    /**
     * 微信公众号的appid
     */
    public static final String APPID = "wxConfig:appId";

    /**
     * 微信公众号的token
     */
    public static final String TOKEN = "wxConfig:token";

    /**
     * 微信公众号的app secret
     */
    public static final String SECRET = "wxConfig:secret";

    /**
     * 微信公众号的EncodingAESKey
     */
    public static final String AESKEY = "wxConfig:aesKey";

    /**
     * 微信关注自动回复
     */
    public static final String WX_FOLLOW_REPLY = "wxConfig:wx_follow_reply";

    /**
     * 有余额消息模板
     */
    public static final String BALANCE_TEMPLATE_ID = "wxConfig:balance_template_id";

    /**
     * 无余额消息模板
     */
    public static final String NO_BALANCE_TEMPLATE_ID = "wxConfig:No_balance_template_id";

    /**
     * 充值模板头部
     */
    public static final String CZ_FIRST_DATA = "wxConfig:cz_first_data";

    /**
     * 消费模板头部
     */
    public static final String XF_FIRST_DATA = "wxConfig:xf_first_data";

    /**
     * 订单退款头部
     */
    public static final String TK_FIRST_DATA = "wxConfig:tk_first_data";

    /**
     * 网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，不支持小程序的老版本客户端将打开本url。
     */
    public static final String MENU_URL = "https://www.baidu.com/";
}
