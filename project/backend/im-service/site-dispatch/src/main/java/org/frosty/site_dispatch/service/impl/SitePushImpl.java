package org.frosty.site_dispatch.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.frosty.common.response.ResponseCodeType;
import org.frosty.common.utils.Ex;
import org.frosty.site_dispatch.service.SitePush;
import org.frosty.sse.entity.SiteMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SitePushImpl implements SitePush {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void pushMessage(String handlerIp, SiteMessage msg) {
        String url = "http://" + handlerIp + PathConstant.INTERNAL_API + "/msg/site/push";
        var response = restTemplate.postForEntity(url, objectMapper.valueToTree(msg), Response.class);
        Ex.check(response.getStatusCode() == HttpStatus.OK
                        && Objects.nonNull(response.getBody())
                        && response.getBody().getCode() == ResponseCodeType.SUCCESS.getCode(),
                new InternalException("unknown-push-failure",
                        new RuntimeException(response.toString())));

    }
}
