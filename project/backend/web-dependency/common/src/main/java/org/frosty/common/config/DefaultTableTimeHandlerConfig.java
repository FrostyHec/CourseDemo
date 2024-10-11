//package org.frosty.common.config;
// TODO clean
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.frosty.common.constant.VarConstant;
//
//import java.time.OffsetDateTime;
//
//
//public class DefaultTableTimeHandlerConfig implements MetaObjectHandler {
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.strictInsertFill(metaObject, VarConstant.CREATED_AT, OffsetDateTime.class, OffsetDateTime.now());
//        this.strictUpdateFill(metaObject, VarConstant.UPDATED_AT,OffsetDateTime.class, OffsetDateTime.now() );
//
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.strictUpdateFill(metaObject, VarConstant.UPDATED_AT,OffsetDateTime.class, OffsetDateTime.now() );
//    }
//}