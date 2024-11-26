package org.frosty.server.entity.bo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("resources")
public class Resource implements ChapterContent {
    @TableId(type = IdType.AUTO)
    private Long resourceId; // 资源ID
    private Long chapterId; // 章节ID
    private String resourceName; // 资源名称
    private String suffix; // 资源后缀
    private String fileName; // 实际文件名(object storage key)
    private Integer resourceOrder; // 资源顺序
    private String resourceVersionName; // 资源版本名称
    private Integer resourceVersionOrder; // 资源版本顺序
    private ResourceType resourceType; // 资源类型 (description/courseware/video/attachment)
    private Boolean studentCanDownload; // 学生是否可以下载
    private OffsetDateTime createdAt; // 创建时间
    private OffsetDateTime updatedAt; // 更新时间

    public enum ResourceType {
        description,
        courseware,
        video,
        attachment
    }
}
