package com.ccl.entity.wexin;

import cn.binarywang.wx.miniapp.bean.Watermark;
import lombok.Data;

/**
 * @Auther: liuc
 * @Date: 2023/9/7 10:18
 * @Description:
 */
@Data
public class WxMaPhoneNumberResult {

    private String phoneNumber;

    private String purePhoneNumber;

    private String countryCode;

    private Watermark watermark;
}
