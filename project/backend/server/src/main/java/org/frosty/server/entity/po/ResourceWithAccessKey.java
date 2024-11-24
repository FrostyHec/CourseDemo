package org.frosty.server.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frosty.server.entity.bo.Resource;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceWithAccessKey {
    Resource resource;
    String accessKey;
}