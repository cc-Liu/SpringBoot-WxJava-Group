package com.ccl.util;

public enum CodeEnum {
    success(200, "SUCCESS！"),
    nameRepect(100, "编号重复！"),
    commonException(300, "EXCEPTION！"),
    paramError(400, "参数错误！"),
    nullValue(101, "字段不可为空！"),
    noImpactRecord(301, "无影响记录！"),
    NO_AUTH_LOGIN(308, "无登录权限！"),
    unknownException(201, "UNKNOWN-EXCEPTION！"),
    COMMON_ZERO(0,"全局0"),
    COMMON_ONE(1,"全局1"),
    COMMON_TWO(2,"全局2"),
    COMMON_THREE(3,"全局3"),
    COMMON_FOUR (4,"全局4"),
    COMMON_FIVE(5,"全局5"),
    COMMON_SIX(6,"全局6"),
    COMMON_SEVEN(7,"全局7"),
    COMMON_EIGHT (8,"全局8"),
    COMMON_NINE(9,"全局9"),
    COMMON_TEN(10,"全局10"),
    COMMON_ELEVEN(11,"全局11"),
    COMMON_TWELVE(12,"全局12"),
    COMMON_THIRTEEN(13,"全局13"),
    COMMON_FOURTEEN(14,"全局14"),
    COMMON_FIFTEEN(15,"全局15"),
    COMMON_MINUS_ONE(-1,"全局-1"),
    ;

    private Integer code;

    private String message;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
