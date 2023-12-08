package com.ccl.entity.wexin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 模板数据
 * @Author admin
 * @Date 2021/6/1 15:37
 * @Version 1.0
 */
@Data
@ApiModel(value="公众号消息推送对象", description="公众号消息推送")
public class TemplateParams {

    @ApiModelProperty(value = "模板消息内容")
    private List<KeyWordParams> valueList;

    @ApiModelProperty(value = "模板类型 1:消费 2:充值 3:退款")
    private Integer templateType;

    @ApiModelProperty(value = "会员手机号")
    private String mobile;

    @ApiModelProperty(value = "模板备注")
    private String remark;

    @ApiModelProperty(value = "模板id")
    private String templateId;
}
