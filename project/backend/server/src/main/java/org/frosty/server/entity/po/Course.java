package org.frosty.server.entity.po;
import lombok.Data;

@Data
public class Course {
    private Long id;
    private String name;
    private String description;
    private String status;
}