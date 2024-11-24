package org.frosty.server.services.langchain;

import okhttp3.*;
import org.frosty.server.controller.langchain.LangchainController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private LangchainService langchainService;

    private static final String API_KEY = "ZeFCBlc7al4pC3vgEzDx9DGV";
    private static final String SECRET_KEY = "00XhKtdc4hxWAO3pkPQiONh3NetC43yL";
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public LangchainController.ChatContext chatWithModel(LangchainController.ChatContext chatContext) throws IOException {
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new JSONObject(chatContext).toString());
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = HTTP_CLIENT.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code: " + response);
        }

        // 解析响应体
        String responseBody = response.body().string();
        JSONObject responseJson = new JSONObject(responseBody);
        String result = responseJson.optString("result", ""); // 提取 result 字段

        // 将 result 封装到新的消息对象
        LangchainController.SingleChatMessage replyMessage =
                new LangchainController.SingleChatMessage(LangchainController.SingleChatMessage.Role.assistant,result);

        // 添加到 chatContext 的消息列表中
        chatContext.getMessages().add(replyMessage);

        return chatContext;
    }

    public void streamChatWithModel(LangchainController.ChatContext chatContext, OutputStream outputStream) throws IOException {
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, new JSONObject(chatContext).put("stream", true).toString());

        String baseUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro";
        Request request = new Request.Builder()
                .url(baseUrl + "?access_token=" + accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = HTTP_CLIENT.newCall(request).execute();
        if (response.isSuccessful()) {
            try (InputStream inputStream = response.body().byteStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                }
            }
        } else {
            throw new IOException("Stream request failed: " + response);
        }
    }

//    private LangchainController.ChatContext constructChatContext(List<LangchainController.SingleChatMessage> chatHistory, String inputMessage) {
//        LangchainController.ChatContext context = new LangchainController.ChatContext();
//        List<LangchainController.SingleChatMessage> messages = new ArrayList<>();
//
//        // Convert existing chat history to messages
//        for (LangchainController.SingleChatMessage entity : chatHistory) {
//            messages.add(new LangchainController.SingleChatMessage("user", entity.getTitle())); // Example: "title" as message content
//        }
//
//        // Append new user input message
//        messages.add(new LangchainController.SingleChatMessage("user", inputMessage));
//
//        context.setMessages(messages);
//        return context;
//    }

    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }
}

