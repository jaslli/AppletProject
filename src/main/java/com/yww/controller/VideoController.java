package com.yww.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yww.Result.Result;
import com.yww.entity.Plan;
import com.yww.entity.Video;
import com.yww.entity.VideoVo;
import com.yww.handler.ProjectException;
import com.yww.service.PlanService;
import com.yww.service.VideoService;
import com.yww.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  视频接口
 * </p>
 *
 * @author yww
 * @since 2021-03-23
 */
@RestController
@CrossOrigin
@Api(tags = "视频接口")
@RequestMapping("/video")
public class VideoController {

    @Resource
    RedisUtil util;

    private final VideoService videoService;
    private final PlanService planService;

    public VideoController(VideoService videoService, PlanService planService) {
        this.videoService = videoService;
        this.planService = planService;
    }

    @ApiOperation("获取所有视频数据(不建议使用该接口)")
    @GetMapping("/")
    public Result getInfo() {
        return Result.ok().data("list",videoService.list());
    }

    @ApiOperation("根据一级分类名称查询视频信息")
    @GetMapping("getByClass1/{first}")
    public Result getByClass1(
            @ApiParam(name = "first",value = "第一分类名称",required = true)
            @PathVariable("first") String first) {
        return Result.ok().data("videoList",videoService.getByClass1(first));
    }

    @ApiOperation("根据两级分类名称查询视频信息")
    @GetMapping("getByClass2/{first}/{secondary}")
    public Result getByClass2(
            @ApiParam(name = "first",value = "第一分类名称",required = true)
            @PathVariable("first") String first,
            @ApiParam(name = "secondary",value = "第二分类名称",required = true)
            @PathVariable("secondary") String secondary) {
        return Result.ok().data("videoList",videoService.getByClass2(first,secondary));
    }

    @ApiOperation("根据VideoId获取视频播放地址")
    @ApiImplicitParam(name = "videoId", value = "视频ID",required = true)
    @GetMapping("getUrl/{videoId}")
    public Result getUrl(@PathVariable("videoId") String videoId) {
        String url = videoService.getUrl(videoId);
        return Result.ok().data("url",url);
    }

    @ApiOperation("封面轮播图的视频数据")
    @GetMapping("getCarousel")
    public Result getCarousel() {
        return Result.ok().data("videoList",videoService.getCarousel(3L));
    }

    @ApiOperation("热门推荐的视频数据")
    @GetMapping("getHot")
    public Result getHot() {
        return Result.ok().data("videoList",videoService.getCarousel(3L));
    }
    
    @ApiOperation("获取首页分类的视频数据")
    @GetMapping("getMain/{first}")
    public Result getMain(
            @ApiParam(name = "first",value = "第一分类名称",required = true)
            @PathVariable("first") String first) {
        return Result.ok().data("videoList",videoService.getMain(first));
    }

    @ApiOperation("根据视频ID修改播放量")
    @GetMapping("update/{videoId}/{count}")
    public Result updateCount(
            @ApiParam(name = "videoId",value = "视频ID",required = true)
            @PathVariable("videoId") String videoId,
            @ApiParam(name = "count",value = "播放量",required = true)
            @PathVariable("count") double count) {
        videoService.updateCount(videoId,count);
        return Result.ok();
    }

    @ApiOperation("获取排行榜的视频数据,显示前15个视频")
    @GetMapping("getTop")
    public Result getTop() {
        return Result.ok().data("videoList",videoService.getCarousel(14L));
    }

    @ApiOperation("获取当天的学习计划视频")
    @GetMapping("getPlanVideo/{id}/{date}")
    public Result getPlanVideo(
            @ApiParam(name = "id",value = "计划ID",required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "date",value = "当前日期",required = true)
            @PathVariable("date") String date) {
        Plan plan = planService.getById(id);
        if (plan == null) {
            throw new ProjectException(20001,"没有计划");
        }
        String key = date.substring(0,date.lastIndexOf('-')) + "+" + id + plan.getOpenid();
        int day = Integer.parseInt(date.substring(date.lastIndexOf('-') + 1));
        if (!util.checkSign(key + "-0",day - 1)) {
            return Result.ok().message("今天该计划没有训练任务");
        }
        if (plan.getComplete() == 1) {
            return Result.ok().message("该计划已经全部完成了");
        }
        return Result.ok().data("videoList",videoService.getVideo(plan, key, day));
    }

    @ApiOperation("根据openid获取当天的学习计划视频")
    @GetMapping("getAllVideo/{openid}/{date}")
    public Result getAllVideo(
            @ApiParam(name = "openid",value = "微信openid",required = true)
            @PathVariable("openid") String openid,
            @ApiParam(name = "date",value = "当前日期",required = true)
            @PathVariable("date") String date) {
        QueryWrapper<Plan> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        List<Plan> planList = planService.list(wrapper);
        if (planList.size() == 0) {
            throw new ProjectException(20002,"当前用户没有计划！");
        }
        if (planList.size() == 1) {
            return getPlanVideo(planList.get(0).getId(),date);
        } else {
            List<VideoVo> videoList = videoService.getAllVideo(planList,openid,date);
            return Result.ok().data("videoList",videoList);
        }
    }

    @ApiOperation("修改视频信息（测试）")
    @PutMapping("updateVideo")
    public Result updateVideo(@RequestBody Video video) {
        videoService.updateById(video);
        return Result.ok();
    }

    @ApiOperation("视频上传(不要用)")
    @PostMapping("uploadVideo")
    public Result uploadVideo(MultipartFile file) {
        String videoId = videoService.uploadVideoAliyun(file);
        Video video = new Video();
        video.setName(file.getOriginalFilename());
        video.setVideoId(videoId);
        videoService.save(video);
        return Result.ok().data("videoId",videoId);
    }

    @ApiOperation("删除缓存")
    @GetMapping("del/{videoId}")
    public Result del(@PathVariable("videoId") String videoId){
        util.deletekey("video",videoId);
        return Result.ok();
    }

}

