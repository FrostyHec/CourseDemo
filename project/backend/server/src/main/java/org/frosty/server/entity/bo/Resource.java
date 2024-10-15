package org.frosty.server.entity.bo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private Long resourceId; // 资源ID
    private Long chapterId; // 章节ID
    private String resourceName; // 资源名称
    private String suffix; // 资源后缀
    private String fileName; // 实际文件名(object storage key)
    private String resourceVersionName; // 资源版本名称
    private Integer resourceVersionOrder; // 资源版本顺序
    private String resourceType; // 资源类型 (description/courseware/video/attachment)
    private Boolean studentCanDownload; // 学生是否可以下载
    private OffsetDateTime createdAt; // 创建时间
    private OffsetDateTime updatedAt; // 更新时间
}
