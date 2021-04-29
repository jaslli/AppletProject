package com.yww.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yww.Result.Result;
import com.yww.entity.Body;
import com.yww.service.BodyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *     身体数据接口
 * </p>
 *
 * @ClassName BodyController
 * @Author yww
 * @Date 2021/3/4 13:57
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@Api(tags = "身体数据接口")
@RequestMapping("/body")
public class BodyController {

    private final BodyService bodyService;

    public BodyController(BodyService bodyService) {
        this.bodyService = bodyService;
    }

    @ApiOperation("根据OPENID查询用户的所有身体信息,按时间从新到旧排序")
    @ApiImplicitParam(name = "openid", value = "微信的openid",required = true)
    @GetMapping("/getDataList/{openid}")
    public Result getDataList(@PathVariable("openid") String openid) {
        QueryWrapper<Body> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.eq("openid",openid);
        List<Body> list = bodyService.list(wrapper);
        return Result.ok().data("list", list);
    }

    @ApiOperation("添加身体数据，一定要传入openid,每天重复提交会更新数据")
    @PostMapping("/addBodyData")
    public Result addBodyData(@RequestBody Body body) {
        bodyService.addBodyData(body);
        return Result.ok();
    }


}
