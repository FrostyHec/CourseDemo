//package org.frosty.sse_push.handler;
//
//import org.frosty.common.exception.InternalException;
//
//import java.util.Map;
//
//
//public enum DynamicTableNameType {
//    NO,
//    MESSAGE_DTO;
//
//    public static String getName(String tableName, Map<String, Object> requestData) {
//        DynamicTableNameType type = (DynamicTableNameType) requestData.get("type");
//        String name = (String) requestData.get("name");
//        switch (type) {
//            case NO -> {
//                return tableName;
//            }
//            case MESSAGE_DTO -> {
//                return tableName + "_" + name;
//            }
//        }
//        throw new InternalException(
//                "unknown table type:" + requestData + "table name:" + tableName);
//    }
//}
