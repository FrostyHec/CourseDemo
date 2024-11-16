package org.frosty.server.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResource {
    private Long id;
    private Long commentId;
    private String resourceName;
    private String fileName;
    private String suffix;
}
