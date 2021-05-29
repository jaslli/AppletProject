package com.yww.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Date;


/**
 * @author yww
 */
@Repository
public interface UserService extends IService<User> {
    /**
     *  更新用户的数据
     * @param user 用户数据
     */
    void updateUser(User user);

    /**
     * 根据出生日期判断年龄
     * @param birthday  出生日期
     * @return          当前的年龄
     */
    int getAge(Date birthday);

    /**
     * 获取使用的天数
     * @param user 用户
     * @return 使用的天数
     */
    int getUsed(User user);
}
