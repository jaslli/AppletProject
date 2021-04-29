package com.yww.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *     用户的身体数据实体类
 * </p>
 *
 * @ClassName body
 * @Author yww
 * @Date 2021/3/4 13:40
 * @Version 1.0
 **/
@Data
@ApiModel(value = "Body",description = "用户健康数据")
public class Body implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "微信openid")
    private String openid;

    @ApiModelProperty(value = "身高")
    private BigDecimal height;

    @ApiModelProperty(value = "体重")
    private BigDecimal weight;

    @ApiModelProperty(value = "BMI数据")
    private BigDecimal bmi;

    @ApiModelProperty(value = "体脂率")
    private BigDecimal rate;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createtime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE,update = "now()")
    private Date updatetime;

}
