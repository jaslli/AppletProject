package com.yww.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yww.entity.Body;
import com.yww.mapper.BodyMapper;
import com.yww.service.BodyService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *     身体数据的服务实现类
 * </p>
 *
 * @ClassName BodyServiceImpl
 * @Author yww
 * @Date 2021/3/4 13:48
 * @Version 1.0
 **/
@Service
public class BodyServiceImpl extends ServiceImpl<BodyMapper,Body> implements BodyService {
    /**
     *  添加身体数据
     */
    @Override
    public void addBodyData(Body body) {
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDay = sf.format(now);

        QueryWrapper<Body> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 1");
        Body body1 = baseMapper.selectOne(wrapper);
        String dateDay = sf.format(body1.getCreatetime());

        if (nowDay.equals(dateDay)) {
            body1.setHeight(body.getHeight());
            body1.setRate(body.getRate());
            body1.setBmi(body.getBmi());
            body1.setWeight(body.getWeight());
            baseMapper.updateById(body1);
        } else {
            baseMapper.insert(body);
        }
    }
}
