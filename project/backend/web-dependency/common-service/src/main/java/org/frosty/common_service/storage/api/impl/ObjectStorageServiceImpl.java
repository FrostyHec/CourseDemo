package org.frosty.common_service.storage.api.impl;

import org.frosty.common.constant.PathConstant;
import org.frosty.common.exception.InternalException;
import  org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Component
public class ObjectStorageServiceImpl implements ObjectStorageService {
    @Value("${api.storage.url}")
    private String path;
    @Autowired
    private RestTemplate restTemplate;
    public String getBaseUrl(String key){
        return path + PathConstant.INTERNAL_API +"/storage/"+key;
    }
    @Override
    public void save(String key, Object value) {
        String url = getBaseUrl(key);
        ResponseEntity<Response> resp = restTemplate.postForEntity(url, value, Response.class);
        Ex.check(Objects.requireNonNull(resp.getBody()).getCode()==200,
                new InternalException("save object failed"+resp));

    }

    @Override
    public <T> T get(String key, Class<T> type) {
        String url = getBaseUrl(key);
        ResponseEntity<Response> resp = restTemplate.getForEntity(url, Response.class);
        Ex.check(Objects.requireNonNull(resp.getBody()).getCode()==200,
                new InternalException("get object failed"+resp));
        return (T) resp.getBody().getData();
    }

    @Override
    public void delete(String key) {
        String url = getBaseUrl(key);
        ResponseEntity<Response> resp = restTemplate.exchange(url, org.springframework.http.HttpMethod.DELETE, null, Response.class);
        Ex.check(Objects.requireNonNull(resp.getBody()).getCode()==200,
                new InternalException("delete object failed"+resp));
    }

    @Override
    public boolean exist(String key) {
        String url = getBaseUrl(key)+"/exists";
        ResponseEntity<Response> resp = restTemplate.getForEntity(url, Response.class);
        Ex.check(Objects.requireNonNull(resp.getBody()).getCode()==200,
                new InternalException("check object exist failed"+resp));
        return ((Map<String,Boolean>) resp.getBody().getData()).get("exists");
    }
}
