package com.ccl.constant;

public enum WeChatTemplateTypeEnum {

    ENERGY_CONSUMPTION(1,"能源消费"),
    NON_OIL_CONSUMPTION(2,"非油品消费"),
    RECHARGE(3,"充值"),
    ;

    private Integer code;

    private String message;

    WeChatTemplateTypeEnum(Integer code, String message) {
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

    /**
     * @Author lj
     * @Description 根据Code值获取Message
     * @Date 16:58 2021/6/25
     * @Param [key]
     * @return java.lang.String
     **/
    public static String getValue(Integer key) {
        WeChatTemplateTypeEnum[] cardTypeEnums = values();
        for (WeChatTemplateTypeEnum cardTypeEnum : cardTypeEnums) {
            if (cardTypeEnum.getCode().equals(key)) {
                return cardTypeEnum.getMessage();
            }
        }
        return null;
    }
}
