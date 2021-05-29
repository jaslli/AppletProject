package com.yww.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yww.Result.Result;
import com.yww.entity.Plan;
import com.yww.entity.PlanDto;
import com.yww.entity.PlanVo;
import com.yww.handler.ProjectException;
import com.yww.service.PlanService;
import com.yww.service.VideoService;
import com.yww.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *     计划接口
 * </p>
 *
 * @ClassName PlanController
 * @Author yww
 * @Date 2021/4/16 1:40
 * @Version 1.0
 **/
@RestController
@CrossOrigin
@Api(tags = "计划接口")
@RequestMapping("/plan")
public class PlanController {

    @Resource
    RedisUtil util;

    private final VideoService videoService;
    private final PlanService planService;

    public PlanController(PlanService planService, VideoService videoService) {
        this.videoService = videoService;
        this.planService = planService;
    }

    @ApiOperation("查询当前用户的所有计划")
    @ApiImplicitParam(name = "openid", value = "微信的openid",required = true)
    @GetMapping("/selectAll/{openid}")
    public Result selectAll(@PathVariable("openid") String openid) {
        QueryWrapper<Plan> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        List<Plan> list = planService.list(wrapper);
        return Result.ok().data("list",list);
    }

    @ApiOperation("查询所有计划")
    @GetMapping("showAll")
    public Result showAll() {
        List<PlanVo> list = planService.getPlan();
        return Result.ok().data("list", list);
    }

    @ApiOperation("新增训练计划")
    @PostMapping("/addPlan")
    public Result addPlan(@RequestBody PlanDto plandto) {
        Plan plan = planService.setPlan(plandto);
        planService.save(plan);
        return Result.ok();
    }

    @ApiOperation("计划显示")
    @GetMapping("indexPlan/{id}/{date}")
    public Result indexPlan(
            @ApiParam(name = "id",value = "计划ID",required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "date",value = "日期字符串",required = true)
            @PathVariable("date") String date) throws ParseException {
        List<Integer> list = planService.indexPlan(id,date);
        return Result.ok().data("list",list);
    }

    @ApiOperation("1根据openid获取计划显示")
    @GetMapping("getIndexPlan1/{openid}/{date}")
    public Result getIndexPlan1(
            @ApiParam(name = "openid",value = "用户openid",required = true)
            @PathVariable("openid") String openid,
            @ApiParam(name = "date",value = "日期字符串",required = true)
            @PathVariable("date") String date) throws ParseException {
        QueryWrapper<Plan> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        wrapper.select("id,complete");
        List<Plan> planList = planService.list(wrapper);
        if (planList.size() == 0) {
            throw new ProjectException(20002,"当前用户没有计划！");
        }
        if (planList.size() == 1) {
            return indexPlan(planList.get(0).getId(),date);
        } else {
            List<Integer> list = planService.getIndexPlan1(planList,date);
            return Result.ok().data("list",list);
        }
    }

    @ApiOperation("2根据openid获取计划显示")
    @GetMapping("getIndexPlan2/{openid}/{date}")
    public Result getIndexPlan2(
            @ApiParam(name = "openid",value = "用户openid",required = true)
            @PathVariable("openid") String openid,
            @ApiParam(name = "date",value = "日期字符串",required = true)
            @PathVariable("date") String date) throws ParseException {
        QueryWrapper<Plan> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        wrapper.ne("complete",1);
        wrapper.select("id");
        List<Plan> planList = planService.list(wrapper);
        if (planList.size() == 0) {
            throw new ProjectException(20002,"当前用户没有计划！");
        }
        if (planList.size() == 1) {
            return indexPlan(planList.get(0).getId(),date);
        } else {
            List<Integer> list = planService.getIndexPlan2(planList,date);
            return Result.ok().data("list",list);
        }
    }


    @ApiOperation("判断用户是否有计划在执行")
    @ApiImplicitParam(name = "openid", value = "微信的openid",required = true)
    @GetMapping("isPlan/{openid}")
    public Result isPlan(@PathVariable("openid") String openid) {
        QueryWrapper<Plan> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        List<Plan> list = planService.list(wrapper);
        int res = 0;
        int count1 = 0;
        int count2 = 0;
        for (Plan plan : list) {
            if (plan.getComplete() == 1) {
                continue;
            }
            if (plan.getName().equals("跑步训练")) {
                count1++;
            } else {
                count2++;
            }
        }
        if (count1 == 0 && count2 == 0) {
            res = 0;
        } else {
            res = 2;
            // 有跑步计划，没有普通计划
            if (count2 == 0) {
                res = 1;
            }
            // 有普通计划，没有跑步计划
            if (count1 == 0) {
                res = 3;
            }
        }
        return Result.ok().data("res",res);
    }

    @ApiOperation("跑步计划打卡")
    @GetMapping("doSign/{id}/{date}")
    public Result doSign(
            @ApiParam(name = "id",value = "计划ID",required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "date",value = "当前日期",required = true)
            @PathVariable("date") String date) {
        Plan plan = planService.getById(id);
        String key = date.substring(0,date.lastIndexOf('-')) + "+" + id + plan.getOpenid();
        int day = Integer.parseInt(date.substring(date.lastIndexOf('-') + 1));
        if (!util.checkSign(key + "-0",day - 1)) {
            return Result.ok().message("今天该计划没有训练任务");
        }
        if (plan.getComplete() == 1) {
            return Result.ok().message("该计划已经全部完成了");
        }
        if (plan.getProgress() + 1 == plan.getExtent()) {
            plan.setComplete(1);
        }
        // 判断该天的计划是否已经进行增加
        if (!videoService.isAdded(plan.getUpdatetime()) || plan.getProgress() == 1) {
            plan.setProgress(plan.getProgress() + 1);
            planService.updateById(plan);
            util.doSign(key + "-1",day - 1);
        }
        return Result.ok().message("打卡成功");
    }

    @ApiOperation("视频计划打卡")
    @GetMapping("doTSign/{id}/{date}")
    public Result doTSign(
            @ApiParam(name = "id",value = "计划ID",required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "date",value = "当前日期",required = true)
            @PathVariable("date") String date) {
        Plan plan = planService.getById(id);
        String key = date.substring(0,date.lastIndexOf('-')) + "+" + id + plan.getOpenid();
        int day = Integer.parseInt(date.substring(date.lastIndexOf('-') + 1));
        if (!util.checkSign(key + "-0",day - 1)) {
            return Result.ok().message("今天该计划没有训练任务");
        }
        if (plan.getComplete() == 1) {
            return Result.ok().message("该计划已经全部完成了");
        }
        util.doSign(key + "-1",day - 1);
        return Result.ok().message("打卡成功");
    }

    @ApiOperation("根据计划ID删除计划")
    @ApiImplicitParam(name = "id", value = "计划ID",required = true)
    @DeleteMapping("deletePlan/{id}")
    public Result deletePlan(@PathVariable("id") String id) {
        planService.removeById(id);
        return Result.ok();
    }

}
