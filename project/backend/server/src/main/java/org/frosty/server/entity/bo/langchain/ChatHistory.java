package org.frosty.server.entity.bo.langchain;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistory {
    private Long id;
    private Long userId;
    private String title;
    private JsonNode context;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
