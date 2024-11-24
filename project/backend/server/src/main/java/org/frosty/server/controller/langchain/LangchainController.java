package org.frosty.server.controller.langchain;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.annotation.GetToken;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.common.constant.PathConstant;
import org.frosty.server.utils.FrameworkUtils;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(PathConstant.API + "/langchain")
@RequiredArgsConstructor
public class LangchainController {
    @PostMapping("/chat")
    public ChatContext sendChat(@RequestBody ChatContext context) {
        FrameworkUtils.notImplemented();
        return null; // 依据输入的上下文返回chat的回复。非流式返回
    }

    @PostMapping("/chat/flow")
    public void sendChatAndGetFlow(@RequestBody ChatContext context, HttpServletResponse response) {
        FrameworkUtils.notImplemented();
         // 依据输入的上下文返回chat的回复。流式返回
    }

    @PostMapping("/title")
    public TitleEntity generateTitle(@RequestBody ChatContext chatContext) {
        FrameworkUtils.notImplemented();
        return null;// 依据输入的chatContext生成title
    }

    @PostMapping("/")
    public ChatEntity createNewChat(@GetToken TokenInfo tokenInfo,@RequestBody TitleEntity titleEntity) {
        FrameworkUtils.notImplemented();
        return null;// 依据输入的chatEntity返回title
    }

    @PutMapping("/{id}")
    public void saveChatHistory( @RequestBody ChatContext context, @PathVariable Long id) {
        FrameworkUtils.notImplemented();//保存context到数据库
    }

    @GetMapping("/all")
    public ChatMetadataList getAllMyChatMetadata(@GetToken TokenInfo tokenInfo) {
        FrameworkUtils.notImplemented();
        return null;//返回我的所有的chat的metadata
    }
    @GetMapping("/{id}")
    public ChatContext getChatContent(@GetToken TokenInfo tokenInfo, @PathVariable Long id) {
        FrameworkUtils.notImplemented();
        return null;//返回该id下所有chat的历史记录
    }
    @PatchMapping("/{id}/title")
    public ChatEntity setChatTitle(@RequestBody TitleEntity titleEntity, @PathVariable Long id) {
        FrameworkUtils.notImplemented();
        return null;//设置chat标题
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
        String role;
        String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatContext {
        private List<SingleChatMessage> messages;
    }
}
