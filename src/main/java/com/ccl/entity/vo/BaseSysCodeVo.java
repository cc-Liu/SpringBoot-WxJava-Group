package com.ccl.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 字典表
 * @Author admin
 * @Date 2021/6/16 14:28
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseSysCodeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "代码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父级编码")
    @TableField("parentCode")
    private String parentCode;

    @ApiModelProperty(value = "级别")
    private Integer level;

    @ApiModelProperty(value = "是否可用：0否，1是")
    @TableField("isEnable")
    private Integer isEnable;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    @TableField("orderId")
    private Integer orderId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
