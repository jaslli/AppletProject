package com.yww.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.entity.Plan;
import com.yww.entity.PlanDto;
import com.yww.entity.PlanVo;
import com.yww.mapper.PlanMapper;
import com.yww.service.PlanService;
import com.yww.utils.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *     计划的服务实现类
 * </p>
 *
 * @ClassName PlanServiceImpl
 * @Author yww
 * @Date 2021/4/16 1:43
 * @Version 1.0
 **/
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

    @Resource
    private RedisUtil util;

    /**
     * 完善计划的信息
     * @param plandto 计划的传输类
     * @return 计划实体
     */
    @Override
    public Plan setPlan(PlanDto plandto) {
        Plan plan = new Plan();
        plan.setOpenid(plandto.getOpenid());
        plan.setName(plandto.getName());
        plan.setDate(plandto.getDate());
        plan.setExtent(getComplete(plan.getName()));
        plan.setCover(plandto.getCover());
        plan.setProgress(0);
        plan.setComplete(0);
        return plan;
    }


    /**
     * 获取该计划的总时间数
     * @param name  计划名称
     * @return      计划的总时间
     */
    private int getComplete(String name) {
        if ("新手入门".equals(name)) {
            return 30;
        } else if ("跑步训练".equals(name)) {
            return 40;
        } else {
            return 33;
        }
    }


    /**
     * 计划页的显示
     * 0代表是没有制定计划
     * 1代表了制定的计划
     * 2代表了制定的计划已经完成
     * 3代表制定的计划没有完成
     * @param id 计划ID
     * @param date  当前日期
     * @return  当月的签到情况
     * @throws ParseException   异常
     */
    @Override
    public List<Integer> indexPlan(String id,String date) throws ParseException {
        Plan plan = baseMapper.selectById(id);
        int remains = plan.getExtent() - plan.getProgress();
        String key = date.substring(0,date.lastIndexOf('-')) + "+" + id + plan.getOpenid();
        int count = getMonthDay(date.substring(0,date.lastIndexOf('-')));
        int day = Integer.parseInt(date.substring(date.lastIndexOf('-') + 1));
        List<Integer> res = new ArrayList<>();
        if (!util.isExistBitMap(key + "-0")) {
            createBitMap(key + "-0",plan.getDate(),count);
        }
        for (int i = 0; i < count; i++) {
            if (util.checkSign(key + "-0",i) && i < remains) {
                res.add(1);
            } else {
                res.add(0);
            }
        }
        for (int i = 0; i < day; i++) {
            // 2代表制定的计划完成
            if (util.checkSign(key + "-1",i) && res.get(i) == 1) {
                res.set(i,2);
            // 3代表制定的计划没有完成
            } else if (!util.checkSign(key + "-1",i) && res.get(i) == 1) {
                res.set(i,3);
            }
        }
        return res;
    }


    /**
     * 创建一个位图
     * @param key   位图的Key
     * @param date  制定的星期几
     * @param count 当前月的天数
     */
    private void createBitMap(String key,String date,int count) throws ParseException {
        char[] arr = date.toCharArray();
        HashMap<Integer, Integer> map = new HashMap<>(16);
        for (char c : arr) {
            map.put(c - '0',1);
        }
        String format = key.substring(0,key.lastIndexOf('+'));
        System.out.println(format);
        for (int i = 0; i < count; i++) {
            System.out.println(format + "-" + i);
            if (map.containsKey(isDay(format + "-" + (i + 1)))) {
                util.doSign(key,i);
            }
        }
    }


    /**
     * 用来判断当前日期是星期几
     * @param formatDate    日期字符串
     * @return              星期几
     * @throws ParseException   异常
     */
    private int isDay(String formatDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(formatDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = 0;
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            day = 7;
        }else{
            day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return day;
    }

    /**
     * 用来判断该月有几天
     * @param formatDate    日期字符串
     * @return              该月的天数
     * @throws ParseException   异常
     */
    private int getMonthDay(String formatDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = format.parse(formatDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);
        calendar.roll(Calendar.DATE,-1);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 展示所有计划
     * @return  计划列表
     */
    @Override
    public List<PlanVo> getPlan() {
        PlanVo vo1 = new PlanVo();
        PlanVo vo2 = new PlanVo();
        PlanVo vo3 = new PlanVo();
        PlanVo vo4 = new PlanVo();
        vo1.setName("新手入门");
        vo1.setCover("https://img.yww52.com/test/%E6%96%B0%E6%89%8B%E5%85%A5%E9%97%A8.jpeg");
        vo1.setExtent(30);
        vo2.setName("30分钟系列");
        vo2.setCover("https://img.yww52.com/test/30%E5%88%86%E9%92%9F%E7%B3%BB%E5%88%97.jpeg");
        vo2.setExtent(33);
        vo3.setName("45分钟系列");
        vo3.setCover("https://img.yww52.com/test/45%E5%88%86%E9%92%9F%E7%B3%BB%E5%88%97.jpeg");
        vo3.setExtent(33);
        vo4.setName("跑步训练");
        vo4.setCover("https://img.yww52.com/test/%E8%B7%91%E6%AD%A5%E8%AE%AD%E7%BB%83.jpeg");
        vo4.setExtent(40);
        List<PlanVo> list = new ArrayList<>();
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        return list;
    }

}
