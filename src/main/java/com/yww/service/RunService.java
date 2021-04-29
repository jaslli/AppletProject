package com.yww.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.entity.Run;
import org.springframework.stereotype.Repository;

/**
 * @author Yw
 */
@Repository
public interface RunService extends IService<Run> {

    /**
     * 添加跑步数据，重复添加会更新
     * @param run 跑步数据
     */
    void addRunData(Run run);

}
