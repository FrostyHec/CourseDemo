package org.frosty.demo.mapper;

import org.apache.ibatis.annotations.Select;

public interface HelloWorldMapper {

    @Select("SELECT 'Hello World'")
    String getHelloMessage();
}
