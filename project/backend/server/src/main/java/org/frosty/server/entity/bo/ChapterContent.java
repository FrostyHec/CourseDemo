package org.frosty.server.entity.bo;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Assignment.class, name = "assignment"),
        @JsonSubTypes.Type(value = Resource.class, name = "resource")
})
public interface ChapterContent {

    enum ChapterContentType {
        assignment, resource
    }
}
