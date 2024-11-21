package org.frosty.server.entity.bo.market;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.frosty.server.entity.bo.market.action_type.ActionParam;
import org.frosty.server.entity.bo.market.action_type.BuyBadgeActionParam;
import org.frosty.server.entity.bo.market.action_type.CompleteCourseActionParam;
import org.frosty.server.entity.bo.market.action_type.DailyCommentActionParam;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("consume_record")
public class ConsumeRecord {
    @TableId(type = IdType.AUTO)
    private Long recordId;
    private Long userId;
    private ConsumeActionType actionType;
    private ActionParam actionParam;
    private Integer changedScore;
    private Integer remainScore;
    private OffsetDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public enum ConsumeActionType {
        buy_badge(BuyBadgeActionParam.class),
        daily_comment(DailyCommentActionParam.class),
        complete_course(CompleteCourseActionParam.class);
        private final Class<?> actionParamType;

        public ActionParam create() throws InstantiationException, IllegalAccessException {
            return (ActionParam) actionParamType.newInstance();
        }
    }
}
