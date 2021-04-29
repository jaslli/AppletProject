package com.yww.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 *     自动填充处理器,注意是对象属性名不是表的字段名
 * </p>
 * @ClassName MyMetaObjectHandler
 * @Author yww
 * @Date 2021/3/2 21:12
 * @Version 1.0
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时更新创建和修改时间
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createtime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updatetime", Date.class, new Date());
    }

    /**
     * 修改时更新修改时间
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatetime", Date.class, new Date());
    }
}
