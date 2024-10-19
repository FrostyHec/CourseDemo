package org.frosty.site_dispatch.outerapi.impl;

import java.util.Map;
import java.util.Objects;

import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.frosty.common.response.ResponseCodeType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frosty.site_dispatch.entity.SingleMessageDTO;
import org.frosty.site_dispatch.outerapi.SitePush;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SitePushImpl implements SitePush {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public long pushMessage(String handlerIp, SingleMessageDTO msg) {
        String url = "http://" + handlerIp + PathConstant.INTERNAL_API + "/msg/site/push";
        var response = restTemplate.postForEntity(url,objectMapper.valueToTree(msg) , Response.class);
        if (response.getStatusCode() != HttpStatus.OK
            || Objects.isNull(response.getBody())
            || response.getBody().getCode() != ResponseCodeType.SUCCESS.getCode()) {
            //如果客户端取消连接,则push pod会删除sse，并将消息重新塞回unposed,所以不会有响应错误
            throw new InternalException("unknown-push-failure",
                                        new RuntimeException(response.toString()));
        }
        return ((Map<String, Long>) response.getBody().getData()).get("mid");
    }
}
