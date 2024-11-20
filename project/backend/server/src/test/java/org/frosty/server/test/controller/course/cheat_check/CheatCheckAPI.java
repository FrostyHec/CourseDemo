package org.frosty.server.test.controller.course.cheat_check;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.utils.ReflectionUtils;
import org.frosty.server.controller.course.cheat_check.CheatCheckController;
import org.frosty.server.entity.bo.cheat_check.VideoRequiredSeconds;
import org.frosty.server.entity.bo.cheat_check.VideoWatchRecord;
import org.frosty.server.services.course.cheat_check.CheatCheckService;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Component
@RequiredArgsConstructor
public class CheatCheckAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final String baseUrl = PathConstant.API + "resource/{id}/watch";

    public void resetTimeConfig(int recordAliveSeconds, int heartBeatIntervalSeconds) {
        // 0. 通过反射设置RECORD_ALIVE_SECONDS和HEART_BEAT_INTERVAL_SECONDS
        try {
            ReflectionUtils.modifyStaticFinalField(CheatCheckService.class, "RECORD_ALIVE_SECONDS", recordAliveSeconds);
            ReflectionUtils.modifyStaticFinalField(CheatCheckService.class, "HEART_BEAT_INTERVAL_SECONDS", heartBeatIntervalSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultActions setMinRequiredTime(String token, Long id, VideoRequiredSeconds videoRequiredSeconds) throws Exception {
        String url = baseUrl.replace("{id}", id.toString()) + "/required-time";
        String json = objectMapper.writeValueAsString(videoRequiredSeconds);
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void setMinRequiredTimeSuccess(String token, Long id, VideoRequiredSeconds videoRequiredSeconds) throws Exception {
        setMinRequiredTime(token, id, videoRequiredSeconds)
                .andExpect(RespChecker.success());
    }

    public ResultActions getMinRequiredTime(String token, Long id) throws Exception {
        String url = baseUrl.replace("{id}", id.toString()) + "/required-time";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public VideoRequiredSeconds getMinRequiredTimeSuccess(String token, Long id) throws Exception {
        var resp = getMinRequiredTime(token, id)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp,VideoRequiredSeconds.class);
    }

    public ResultActions getLastWatched(String token, Long id) throws Exception {
        String url = baseUrl.replace("{id}", id.toString()) + "/last";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public VideoWatchRecord getLastWatchedSuccess(String token, Long id) throws Exception {
        var resp = getLastWatched(token, id)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        return JsonUtils.toObject(resp, VideoWatchRecord.class);
    }

    public ResultActions startWatchAlive(String token, Long id) throws Exception {
        String url = baseUrl.replace("{id}", id.toString()) + "/start";
        return mockMvc.perform(MockMvcRequestBuilders.put(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void startWatchAliveSuccess(String token, Long id) throws Exception {
        startWatchAlive(token, id)
                .andExpect(RespChecker.success());
    }

    public ResultActions keepWatchAlive(String token, Long id) throws Exception {
        String url = baseUrl.replace("{id}", id.toString()) + "/alive";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public void keepWatchAliveSuccess(String token, Long id) throws Exception {
        keepWatchAlive(token, id)
                .andExpect(RespChecker.success());
    }

    public ResultActions stopWatchAlive(String token, Long id, CheatCheckController.WatchedInfoEntity watchedInfoEntity) throws Exception {
        String url = baseUrl.replace("{id}", id.toString()) + "/stop";
        String json = objectMapper.writeValueAsString(watchedInfoEntity);
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void stopWatchAliveSuccess(String token, Long id, CheatCheckController.WatchedInfoEntity watchedInfoEntity) throws Exception {
        stopWatchAlive(token, id, watchedInfoEntity)
                .andExpect(RespChecker.success());
    }
}
