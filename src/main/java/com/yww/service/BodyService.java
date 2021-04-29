package com.yww.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.entity.Body;
import org.springframework.stereotype.Repository;

/**
 * @author yww
 */
@Repository
public interface BodyService extends IService<Body> {
    /**
     * 添加身体数据，重复添加会更新
     * @param body 身体数据
     */
    void addBodyData(Body body);
}
