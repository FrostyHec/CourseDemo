package org.frosty.sse_push.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.frosty.sse_push.entity.SSEIPEntity;

@Mapper
public interface SSEIPRawMapper extends BaseMapper<SSEIPEntity> {

}
