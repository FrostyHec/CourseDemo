package org.frosty.server.controller.langchain;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.entity.bo.langchain.ChatHistory;
import org.frosty.server.services.langchain.ChatService;
import org.frosty.server.services.langchain.LangchainService;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(PathConstant.API + "/langchain")
@RequiredArgsConstructor
public class LangchainController {
    private final LangchainService langchainService;

    private final ChatService chatService;
    
    @PostMapping("/chat")
    public ChatContext sendChat(@RequestBody ChatContext context, @GetToken TokenInfo tokenInfo) {
        try {
            // 调用服务层进行处理
            return chatService.chatWithModel(context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/chat/flow")
    public void sendChatAndGetFlow(@RequestBody ChatContext context, HttpServletResponse response) {
        // 依据输入的上下文返回chat的回复。流式返回
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            OutputStream outputStream = response.getOutputStream();
            chatService.streamChatWithModel(context, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/title")
    public TitleEntity generateTitle(@RequestBody ChatContext chatContext) {
        // TODO 依据输入的chatContext生成title
        FrameworkUtils.notImplemented();
        return null;
    }

    @PostMapping("/")
    public ChatEntity createNewChat(@GetToken TokenInfo tokenInfo, @RequestBody TitleEntity titleEntity) {
        // 依据输入的chatEntity返回title
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setTitle(titleEntity.getTitle());

        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode emptyContext = objectMapper.valueToTree(Map.of());// 创建一个空的 JSON 对象
        ChatContext emptyContext = new ChatContext();

                ChatHistory chatHistory = new ChatHistory()
                .setUserId(tokenInfo.getAuthInfo().getUserID())
                .setTitle(titleEntity.getTitle())
                .setContext(emptyContext);
        langchainService.createNewChat(chatHistory);
        return chatEntity;
    }

    @PutMapping("/{id}")
    public void saveChatHistory(@RequestBody ChatContext context, @PathVariable Long id) {
        // 保存context到数据库
        langchainService.saveChatHistory(context, id);
    }

    @GetMapping("/all")
    public ChatMetadataList getAllMyChatMetadata(@GetToken TokenInfo tokenInfo) {
        // 返回我的所有的chat的metadata
        ChatMetadataList chatMetadataList = new ChatMetadataList();
        chatMetadataList.chatHistory = langchainService.getAllMyChatMetadata(tokenInfo.getAuthInfo().getUserID());
        return chatMetadataList;
    }

    @GetMapping("/{id}")
    public ChatContext getChatContent(@GetToken TokenInfo tokenInfo, @PathVariable Long id) {
        // 返回该id下所有chat的历史记录
        ChatContext context = langchainService.getChatContentById(id);
        return context;
    }

    @PatchMapping("/{id}/title")
    public ChatEntity setChatTitle(@RequestBody TitleEntity titleEntity, @PathVariable Long id) {
        // 设置chat标题并返回更新后的chat
        langchainService.setChatTitle(titleEntity.getTitle(), id);
        return langchainService.getChatEntityById(id);
    }

    @DeleteMapping("/{id}")
    public void setChatTitle(@PathVariable Long id) {
        // 设置chat标题并返回更新后的chat
        langchainService.deleteChat(id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatMetadataList {
        private List<ChatEntity> chatHistory;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TitleEntity {
        private String title;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatEntity {
        private Long id;
        private String title;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SingleChatMessage {
        Role role;
        String content;

        public enum Role {
            user,
            assistant
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatContext {
        private List<SingleChatMessage> messages;
    }


}
