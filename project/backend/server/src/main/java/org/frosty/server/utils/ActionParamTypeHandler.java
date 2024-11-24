package org.frosty.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.frosty.common.exception.InternalException;
import org.frosty.server.entity.bo.market.action_type.ActionParam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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
        String json = rs.getString(columnName);
        try {
            return json == null ? null : objectMapper.readValue(json, ActionParam.class);
        } catch (JsonProcessingException e) {
            throw new InternalException("",e);
        }
    }

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
}
