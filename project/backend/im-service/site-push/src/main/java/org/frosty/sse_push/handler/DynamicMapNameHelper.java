//package org.frosty.sse_push.handler;
//
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//
//import java.util.Map;
//
//public class DynamicMapNameHelper {
//    /**
//     * 请求参数存取
//     */
//    private static final ThreadLocal<Map<String, Object>> REQUEST_DATA = new ThreadLocal<>();
//
//    /**
//     * 获取请求参数
//     *
//     * @param param 请求参数
//     * @return 请求参数 MAP 对象
//     */
//    public static <T> T getRequestData(String param) {
//        Map<String, Object> dataMap = getRequestData();
//        if (CollectionUtils.isNotEmpty(dataMap)) {
//            return (T) dataMap.get(param);
//        }
//        return null;
//    }
//
//    /**
//     * 获取请求参数
//     *
//     * @return 请求参数 MAP 对象
//     */
//    public static Map<String, Object> getRequestData() {
//        return REQUEST_DATA.get();
//    }
//
//    /**
//     * 设置请求参数
//     *
//     * @param requestData 请求参数 MAP 对象
//     */
//    public static void setRequestData(Map<String, Object> requestData) {
//        REQUEST_DATA.set(requestData);
//    }
//
//    public static void setMessageDTO(String name) {
//        REQUEST_DATA.set(Map.of("type", DynamicTableNameType.MESSAGE_DTO, "name", name));
//    }
//
//    public static void setDefault() {
//        REQUEST_DATA.set(Map.of("type", DynamicTableNameType.NO, "name", ""));
//    }
//}