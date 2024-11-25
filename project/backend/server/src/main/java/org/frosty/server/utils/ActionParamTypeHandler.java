package org.frosty.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.frosty.common.exception.InternalException;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.entity.bo.market.action_type.ActionParam;
import org.frosty.server.entity.bo.market.action_type.BuyBadgeActionParam;
import org.frosty.server.entity.bo.market.action_type.CompleteCourseActionParam;
import org.frosty.server.entity.bo.market.action_type.DailyCommentActionParam;

import java.sql.*;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(ActionParam.class)
public class ActionParamTypeHandler extends BaseTypeHandler<ActionParam> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ActionParam parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setObject(i, objectMapper.writeValueAsString(parameter), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new InternalException("",e);
        }
    }


    @Override
    public ActionParam getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String actionParamJson = rs.getString(columnName);
        if (actionParamJson == null || actionParamJson.isEmpty()) {
            return null;
        }

        try {
            // 根据 actionType 来反序列化
            String actionType = rs.getString("action_type");
            Class<?> clazz = getActionParamClass(actionType);
            return (ActionParam) objectMapper.readValue(actionParamJson, clazz);
        } catch (Exception e) {
            throw new SQLException("Error deserializing ActionParam", e);
        }
    }

    private Class<?> getActionParamClass(String actionType) {
        // 根据 actionType 返回对应的类
        if ("buy_badge".equals(actionType)) {
            return BuyBadgeActionParam.class;
        } else if ("daily_comment".equals(actionType)) {
            return DailyCommentActionParam.class;
        } else if ("complete_course".equals(actionType)) {
            return CompleteCourseActionParam.class;
        }
        throw new IllegalArgumentException("Unknown actionType: " + actionType);
    }


//    @Override
//    public ActionParam getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        String json = rs.getString(columnName);
//        try {
//            return json == null ? null : objectMapper.readValue(json, ActionParam.class);
//        } catch (JsonProcessingException e) {
//            throw new InternalException("",e);
//        }
//    }

    @Override
    public ActionParam getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        try {
            return json == null ? null : objectMapper.readValue(json, ActionParam.class);
        } catch (JsonProcessingException e) {
            throw new InternalException("",e);
        }
    }

    @Override
    public ActionParam getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        try {
            return json == null ? null : objectMapper.readValue(json, ActionParam.class);
        } catch (JsonProcessingException e) {
            throw new InternalException("",e);
        }
    }



//    private ActionParam deserialize(String json) {
//        try {
//            JsonNode node = objectMapper.readTree(json);
//            String actionType = node.get("actionType").asText(); // 获取 actionType
//            ConsumeRecord.ConsumeActionType type = ConsumeRecord.ConsumeActionType.valueOf(actionType);
//            return (ActionParam) objectMapper.readValue(json, type.getActionParamType());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to deserialize actionParam JSON", e);
//        }
//    }
}
