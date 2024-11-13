package org.frosty.object_storage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;
@Data
@AllArgsConstructor
public class FlowWithMetadata {
    private InputStream stream;
    private String contentType;
}
