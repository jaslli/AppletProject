package com.yww.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.entity.Run;
import com.yww.mapper.RunMapper;
import com.yww.service.RunService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName RunServiceImpl
 * @Author yww
 * @Date 2021/4/21 13:05
 * @Version 1.0
 **/
@Service
public class RunServiceImpl extends ServiceImpl<RunMapper, Run> implements RunService {

    /**
     * 添加跑步数据，重复添加会更新
     * @param run 跑步数据
     */
    @Override
    public void addRunData(Run run) {
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDay = sf.format(now);

        QueryWrapper<Run> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 1");
        Run run1 = baseMapper.selectOne(wrapper);
        if (run1 == null) {
            baseMapper.insert(run);
        } else {
            String dateDay = sf.format(run1.getCreatetime());
            // 如果今天已经传过更新数据
            if (nowDay.equals(dateDay)) {
                run1.setData(run.getData());
                baseMapper.updateById(run1);
            }
        }
    }

}
