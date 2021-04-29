package com.yww.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *     计划的的传输类
 * </p>
 *
 * @ClassName PlanDto
 * @Author yww
 * @Date 2021/4/16 2:26
 * @Version 1.0
 **/
@Data
public class PlanDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openid;

    private String name;

    private String date;

    private String cover;

    private int extent;

}
