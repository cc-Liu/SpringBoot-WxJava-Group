package com.ccl.enums;

public enum PayTypeEnum {

    NOWAY(0,"全部"),
    ALIPAY(1,"支付宝"),
    WECHAT(2,"微信"),
    UNIONPAY(3,"银联"),
    OFFLINE(4,"现金"),
    MEMBERPAY (5,"电子钱包"),
    MONEPAY(6,"实体卡"),
    BLENDPAY(8,"混合"),
    PUBLICTOPUBLIC(9,"公对公"),
    IC_CARD(10,"IC卡"),
    OTHER(11,"其他"),
    DIDI(12,"滴滴"),
    TUANYOU(13,"团油"),
    PETROL_COUPON(14,"油票"),
    BACK_POT(15,"回罐"),
    CNPC_IC_CARD(16,"中油/中石化IC卡"),
    EZJ(17,"易捷")
    ;

    private Integer code;

    private String message;

    PayTypeEnum(Integer code, String message) {
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
        PayTypeEnum[] payEnums = values();
        for (PayTypeEnum payEnum : payEnums) {
            if (payEnum.getCode().equals(key)) {
                return payEnum.getMessage();
            }
        }
        return null;
    }

}
