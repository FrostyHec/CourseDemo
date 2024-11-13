package org.frosty.site_dispatch.testcode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.config.AuthConstant;
import org.frosty.auth.mock.AuthMockUtils;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.response.Response;
import org.frosty.sse.entity.SiteMessage;
import org.frosty.sse.entity.SiteMessage.MessageType;
import org.frosty.sse.entity.SiteMessagePacketDTO;
import org.frosty.sse.entity.SitePushDTO;
import org.frosty.sse.entity.SitePushDTO.PushType;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SiteMsgAPI {
    //    private final UserMapper mapper;
    private final WebClient.Builder webClientBuilder;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final String pushIp = "localhost:9973";

//    public long addUser(String name) {
//        var user = new UserEntity(null, name, "");
//        mapper.insert(user);
//        return user.getId();
//    }

    public Flux<Response> register(long id) {
        WebClient webClient = webClientBuilder.baseUrl("http://" + pushIp).build();
        return webClient.get()
                .uri(PathConstant.API + "/msg/site/user/" + id)
                .header(AuthConstant.parsedHeader, AuthMockUtils.getPassTokenInfoString(id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    // 处理错误状态
                    return Mono.error(new RuntimeException(
                            "Failed to connect: " + response));
                })
                .bodyToFlux(Response.class);
    }


    public ResultActions push(SiteMessage dto) throws Exception {
        String requestBody = objectMapper.writeValueAsString(dto);
        String baseUrl = PathConstant.INTERNAL_API + "/msg/site";
        return mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

    }

    public long pushSuccess(SiteMessage dto) throws Exception {
        var res = push(dto)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toJsonData(res).get("mid").asLong();
    }

    public ResultActions ack(long mid) throws Exception {
        String baseUrl = PathConstant.INTERNAL_API + "/msg/site/ack/" + mid;
        return mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl)
                .accept(MediaType.APPLICATION_JSON));

    }

    public void ackSuccess(long mid) throws Exception {
        ack(mid)
                .andExpect(RespChecker.success());
    }

    public SiteMessage getSimpleNewMessage(long toId, boolean requiredAck) {
        var dto = new SiteMessage();
        dto.setToId(toId);
        dto.setRequiredAck(requiredAck);
        dto.setType(MessageType.NEW);
        Map<String, String> map = Map.of("msg", "this is testing message");
        dto.setBody(objectMapper.valueToTree(map));
        return dto;
    }

    public SiteMessage getSimpleNewMessage(long toId, boolean requiredAck, String msg) {
        var dto = new SiteMessage();
        dto.setToId(toId);
        dto.setRequiredAck(requiredAck);
        dto.setType(MessageType.NEW);
        Map<String, String> map = Map.of("msg", msg);
        dto.setBody(objectMapper.valueToTree(map));
        return dto;
    }

    public void checkMessage(SiteMessage rcvd, SiteMessage expected, Long mid,
                             MessageType type) {
        if (mid == null) {
            assert Objects.equals(rcvd.getMessageId(), expected.getMessageId());
        } else {
            assert Objects.equals(rcvd.getMessageId(), mid);
        }
        assert Objects.equals(rcvd.getFromId(), expected.getFromId());
        assert Objects.equals(rcvd.getToId(), expected.getToId());
        if (type == null) {
            assert Objects.equals(rcvd.getType(), expected.getType());
        } else {
            assert Objects.equals(rcvd.getType(), type);
        }
        assert Objects.equals(rcvd.isRequiredAck(), expected.isRequiredAck());
        assert Objects.equals(rcvd.getBody(), expected.getBody());
    }

    public SiteMessage getRcvdSingleMessage(Response resp) {
        try {
            var dto = JsonUtils.dataCast(resp.getData(), SitePushDTO.class);
            assert dto.getPushType() == PushType.SINGLE;
            return JsonUtils.dataCast(dto.getBody(), SiteMessage.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SiteMessagePacketDTO getRcvdPacketMessage(Response resp) {
        assert resp.getCode() == Response.getSuccess().getCode();
        try {
            var dto = JsonUtils.dataCast(resp.getData(), SitePushDTO.class);
            assert dto.getPushType() == PushType.PACKET;
            return JsonUtils.dataCast(dto.getBody(), SiteMessagePacketDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
