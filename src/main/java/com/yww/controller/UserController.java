package com.yww.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yww.Result.Result;
import com.yww.entity.User;
import com.yww.handler.ProjectException;
import com.yww.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

/**
 * <p>
 *     用户接口
 * </p>
 *
 * @ClassName UserController
 * @Author yww
 * @Date 2021/3/2 20:51
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@Api(tags = "用户接口")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiImplicitParam(name = "user", value = "用户对象",required = true)
    @ApiOperation("添加用户")
    @PostMapping("/add")
    public Result addUser(@RequestBody User user) {
        if (user == null) {
            throw new ProjectException(20001, "用户信息不能为空");
        }
        userService.save(user);
        return Result.ok();
    }

    @ApiOperation("查询所有用户")
    @GetMapping("/findAll")
    public Result findAll() {
        List<User> list = userService.list();
        return Result.ok().data("list", list);
    }

    @ApiImplicitParam(name = "openid", value = "微信的openid",required = true)
    @ApiOperation("根据OPENID查询用户的信息")
    @GetMapping("/getUserById/{openid}")
    public Result getUserById(@PathVariable("openid") String openid) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        User user = userService.getOne(wrapper);
        return Result.ok().data("user", user);
    }

    @ApiImplicitParam(name = "openid", value = "微信的openid",required = true)
    @ApiOperation("根据OPENID查询用户的年龄")
    @GetMapping("/getAge/{openid}")
    public Result getAge(@PathVariable("openid") String openid) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        User user = userService.getOne(wrapper);
        Date date = user.getBirthday();
        if (date == null) {
            return Result.ok().message("请填写生日后在进行操作");
        }
        int age = userService.getAge(user.getBirthday());

        return Result.ok().data("age",age);
    }

    @ApiImplicitParam(name = "user", value = "需要更新的用户信息",required = false)
    @ApiOperation("根据用户id更新数据，主要是更新生日和性别")
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.ok();
    }

    @ApiImplicitParam(name = "openid", value = "用户的openid", required = true)
    @ApiOperation("获取用户使用时间(返回使用的天数)")
    @GetMapping("/getUsed/{openid}")
    public Result getUsed(@PathVariable("openid")String openid) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        User user = userService.getOne(wrapper);
        return Result.ok().data("time",userService.getUsed(user));
    }

}
