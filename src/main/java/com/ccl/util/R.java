package com.ccl.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author lc
 * @Description 统一返回结果的类
 * @Date 11:29 2021/4/20
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();

    @ApiModelProperty(value = "返回数据")
    private T resultData;

    /*
     * @Author lj
     * @Description 把构造方法私有
     * @Date 11:30 2021/4/20
     **/
    private R() {}

    /*
     * @Author lj
     * @Description 成功静态方法
     * @Date 11:30 2021/4/20
     **/
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(200);
        r.setMessage("成功");
        return r;
    }

    /*
     * @Author lj
     * @Description 失败静态方法
     * @Date 11:30 2021/4/20
     **/
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(201);
        r.setMessage("失败");
        return r;
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.setCode(code);
        r.setMessage(msg);
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    public R resultData(T resultData){
        this.resultData = resultData;
        return this;
    }

}
