package com.yww.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.entity.Plan;
import com.yww.entity.Video;
import com.yww.entity.VideoVo;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author yww
 */
@Repository
public interface VideoService extends IService<Video> {


    /**
     * 完善视频实体的信息，获取视频播放地址和封面地址
     * @param video 视频实体
     */
    void getAllInfo(Video video);

    /**
     * 根据一级分类查询视频信息
     * @param first         一级分类名称
     * @return              视频列表
     */
    List<Video> getByClass1(String first);

    /**
     * 根据两级分类查询视频信息
     * @param first         一级分类名称
     * @param secondary     二级分类名称
     * @return              视频列表
     */
    List<Video> getByClass2(String first, String secondary);

    /**
     * 获取封面轮播图的视频数据
     * @param top   取视频的个数
     * @return      轮播图的视频数据
     */
    List<Video> getCarousel(long top);

    /**
     * 获取首页分类的视频数据（取前四个视频的数据）
     * @param first     一级分类名称
     * @return          初始分类的视频数据
     */
    List<Video> getMain(String first);

    /**
     * 根据视频ID修改播放量
     * @param videoId       视频ID
     * @param count         播放量
     */
    void updateCount(String videoId,double count);

    /**
     * 判断当前日期是否已经进行增加
     * @param planDate  计划修改日期
     * @return          true表示这是重复获取
     */
    boolean isAdded(Date planDate);

    /**
     * 视频上传
     * @param file  视频文件
     * @return  视频的videoId
     */
    String uploadVideoAliyun(MultipartFile file);

    /**
     * 通过视频ID获取视频播放地址
     * @param videoId    视频ID
     * @return      视频播放地址
     */
    String getUrl(String videoId);

    /**
     * 获取当天计划的所有视频
     */
    List<VideoVo> getVideo(Plan plan, String key, int day);

    /**
     * 根据openid获取当天全部视频
     */
    List<VideoVo> getAllVideo(List<Plan> planList,String openid, String date);
}
