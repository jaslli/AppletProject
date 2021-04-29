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
 *     用户的跑步数据实体类
 * </p>
 *
 * @ClassName Run
 * @Author yww
 * @Date 2021/4/21 13:00
 * @Version 1.0
 **/
@Data
@ApiModel(value = "Run",description = "用户跑步数据")
public class Run implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "微信openid")
    private String openid;

    @ApiModelProperty(value = "跑步数据")
    private BigDecimal data;

    @ApiModelProperty(value = "跑步时间")
    private String date;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createtime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE,update = "now()")
    private Date updatetime;

}
