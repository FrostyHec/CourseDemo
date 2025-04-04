package org.frosty.common_service.im.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.common.utils.RestTemplateUtils;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.common_service.im.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.frosty.sse.entity.SiteMessage;
import java.util.Map;
import java.util.Objects;

public class MessagePushServiceImpl implements MessagePushService {
    @Value("${api.message.site.url}")
    private String siteMsgPath;
    @Value("${api.message.email.url}")
    private String emailMsgPath;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 推送站内消息
     *
     * @param siteMessage 站内消息对象
     * @return 更新后的站内消息对象
     * @throws InternalException 如果连接站内消息分发器失败或服务器抛出异常
     */
    @Override
    public SiteMessage pushSite(SiteMessage siteMessage) {
        String url = siteMsgPath + PathConstant.INTERNAL_API + "/msg/site";
        var map = objectMapper.convertValue(siteMessage, Map.class);
        ResponseEntity<Response> res =
                restTemplate.postForEntity(url,map, Response.class);
        Object body = RestTemplateUtils.checkSuccess(res,
                "Failed to connect to site dispatcher.",
                "Exception thrown by server.");
        Long mid = (Long) objectMapper.convertValue(body, Map.class).get("mid");
        siteMessage.setMessageId(mid);
        return siteMessage;
    }

    /**
     * 确认站内消息
     *
     * @param mid 消息ID
     * @throws InternalException 如果服务器抛出异常
     */
    @Override
    public void ackSite(Long mid) {
        String url = siteMsgPath + PathConstant.INTERNAL_API + "/msg/site/ack/" + mid;
        Response res = restTemplate.patchForObject(url, null, Response.class);
        Ex.check(Objects.requireNonNull(res).getCode() == Response.getSuccess().getCode(),
                new InternalException("Exception thrown by server. Response:" + res));
    }

    /**
     * 推送邮件
     *
     * @param email 邮件对象
     * @throws InternalException 如果连接邮件服务失败或邮件服务返回错误响应
     */
    @Override
    public void pushEmail(Email email) {
        String url = emailMsgPath + PathConstant.INTERNAL_API + "/msg/email";
        var res = restTemplate.postForEntity(url, email, Response.class);
        RestTemplateUtils.checkSuccess(res, "Connect to Email Service failed",
                "Error response from Email Service");
    }
}
