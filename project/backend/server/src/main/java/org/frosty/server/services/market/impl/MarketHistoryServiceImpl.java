package org.frosty.server.services.market.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.frosty.server.controller.market.MarketHistoryController;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.entity.bo.market.action_type.ActionParam;
import org.frosty.server.mapper.market.MarketHistoryMapper;
import org.frosty.server.services.market.MarketHistoryService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MarketHistoryServiceImpl implements MarketHistoryService {
    private final MarketHistoryMapper marketHistoryMapper;
    // 不知道这个是不是需要的objectMapper
    private final ObjectMapper objectMapper; // Jackson的ObjectMapper

    // TODO: 检测 JSON 是否成功反序列化
    @Override
    public List<ConsumeRecord> getHistoryByUserId(Long userId) {
        // 分别获取三种历史记录
        List<ConsumeRecord> badgeBuyRecords = marketHistoryMapper.getBadgeBuyHistoryByUserId(userId);
        List<ConsumeRecord> dailyCommentRecords = marketHistoryMapper.getDailyCommentHistoryByUserId(userId);
        List<ConsumeRecord> completeCourseRecords = marketHistoryMapper.getCompleteCourseHistoryByUserId(userId);

        // 合并所有记录
        List<ConsumeRecord> allRecords = Stream.concat(
                        Stream.concat(badgeBuyRecords.stream(), dailyCommentRecords.stream()),
                        completeCourseRecords.stream()
                )
                .sorted(Comparator.comparing(ConsumeRecord::getCreatedAt).reversed()) // 按创建时间降序排序
                .collect(Collectors.toList());

        return allRecords;
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
}
