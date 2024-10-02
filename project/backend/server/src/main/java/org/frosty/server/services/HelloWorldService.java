package org.frosty.server.services;



import org.frosty.server.mapper.HelloWorldMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelloWorldService {
    private final HelloWorldMapper helloWorldMapper;
    public String getHelloMessage() {
        return helloWorldMapper.getHelloMessage();
    }

}
