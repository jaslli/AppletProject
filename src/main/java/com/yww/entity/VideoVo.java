package com.yww.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName VideoVo
 * @Descriprtion TODO
 * @Author yww
 * @Date 2021/5/14 20:55
 * @Version 1.0
 **/
@Data
public class VideoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频的名称")
    private String name;

    @ApiModelProperty(value = "视频的播放地址")
    private String url;

    @ApiModelProperty(value = "视频的封面地址")
    private String cover;

    @ApiModelProperty(value = "视频播放的次数")
    private BigDecimal count;

    @ApiModelProperty(value = "计划的分类名称")
    private String planName;

    @ApiModelProperty(value = "计划的总时间")
    private int extent;

    @ApiModelProperty(value = "执行计划的当前进度")
    private int progress;

    @ApiModelProperty(value = "计划ID")
    private String planId;

}
