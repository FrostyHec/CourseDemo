package org.frosty.sse.converter;


import org.frosty.sse.entity.SiteMessage;
import org.frosty.sse.entity.UnackedSiteMessage;
import org.frosty.sse.entity.UnposedSiteMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SiteMessageConverter {
    UnposedSiteMessage toUnposed(SiteMessage siteMessage);
    UnackedSiteMessage toUnacked(SiteMessage siteMessage);
}
