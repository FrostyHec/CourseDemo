package org.frosty.server.mapper.langchain;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.entity.bo.Assignment;

import java.util.List;

@Mapper
public interface LangchainMapper extends BaseMapper<Assignment> {

    @Update("")
    void updateByChatId(Long id);

    @Update("")
    void setChatTitle(String title, Long id);

    List<LangchainController.SingleChatMessage> selectChatContentByChatId(Long id);

    LangchainController.ChatEntity selectChatEntityByChatId(Long id);

    List<LangchainController.ChatEntity> selectChatListByUserId(long userID);
}
