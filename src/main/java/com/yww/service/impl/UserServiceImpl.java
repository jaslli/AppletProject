package com.yww.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.entity.User;
import com.yww.handler.ProjectException;
import com.yww.mapper.UserMapper;
import com.yww.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 *     用户服务实现类
 * </p>
 *
 * @ClassName UserServiceImpl
 * @Author yww
 * @Date 2021/3/2 20:49
 * @Version 1.0
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    /**
     * 更新用户数据
     * @param user 用户数据
     */
    @Override
    public void updateUser(User user) {
        if(user.getOpenid().isEmpty()) {
            throw new ProjectException(20001, "用户数据不能为空");
        }
        String openid = user.getOpenid();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        User updateuser = baseMapper.selectOne(wrapper);
        updateuser.setBirthday(user.getBirthday());
        updateuser.setSex(user.getSex());
        baseMapper.updateById(updateuser);
    }

    /**
     * 根据出生日期判断年龄
     * @param birthday  出生日期
     * @return          当前的年龄
     */
    @Override
    public int getAge(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.before(birthday)) {
            throw new ProjectException(20001,"出生日期不合法");
        }
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(birthday);
        int birthYear = calendar.get(Calendar.YEAR);
        int birthMonth = calendar.get(Calendar.MONTH);
        int birthDay = calendar.get(Calendar.DAY_OF_MONTH);
        int age = nowYear - birthYear;
        if (nowMonth <= birthMonth) {
            if (nowMonth == birthMonth) {
                if (nowDay < birthDay) {
                    age--;
                }
            } else{
                age--;
            }
        }
        return age;
    }

}
