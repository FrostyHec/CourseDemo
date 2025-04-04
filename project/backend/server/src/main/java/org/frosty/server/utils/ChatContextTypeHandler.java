package org.frosty.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.frosty.server.controller.langchain.LangchainController;

import java.sql.*;

/**
 * 自定义 MyBatis TypeHandler，用于处理 ChatContext 类型的字段。
 */
//@MappedJdbcTypes(JdbcType.OTHER)  // 指定数据库列的类型是 OTHER
//@MappedTypes(LangchainController.ChatContext.class)  // 指定该 TypeHandler 处理的 Java 类型
public class ChatContextTypeHandler extends BaseTypeHandler<LangchainController.ChatContext> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 将 ChatContext 转换为 JSON 字符串并设置到 PreparedStatement 中
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LangchainController.ChatContext parameter, JdbcType jdbcType) throws SQLException {
        try {
            // 将 ChatContext 转换为 JSON 字符串并设置为参数
            ps.setObject(i, objectMapper.writeValueAsString(parameter), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new SQLException("Failed to serialize ChatContext", e);
        }
    }

    // 从 ResultSet 中读取 JSON 字符串并反序列化为 ChatContext 对象
    @Override
    public LangchainController.ChatContext getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        try {
            return json == null ? null : objectMapper.readValue(json, LangchainController.ChatContext.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 从 ResultSet 中读取 JSON 字符串并反序列化为 ChatContext 对象
    @Override
    public LangchainController.ChatContext getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        try {
            return json == null ? null : objectMapper.readValue(json, LangchainController.ChatContext.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 从 CallableStatement 中读取 JSON 字符串并反序列化为 ChatContext 对象
    @Override
    public LangchainController.ChatContext getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        try {
            return json == null ? null : objectMapper.readValue(json, LangchainController.ChatContext.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
