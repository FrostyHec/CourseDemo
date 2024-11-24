package org.frosty.server.entity.converter;


import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.UserPublicInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommonConverter {
    UserPublicInfo toUserPublicInfo(User user);
}
