package com.ccl.entity.wexin;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="公众号消息推送模板内容")
public class KeyWordParams {

    //内容
    private String value;

    //颜色
//    private String color;

    //参数类型 1:thing 2:amount 3:time
    private String KeyWordType;

    public KeyWordParams(String value) {
        this.value = value;
    }

    public KeyWordParams(String value, String KeyWordType) {
        this.value = value;
        this.KeyWordType = KeyWordType;
    }

}
