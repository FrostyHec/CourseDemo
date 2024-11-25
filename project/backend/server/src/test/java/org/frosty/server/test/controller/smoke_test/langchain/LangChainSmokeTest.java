package org.frosty.server.test.controller.smoke_test.langchain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.entity.bo.User;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.langchain.LangChainAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IdempotentControllerTest
public class LangChainSmokeTest {
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private LangChainAPI langChainAPI;

    @Test
    public void testLangChainCRUD() throws Exception {
        // Setup: Authenticate and get token
        var name = "testUser";
        var res = authAPI.quickAddUserAndLogin(name, User.Role.student);
        var token = res.first;
        var uid = res.second.getUserId();



        // STEP1: 用户新建一个聊天
        // Test: Create a new chat
        LangchainController.TitleEntity titleEntity = new LangchainController.TitleEntity("Sample Chat");
        langChainAPI.createChatSuccess(token, titleEntity);

        // Verify: Get all chats and ensure the chat exists
        var chatMetadataList = langChainAPI.getAllChatMetadataSuccess(token);
        assert chatMetadataList.size() == 1;

        // Verify: Check if the title is correct
        var chatEntity = chatMetadataList.get(0);
        assert chatEntity.getTitle().equals("Sample Chat");

        //  STEP2: 发送消息
        // 原始JSON字符串
        String json = "{\"messages\":[{\"role\":\"user\",\"content\":\"你好\"},"
                + "{\"role\":\"assistant\",\"content\":\"你好，请问有什么我可以帮助你的吗？无论是回答问题、提供建议，还是其他任何需求，我都会尽力提供帮助。请告诉我你的具体需求或问题。\"}," +
                "{\"role\":\"user\",\"content\":\"自我介绍\"}]}";

        // 将JSON字符串转为ChatContext对象
        ObjectMapper objectMapper = new ObjectMapper();
        LangchainController.ChatContext chatContext = objectMapper.readValue(json, LangchainController.ChatContext.class);
//        langChainAPI.saveChatHistorySuccess(token, chatContext,1L);
        var chatContext1 = langChainAPI.sendChatSuccess(token,chatContext);

        System.out.println("---------------------");
        System.out.println(chatContext1);
        System.out.println("---------------------");

        //  STEP3: 保存聊天记录
        langChainAPI.saveChatHistorySuccess(token, chatContext1,1L);

        //  STEP4: 获取某条聊天记录
        var chatContext2  = langChainAPI.getChatContentSuccess(token,1L); // 先这么写id
//        System.out.println("---------------------");
//        System.out.println(chatContext2);
//        System.out.println("---------------------");
        assert chatContext2.getMessages().equals(chatContext1.getMessages());

        //  STEP5: 获取我的全部聊天记录

        // Test: Update chat title
        chatEntity.setTitle("Updated Chat Title");
        langChainAPI.updateChatTitleSuccess(token, chatEntity.getId(), new LangchainController.TitleEntity("Updated Chat Title"));

        // Verify: Get updated chat and check title
        var updatedChatEntity = langChainAPI.getAllChatMetadataSuccess(token);
        assert updatedChatEntity.size() == 1;
        assert updatedChatEntity.get(0).getTitle().equals("Updated Chat Title");


    }
}
