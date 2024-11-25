package org.frosty.server.test.controller.langchain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.services.langchain.LangchainService;
import org.frosty.server.test.controller.auth.AuthUtil;
import org.frosty.test_common.utils.JsonUtils;
import org.frosty.test_common.utils.RespChecker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LangChainAPI {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;
    private final LangchainService langchainService;
    private final String langchainBaseUrl = PathConstant.API + "/langchain";

    public LangchainController.ChatEntity getTemplateChatEntity() {
        LangchainController.ChatEntity chatEntity = new LangchainController.ChatEntity();
        chatEntity.setTitle("Sample Chat");
        chatEntity.setCreatedAt(OffsetDateTime.now());
        chatEntity.setUpdatedAt(OffsetDateTime.now());
        return chatEntity;

    }

    public ResultActions sendChat(String token, LangchainController.ChatContext chatContext) throws Exception {
        String url = langchainBaseUrl + "/chat";
        String json = objectMapper.writeValueAsString(chatContext);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public LangchainController.ChatContext sendChatSuccess(String token, LangchainController.ChatContext chatContext) throws Exception {
        var resp = sendChat(token, chatContext)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, LangchainController.ChatContext.class);

    }


    public ResultActions saveChatHistory(String token, LangchainController.ChatContext chatContext) throws Exception {
        String url = langchainBaseUrl + "/";
        String json = objectMapper.writeValueAsString(chatContext);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void saveChatHistorySuccess(String token, LangchainController.ChatContext chatContext) throws Exception {
        saveChatHistory(token, chatContext)
                .andExpect(RespChecker.success());
    }


    public ResultActions createChat(String token, LangchainController.TitleEntity titleEntity) throws Exception {
        String url = langchainBaseUrl + "/";
        String json = objectMapper.writeValueAsString(titleEntity);
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void createChatSuccess(String token, LangchainController.TitleEntity titleEntity) throws Exception {
        createChat(token, titleEntity)
                .andExpect(RespChecker.success());
    }

    public ResultActions getChatMetadata(String token) throws Exception {
        String url = langchainBaseUrl + "/all";
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public List<LangchainController.ChatEntity> getAllChatMetadataSuccess(String token) throws Exception {
        var resp = getChatMetadata(token)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, LangchainController.ChatMetadataList.class).getChatHistory();
    }

    public ResultActions getChatContent(String token, Long chatId) throws Exception {
        String url = langchainBaseUrl + "/" + chatId;
        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .headers(authUtil.setAuthHeader(token))
                .accept(MediaType.APPLICATION_JSON));
    }

    public LangchainController.ChatContext getChatContentSuccess(String token, Long chatId) throws Exception {
        var resp = getChatContent(token, chatId)
                .andExpect(RespChecker.success())
                .andReturn();
        return JsonUtils.toObject(resp, LangchainController.ChatContext.class);
    }

    public ResultActions updateChatTitle(String token, Long chatId, LangchainController.TitleEntity titleEntity) throws Exception {
        String url = langchainBaseUrl + "/" + chatId + "/title";
        String json = objectMapper.writeValueAsString(titleEntity);
        return mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .headers(authUtil.setAuthHeader(token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON));
    }

    public void updateChatTitleSuccess(String token, Long chatId, LangchainController.TitleEntity titleEntity) throws Exception {
        updateChatTitle(token, chatId, titleEntity)
                .andExpect(RespChecker.success());
    }
}
