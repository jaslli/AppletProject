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
 *      视频实体类
 * </p>
 *
 * @ClassName Video
 * @Author yww
 * @Date 2021/3/2 20:35
 * @Version 1.0
 */
@Data
@ApiModel(value = "Video对象",description = "视频")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频数据ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "点播中的视频ID")
    @TableField("videoId")
    private String videoId;

    @ApiModelProperty(value = "视频的名称")
    private String name;

    @ApiModelProperty(value = "视频的播放地址")
    private String url;

    @ApiModelProperty(value = "视频的封面地址")
    private String cover;

    @ApiModelProperty(value = "视频播放的次数")
    private BigDecimal count;

    @ApiModelProperty(value = "一级分类名称")
    private String firstClassification;


    @ApiModelProperty(value = "二级分类名称")
    private String secondaryClassification;


    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createtime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE,update = "now()")
    private Date updatetime;


}
