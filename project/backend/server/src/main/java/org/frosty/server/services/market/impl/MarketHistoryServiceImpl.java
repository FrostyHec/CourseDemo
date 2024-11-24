package org.frosty.server.services.market.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.entity.bo.market.action_type.ActionParam;
import org.frosty.server.mapper.market.MarketHistoryMapper;
import org.frosty.server.services.market.MarketHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketHistoryServiceImpl implements MarketHistoryService {
    private final MarketHistoryMapper marketHistoryMapper;
    // 不知道这个是不是需要的objectMapper
    private final ObjectMapper objectMapper; // Jackson的ObjectMapper

    // TODO: 检测 JSON 是否成功反序列化
    @Override
    public List<ConsumeRecord> getHistoryByUserId(Long userId) {
        return marketHistoryMapper.getHistoryByUserId(userId).stream()
                .map(this::deserializeActionParam)
                .collect(Collectors.toList());
    }

    @Override
    public void insertCourseCompleteHistory(ConsumeRecord consumeRecord) {
        marketHistoryMapper.insert(consumeRecord);
    }

    @Override
    public void insertDailyCommentHistory(ConsumeRecord consumeRecord) {
        marketHistoryMapper.insert(consumeRecord);
    }

    @Override
    public void insertConsumeRecord(ConsumeRecord consumeRecord) {
        marketHistoryMapper.insert(consumeRecord);
    }

    private ConsumeRecord deserializeActionParam(ConsumeRecord record) {
        try {
            Class<?> paramType = record.getActionType().getActionParamType();
            ActionParam actionParam = (ActionParam) objectMapper.readValue(record.getActionParam().toString(), paramType);
            return record.setActionParam(actionParam);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize actionParam for recordId: " + record.getRecordId(), e);
        }
    }
}
