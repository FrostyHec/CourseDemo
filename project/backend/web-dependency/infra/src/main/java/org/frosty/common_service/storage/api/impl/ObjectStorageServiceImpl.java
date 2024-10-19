package org.frosty.common_service.storage.api.impl;

import jakarta.annotation.PostConstruct;
import okhttp3.MultipartBody;
import org.frosty.common.response.ResponseCodeType;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import java.util.Objects;

@Component
public class ObjectStorageServiceImpl implements ObjectStorageService {
    @Value("${api.storage.url}")
    private String path;
    @Autowired
    private RestTemplate restTemplate;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(path) // 替换为后端B的URL
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getBaseUrl(String key) {
        return path + PathConstant.INTERNAL_API + "/storage/" + key;
    }

    @Override
    public void save(String key, Object value) {
        byte[] bytes;
        if (value instanceof byte[]) {
            bytes = (byte[]) value;
        } else {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(value);
                oos.flush();
                bytes = bos.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert object to MultipartFile", e);
            }
        }
        flowSave(key, new ByteArrayInputStream(bytes));
    }

    @Override
    public void flowSave(String key, InputStream flow) {
        String url = getBaseUrl(key);
        Flux<DataBuffer> dataBufferFlux = DataBufferUtils.readInputStream(
                () -> flow,
                new DefaultDataBufferFactory(),
                4096 // TODO CHECK SIZE AT HERE
        );

        webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(BodyInserters.fromDataBuffers(dataBufferFlux))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), (e) -> {
                    throw new InternalException("failure when uploading data:" + e);
                })
                .bodyToMono(Response.class)
                .doOnNext(response -> {
                    Ex.check(response.getCode() == ResponseCodeType.SUCCESS.getCode(),
                            new RuntimeException("Unexpected response:" + response));
                })
                .block();
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        String url = getBaseUrl(key);
        ResponseEntity<byte[]> resp = restTemplate.getForEntity(url, byte[].class);
        Ex.check(resp.getStatusCode().is2xxSuccessful(),
                new InternalException("get object failed" + resp));

        byte[] body = resp.getBody();
        Ex.check(body != null,
                new InternalException("Received empty response body for key: " + key));
        if (type == byte[].class) {
            return (T) body;
        }
        try (ByteArrayInputStream bais = new ByteArrayInputStream(body);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return type.cast(ois.readObject());
        } catch (Exception e) {
            throw new InternalException("Failed to deserialize object", e);
        }
    }

    @Override
    public InputStream flowGet(String key) {
        String url = getBaseUrl(key);

        Flux<DataBuffer> dataBufferFlux = webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        response -> Mono.error(new RuntimeException("Failed to download file with status code: " + response)))
                .bodyToFlux(DataBuffer.class);

        ReadableByteChannel channel = new ReadableByteChannel() {
            private final java.util.Iterator<DataBuffer> iterator = dataBufferFlux.toIterable().iterator();
            private boolean open = true;

            @Override
            public int read(java.nio.ByteBuffer dst) {
                if (!iterator.hasNext()) {
                    return -1;
                }
                DataBuffer dataBuffer = iterator.next();
                int readBytes = dataBuffer.readableByteCount();
                dst.put(dataBuffer.asByteBuffer());
                DataBufferUtils.release(dataBuffer);
                return readBytes;
            }

            @Override
            public boolean isOpen() {
                return open;
            }

            @Override
            public void close() {
                open = false;
            }
        };

        return Channels.newInputStream(channel);
    }

    @Override
    public void delete(String key) {
        String url = getBaseUrl(key);
        ResponseEntity<Response> resp = restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, null, Response.class);
        Ex.check(Objects.requireNonNull(resp.getBody()).getCode() == 200,
                new InternalException("delete object failed" + resp));
    }

    @Override
    public boolean exist(String key) {
        String url = getBaseUrl(key) + "/exists";
        ResponseEntity<Response> resp = restTemplate.getForEntity(url, Response.class);
        Ex.check(Objects.requireNonNull(resp.getBody()).getCode() == 200,
                new InternalException("check object exist failed" + resp));
        return ((Map<String, Boolean>) resp.getBody().getData()).get("exists");
    }

    @Override
    public String getAccessKey(String key, String caseName) {
        String url = getBaseUrl(key) + "/access-key";
        url = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("case_name", caseName)
                .toUriString();
        var resp = restTemplate.getForEntity(url, Response.class);
        Ex.check(Objects.requireNonNull(resp.getBody()).getCode() == 200,
                new InternalException("get access key failed" + resp));
        return ((Map<String, String>) resp.getBody().getData()).get("access_key");
    }

    @Override
    public void withdrawAccessKey(String key, String caseName) {
        String url = getBaseUrl(key) + "/access-key";
        url = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("case_name", caseName)
                .toUriString();
        restTemplate.delete(url);
    }
}
