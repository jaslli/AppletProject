package com.yww.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *     计划的实体类
 * </p>
 *
 * @ClassName Plan
 * @Author yww
 * @Date 2021/4/16 1:35
 * @Version 1.0
 **/
@Data
@ApiModel(value = "Plan",description = "用户")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "微信openid")
    private String openid;

    @ApiModelProperty(value = "计划的分类名称")
    private String name;

    @ApiModelProperty(value = "制定的计划日期，用一个字符串来表示")
    private String date;

    @ApiModelProperty(value = "计划的总时间")
    private int extent;

    @ApiModelProperty(value = "封面地址")
    private String cover;

    @ApiModelProperty(value = "执行计划的当前进度")
    private int progress;

    @ApiModelProperty(value = "是否完成，完成则为1，还没完成为0")
    private int complete;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createtime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE,update = "now()")
    private Date updatetime;

}
