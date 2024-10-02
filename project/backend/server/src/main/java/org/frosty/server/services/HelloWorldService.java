package org.frosty.server.services;


import lombok.RequiredArgsConstructor;
import org.frosty.infra.initalizer.DatabaseInitializer;
import org.frosty.server.mapper.HelloWorldMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloWorldService {
    private final DatabaseInitializer databaseInitializer;
    private final HelloWorldMapper helloWorldMapper;
    public String getHelloMessage() {
        return helloWorldMapper.getHelloMessage();
    }

}
