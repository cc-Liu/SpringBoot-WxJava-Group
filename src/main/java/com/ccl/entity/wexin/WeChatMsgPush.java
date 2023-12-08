package com.ccl.entity.wexin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author liuc
 * @Description 公众号消息推送参数
 * @Date 2021/10/29 15:49
 * @Param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WeChatMsgPush {

    /*
     * 会员手机号
     */
    private String mobile;
    /*
     * 实际支付金额
     */
    private String payPrice;
    /*
     * 消费门店
     */
    private Integer siteName;
    /*
     * 消费时间
     */
    private String payTime;
    /*
     * 付款方式
     */
    private Integer payType;
    /*
     * 交易说明 (充值：油卡/气卡/氢卡/电卡充值 、消费：加油/加氢/充电/非油品消费)
     */
    private String remark;

    /*
     * 模板类型 1:能源消费 2:非油品消费 3:充值
     */
    private Integer templateType;

    /*
     * 余额
     */
    private String balance;

    /*
     * 赠送余额
     */
    private String giftBalance;

}
