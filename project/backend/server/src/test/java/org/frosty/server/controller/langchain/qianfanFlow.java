package org.frosty.server.controller.langchain;




// <dependencies>
// <dependency>
//   <groupId>junit</groupId>
//   <artifactId>junit</artifactId>
//   <version>3.8.1</version>
//   <scope>test</scope>
// </dependency>
// <dependency>
//   <groupId>com.squareup.okhttp3</groupId>
//   <artifactId>okhttp</artifactId>
//   <version>3.14.9</version>
// </dependency>
// <dependency>
//   <groupId>org.json</groupId>
//   <artifactId>json</artifactId>
//   <version>20210307</version>
// </dependency>
// </dependencies>


import com.sun.net.httpserver.*;
        import java.io.*;
        import java.net.InetSocketAddress;
import okhttp3.*;
        import org.json.JSONObject;


public class qianfanFlow
{
    public static final String API_KEY = "ZeFCBlc7al4pC3vgEzDx9DGV";
    public static final String SECRET_KEY = "00XhKtdc4hxWAO3pkPQiONh3NetC43yL";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/eb_stream", new StreamHandler());
        server.setExecutor(null);
        server.start();
    }

    static String getAccessToken() throws IOException {
        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

    static class StreamHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // 读取body中的prompt入参
            InputStreamReader bodyReader = new InputStreamReader(exchange.getRequestBody());
            BufferedReader br = new BufferedReader(bodyReader);
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                strBuilder.append(line);
            }
            br.close();
            JSONObject body = new JSONObject(strBuilder.toString());
            String prompt = body.getString("prompt");

            // 发起流式请求
            OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody ebRequestBody = RequestBody.create(mediaType, "{\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}],\"stream\":true}");
            String source = "&sourceVer=0.0.1&source=app_center&appName=streamDemo";
            // 大模型接口URL
            String baseUrl = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-lite-8k";
            Request request = new Request.Builder()
                    .url(baseUrl + "?access_token=" + getAccessToken() + source)
                    .method("POST", ebRequestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();

            OutputStream outputStream = exchange.getResponseBody();
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");  // 允许跨域
            exchange.sendResponseHeaders(200, 0);
            // 流式返回
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream inputStream = responseBody.byteStream();
                    String rawData = "";
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        String data = new String(buffer, 0, bytesRead);
                        String[] lines = data.split("\n");
                        for (String li : lines) {
                            if (li.startsWith("data: ")) {
                                if (rawData != "") {
                                    outputStream.write(rawData.substring(6).getBytes());
                                    outputStream.flush();
                                }
                                rawData = li;
                            } else {
                                rawData = rawData + li;
                            }
                        }
                    }
                }
            } else {
                System.out.println("流式请求异常: " + response);
            }
            outputStream.close();
            exchange.close();
        }
    }
}