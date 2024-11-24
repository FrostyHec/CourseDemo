package org.frosty.server.services.market.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.server.controller.course.CommentController;
import org.frosty.server.controller.market.EarnCreditEventHandler;
import org.frosty.server.entity.bo.ResourceComment;
import org.frosty.server.entity.bo.market.ConsumeRecord;
import org.frosty.server.entity.bo.market.action_type.CompleteCourseActionParam;
import org.frosty.server.entity.bo.market.action_type.DailyCommentActionParam;
import org.frosty.server.event.update.CompleteCourseEvent;
import org.frosty.server.event.update.CreateCommentEvent;
import org.frosty.server.services.course.CommentService;
import org.frosty.server.services.course.CourseService;
import org.frosty.server.services.market.EarnCreditEventService;
import org.frosty.server.services.market.MarketHistoryService;
import org.frosty.server.services.market.MarketService;
import org.frosty.sse.constant.MessageBodyType;
import org.frosty.sse.entity.SiteMessage;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EarnCreditEventServiceImp implements EarnCreditEventService {
    private final CommentService commentService;
    private final MarketService marketService;
    private final MarketHistoryService marketHistoryService;
    private final CourseService courseService;
    private final MessagePushService messagePushService;

    @Override
    public void handleCourseComplete(CompleteCourseEvent event) {
        // 获取用户ID和课程ID
        Long userId = event.getStudentId();
        Long courseId = event.getCourseId();
        String courseName = courseService.getCourse(courseId).getCourseName();

        // 构建操作参数
        CompleteCourseActionParam actionParam = new CompleteCourseActionParam();
        actionParam.setCourseId(courseId);
        actionParam.setCourseName(courseName);

        // 获取变动积分值
        int changedScore = EarnCreditEventHandler.ScoreRule.COMPLETE_COURSE.ordinal();

        // 获取当前用户的剩余积分
        int currentScore = marketService.getMyMarketScore(userId).getMarketScore();
        int remainScore = currentScore + changedScore;

        // 插入消费历史记录
        ConsumeRecord consumeRecord = new ConsumeRecord()
                .setUserId(userId)
                .setActionType(ConsumeRecord.ConsumeActionType.complete_course)
                .setActionParam(actionParam)
                .setChangedScore(changedScore)
                .setRemainScore(remainScore);


        marketHistoryService.insertCourseCompleteHistory(consumeRecord);
        marketService.addUserMarketScore(userId, EarnCreditEventHandler.ScoreRule.COMPLETE_COURSE.getScore());


        // 构建消息通知
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("type", "complete-course");
        body.put("count", changedScore);

        SiteMessage message = SiteMessage.getSimpleSystemNewMessage(
                userId,
                MessageBodyType.receive_credits,
                body
        );

        // 推送消息
        messagePushService.pushSite(message);
    }

    @Override
    public void handleDailyComment(CreateCommentEvent event) {
        Long commentId = event.getCommentId();
        ResourceComment resourceComment = commentService.findById(commentId);
        List<ResourceComment> resourceCommentList = commentService.findByUserIdAndCreatedTime(
                resourceComment.getUserId(),
                resourceComment.getCreatedAt());

        // 判断当前评论是否是当天最早的评论
        boolean isEarliestComment = resourceCommentList.stream()
                .min(Comparator.comparing(ResourceComment::getCreatedAt))
                .map(ResourceComment::getCreatedAt)
                .map(earliestTime -> earliestTime.equals(resourceComment.getCreatedAt()))
                .orElse(false);

        // 如果是最早评论，则处理积分逻辑
        if (isEarliestComment) {
            Long userId = resourceComment.getUserId();
            Long courseId = commentService.findCourseIdByComment(commentId);
            String courseName = courseService.getCourse(courseId).getCourseName();

            // 构建操作参数
            DailyCommentActionParam actionParam = new DailyCommentActionParam();
            actionParam.setCourseId(courseId);
            actionParam.setCourseName(courseName);

            // 获取变动积分值
            int changedScore = EarnCreditEventHandler.ScoreRule.DAILY_COMMENT.ordinal();

            // 获取当前用户的剩余积分
            int currentScore = marketService.getMyMarketScore(userId).getMarketScore();
            int remainScore = currentScore + changedScore;

            // 插入消费历史记录
            ConsumeRecord consumeRecord = new ConsumeRecord()
                    .setUserId(userId)
                    .setActionType(ConsumeRecord.ConsumeActionType.daily_comment)
                    .setActionParam(actionParam)
                    .setChangedScore(changedScore)
                    .setRemainScore(remainScore);

            marketHistoryService.insertDailyCommentHistory(consumeRecord);
            marketService.addUserMarketScore(
                    userId,
                    EarnCreditEventHandler.ScoreRule.DAILY_COMMENT.getScore()
            );

            // 构建消息通知
            ObjectNode body = JsonNodeFactory.instance.objectNode();
            body.put("type", "daily-comment");
            body.put("count", changedScore);

            SiteMessage message = SiteMessage.getSimpleSystemNewMessage(
                    userId,
                    MessageBodyType.receive_credits,
                    body
            );

            // 推送消息
            messagePushService.pushSite(message);
        }

    }
}
