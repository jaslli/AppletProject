package com.yww.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.entity.Video;
import com.yww.mapper.VideoMapper;
import com.yww.service.VideoService;
import com.yww.utils.ConstantVodUtils;
import com.yww.utils.InitVod;
import com.yww.utils.RedisUtil;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @ClassName UserServiceImpl
 * @Author yww
 * @Date 2021/3/2 20:49
 * @Version 1.0
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Resource
    private RedisUtil util;


    /**
     * 完善视频实体的信息，获取视频播放地址和封面地址
     * @param video 视频实体
     */
    @Override
    public void getAllInfo(Video video) {
        String videoId = video.getVideoId();
        String url = null;
        String cover = null;
        try {
            // 初始化客户端
            DefaultAcsClient client = InitVod.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET);
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(videoId);
            GetPlayInfoResponse response = client.getAcsResponse(request);
            // 获取播放地址
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                url = playInfo.getPlayURL();
            }
            video.setUrl(url);
            // 获取封面
            cover = response.getVideoBase().getCoverURL();
            video.setCover(cover);
            // 设置名字
            if (video.getName() == null) {
                String title = setWlName(response.getVideoBase().getTitle());
                video.setName(title);
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据一级分类查询视频信息
     * @param first         一级分类名称
     * @return              视频列表
     */
    @Override
    public List<Video> getByClass1(String first) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(first)) {
            wrapper.eq("first_classification",first);
        }
        List<Video> videoList = baseMapper.selectList(wrapper);
        setList(videoList);
        return videoList;
    }

    /**
     * 根据两级分类查询视频信息
     * @param first         一级分类名称
     * @param secondary     二级分类名称
     * @return              视频列表
     */
    @Override
    public List<Video> getByClass2(String first, String secondary) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(first)) {
            wrapper.eq("first_classification",first);
        }
        if (!StringUtils.isEmpty(secondary)) {
            wrapper.eq("secondary_classification",secondary);
        }
        List<Video> videoList = baseMapper.selectList(wrapper);
        setList(videoList);
        return videoList;
    }

    /**
     * 完善视频的信息，加入排行榜，并设置播放量信息
     * @param videoList 视频集合
     */
    private void setList(List<Video> videoList) {
        for (Video video : videoList) {
            if (util.getScore("video",video.getVideoId()) == null) {
                util.addZset("video",video.getVideoId(),1);
            } else {
                util.incrementScore("video",video.getVideoId(),1);
            }
            getAllInfo(video);
            video.setCount(BigDecimal.valueOf(util.getScore("video",video.getVideoId())));
        }
    }

    /**
     * 获取封面轮播图的视频数据
     * @return  轮播图的视频数据
     */
    @Override
    public List<Video> getCarousel(long top) {
        Set<ZSetOperations.TypedTuple<Object>> set = util.getTop("video",top);
        List<Video> videoList = new ArrayList<>();
        if (set == null) {
            return null;
        }
        for (ZSetOperations.TypedTuple<Object> typedTuple : set) {
            Video video = new Video();
            video.setVideoId((String) typedTuple.getValue());
            getAllInfo(video);
            if (typedTuple.getScore() != null) {
                video.setCount(BigDecimal.valueOf(typedTuple.getScore()));
            }
            videoList.add(video);
        }
        return videoList;
    }

    /**
     * 获取首页分类的视频数据（取前四个视频的数据）
     * @param first     一级分类名称
     * @return          初始分类的视频数据
     */
    @Override
    public List<Video> getMain(String first) {
        QueryWrapper<Video> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(first)) {
            wrapper.eq("first_classification",first);
        }
        wrapper.last("limit 4");
        List<Video> videoList = baseMapper.selectList(wrapper);
        for (Video video : videoList) {
            getAllInfo(video);
            if (util.getScore(first,video.getVideoId()) != null) {
                video.setCount(BigDecimal.valueOf(util.getScore("video",video.getVideoId())));
            }
        }
        return videoList;
    }


    /**
     * 根据视频ID修改播放量
     * @param videoId       视频ID
     * @param count         播放量
     */
    @Override
    public void updateCount(String videoId,double count) {
        util.addZset("video",videoId,count);
    }

    /**
     * 判断当前日期是否已经进行增加
     * @param planDate  计划修改日期
     * @return          true表示这是重复获取
     */
    @Override
    public boolean isAdded(Date planDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String nowday = format.format(now);
        String planday = format.format(planDate);
        return nowday.equals(planday);
    }


    /**
     * 修改显示的名称
     */
    private String setWlName(String title) {
        if (title.indexOf('-') != -1) {
            if (title.lastIndexOf('[') != -1) {
                title = title.substring(title.indexOf('-') + 1,title.lastIndexOf('['));
            }else {
                title = title.substring(title.indexOf('-') + 1);
            }
        }
        return title;
    }


    /**
     * 视频文件上传
     * @param file 上传的文件
     * @return 返回视频的ID
     */
    @Override
    public String uploadVideoAliyun(MultipartFile file) {
        // 上传文件的名称
        String fileName = file.getOriginalFilename();
        // 上传后的名称
        String title = null;
        if (fileName != null) {
            title = fileName.substring(0, fileName.lastIndexOf("."));
        }
        // 上传文件流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UploadStreamRequest request = new UploadStreamRequest
                (ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

        String videoId = null;
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        //请求视频点播服务的请求ID
        System.out.print("RequestId=" + response.getRequestId() + "\n");

        videoId = response.getVideoId();
        return videoId;
    }

    /**
     * 通过视频ID获取视频播放地址
     * @param videoId    视频ID
     * @return      视频播放地址
     */
    @Override
    public String getUrl(String videoId) {
        String url = null;
        try {
            // 初始化客户端
            DefaultAcsClient client = InitVod.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET);
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(videoId);
            GetPlayInfoResponse response = client.getAcsResponse(request);
            // 获取播放地址
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                url = playInfo.getPlayURL();
            }
            if (util.getScore("video",videoId) == null) {
                util.addZset("video",videoId,1);
            } else {
                util.incrementScore("video",videoId,1);
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return url;
    }

}
