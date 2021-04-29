package com.yww.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PlanVo
 * @Author yww
 * @Date 2021/4/20 20:14
 * @Version 1.0
 **/
@Data
public class PlanVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private int extent;

    private String cover;

}
