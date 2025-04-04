package org.frosty.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.entity.bo.market.action_type.ActionParam;

import java.sql.*;

//@MappedJdbcTypes(JdbcType.OTHER)
//@MappedTypes(ActionParam.class)
public class ActionParamTypeHandler extends BaseTypeHandler<ActionParam> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ActionParam parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setObject(i, objectMapper.writeValueAsString(parameter), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize ActionParam", e);
        }
    }

    @Override
    public ActionParam getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 获取 actionParam 的 JSON 字符串
        String actionParamJson = rs.getString(columnName);
        if (actionParamJson == null || actionParamJson.isEmpty()) {
            return null;
        }

        // 获取 actionType 字段
        String actionType = rs.getString("action_type");
        if (actionType == null || actionType.isEmpty()) {
            throw new SQLException("Missing action_type column in ResultSet");
        }

        try {
            // 根据 actionType 获取对应的类
            Class<?> actionParamClass = getActionParamClass(actionType);
            return (ActionParam) objectMapper.readValue(actionParamJson, actionParamClass);
        } catch (Exception e) {
            throw new SQLException("Error deserializing ActionParam", e);
        }
    }

    @Override
    public ActionParam getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getNullableResult by columnIndex is not supported");
    }

    @Override
    public ActionParam getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        throw new UnsupportedOperationException("getNullableResult by CallableStatement is not supported");
    }

    private Class<?> getActionParamClass(String actionType) {
        // 根据 actionType 获取对应的类
        for (ConsumeRecord.ConsumeActionType type : ConsumeRecord.ConsumeActionType.values()) {
            if (type.name().equals(actionType)) {
                return type.getActionParamType();
            }
        }
        throw new IllegalArgumentException("Unknown actionType: " + actionType);
    }
}
