package org.frosty.server.entity.bo.langchain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.entity.handler.JsonNodeTypeHandler;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("chat_history")
public class ChatHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
//    @TableField(typeHandler = JsonNodeTypeHandler.class)
    private LangchainController.ChatContext context;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
