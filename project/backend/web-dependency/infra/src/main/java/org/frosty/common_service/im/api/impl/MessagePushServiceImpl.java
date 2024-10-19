package org.frosty.common_service.im.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.common.utils.RestTemplateUtils;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.common_service.im.entity.Email;
import org.frosty.common_service.im.entity.SiteMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public SiteMessage pushSite(SiteMessage siteMessage) {
        ResponseEntity<Response> res =
                restTemplate.postForEntity(siteMsgPath + "/msg/site", siteMessage, Response.class);
        Object body = RestTemplateUtils.checkSuccess(res,
                "Failed to connect to site dispatcher.",
                "Exception thrown by server.");
        Long mid = (Long) objectMapper.convertValue(body, Map.class).get("mid");
        siteMessage.setMessageId(mid);
        return siteMessage;
    }

    @Override
    public void ackSite(Long mid) {
        Response res = restTemplate.patchForObject(siteMsgPath + "/msg/site/ack/" + mid, null, Response.class);
        Ex.check(Objects.requireNonNull(res).getCode()==Response.getSuccess().getCode(),
                new InternalException("Exception thrown by server. Response:"+res));
    }

    @Override
    public void pushEmail(Email email) {

    }
}
