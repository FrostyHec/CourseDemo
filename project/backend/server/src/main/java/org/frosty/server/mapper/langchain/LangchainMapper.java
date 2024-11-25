package org.frosty.server.mapper.langchain;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.frosty.server.controller.langchain.LangchainController;
import org.frosty.server.entity.bo.Assignment;
import org.frosty.server.entity.bo.langchain.ChatHistory;

import java.util.List;

@Mapper
public interface LangchainMapper extends BaseMapper<ChatHistory> {
    // 不确定这个context这样写是否合法
//    @Update("UPDATE chat_history SET context = #{context,jdbcType=OTHER} WHERE id = #{id}")
    @Update("UPDATE chat_history SET context = #{context} WHERE id = #{id}")
    void updateByChatId(Long id, LangchainController.ChatContext context);

    @Update("UPDATE chat_history SET title = #{title} WHERE id = #{id}")
    void setChatTitle(String title, Long id);

    @Select("SELECT context FROM chat_history WHERE id = #{id}")
    List<LangchainController.SingleChatMessage> selectChatContentByChatId(Long id);

    @Select("SELECT id, title, context, created_at, updated_at FROM chat_history WHERE id = #{id}")
    LangchainController.ChatEntity selectChatEntityByChatId(Long id);

    @Select("SELECT id, title, created_at, updated_at FROM chat_history WHERE user_id = #{userID}")
    List<LangchainController.ChatEntity> selectChatListByUserId(long userID);

}
