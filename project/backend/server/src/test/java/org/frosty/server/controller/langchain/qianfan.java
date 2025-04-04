package org.frosty.server.controller.langchain;


import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;

import okhttp3.*;
import org.json.JSONObject;


public class qianfan {
    public static final String API_KEY = "ZeFCBlc7al4pC3vgEzDx9DGV";
    public static final String SECRET_KEY = "00XhKtdc4hxWAO3pkPQiONh3NetC43yL";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String[] args) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"messages\":[{\"role\":\"user\",\"content\":\"你好\"}," +
                "{\"role\":\"assistant\",\"content\":\"你好，请问有什么我可以帮助你的吗？无论是回答问题、提供建议，还是其他任何需求，我都会尽力提供帮助。请告诉我你的具体需求或问题。\"}," +
                "{\"role\":\"user\",\"content\":\"自我介绍一下\"}" +
                "]}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-speed-pro-128k?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
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
