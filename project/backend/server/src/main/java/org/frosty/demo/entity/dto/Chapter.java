package org.frosty.demo.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chapter {
    private Long chapter_id;
    private Long course_id;
    private String chapter_title;
    private String chapter_type;
    private String content;
    private OffsetDateTime created_at;
    private OffsetDateTime updated_at;
}
