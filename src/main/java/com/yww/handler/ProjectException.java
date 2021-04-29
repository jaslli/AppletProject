package com.yww.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     自定义异常
 * </p>
 *
 * @ClassName ProjectException
 * @Author yww
 * @Date 2021/3/2 9:47
 * @Version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectException extends RuntimeException {

    private Integer code;

    private String msg;

}
