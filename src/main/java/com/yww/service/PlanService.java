package com.yww.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yww.entity.Plan;
import com.yww.entity.PlanDto;
import com.yww.entity.PlanVo;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.util.List;

/**
 * @author yww
 */
@Repository
public interface PlanService extends IService<Plan> {

    /**
     * 完善计划的信息
     * @param plandto 计划的传输类
     * @return 计划实体
     */
    Plan setPlan(PlanDto plandto);

    /**
     * 计划显示状态
     * @param id 计划ID
     * @param date  当前日期
     * @return  该月的状态
     * @exception ParseException 异常
     */
    List<Integer> indexPlan(String id,String date) throws ParseException;

    /**
     * 展示所有计划
     * @return  计划列表
     */
    List<PlanVo> getPlan();
}
