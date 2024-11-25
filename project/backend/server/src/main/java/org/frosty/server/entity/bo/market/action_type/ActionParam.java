package org.frosty.server.entity.bo.market.action_type;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "actionType")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = BuyBadgeActionParam.class, name = "buy_badge"),
//        @JsonSubTypes.Type(value = DailyCommentActionParam.class, name = "daily_comment"),
//        @JsonSubTypes.Type(value = CompleteCourseActionParam.class, name = "complete_course")
//})
public interface ActionParam {
}
