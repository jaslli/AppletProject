package com.yww.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yww.Result.Result;
import com.yww.entity.Run;
import com.yww.service.RunService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *     跑步数据接口
 * </p>
 *
 * @ClassName RunController
 * @Author yww
 * @Date 2021/4/21 13:07
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@Api(tags = "跑步数据接口")
@RequestMapping("/run")
public class RunController {

    private final RunService runService;

    public RunController(RunService runService) {
        this.runService = runService;
    }

    @ApiOperation("根据OPENID查询用户的所有跑步数据,显示前30天的数据")
    @ApiImplicitParam(name = "openid", value = "微信的openid",required = true)
    @GetMapping("/getDataList/{openid}")
    public Result getDataList(@PathVariable("openid") String openid) {
        QueryWrapper<Run> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.eq("openid",openid);
        wrapper.last("LIMIT 30");
        List<Run> list = runService.list(wrapper);
        return Result.ok().data("list", list);
    }

    @ApiOperation("添加跑步数据")
    @PostMapping("/addRunData")
    public Result addBodyData(@RequestBody Run run) {
        runService.save(run);
        return Result.ok();
    }

}
