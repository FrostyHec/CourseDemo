package org.frosty.demo.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    private Long chapter_id;
    private Long course_id;
    private String chapter_title;
    private String chapter_type;
    private String content;
    private OffsetDateTime created_at;
}
