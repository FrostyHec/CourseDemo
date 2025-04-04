package org.frosty.server.test.controller.smoke_test.course.livestream;

import org.frosty.server.controller.course.livestream.ChatRoomWebSocket;
import org.frosty.server.entity.bo.User;
import org.frosty.server.mapper.user.UserMapper;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.course.livestream.ChatRoomSocketWrapper;
import org.frosty.server.test.controller.user.UserAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.concurrent.TimeoutException;

// can run only when backend is started
@IdempotentControllerTest
public class ChatRoomSmokeTest {
    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testNormalChat() throws Exception {
        var res = authAPI.quickAddUserAndLogin("u1", User.Role.teacher);
        var u1Info = res.second;
        var u1Uid = u1Info.getUserId();

        res = authAPI.quickAddUserAndLogin("u2", User.Role.student);
        var u2Info = res.second;
        var u2Uid = u2Info.getUserId();

        res = authAPI.quickAddUserAndLogin("u3", User.Role.student);
        var u3Info = res.second;
        var u3Uid = u3Info.getUserId();

        res = authAPI.quickAddUserAndLogin("u4", User.Role.student);
        var u4Info = res.second;
        var u4Uid = u4Info.getUserId();

        // 1. 创建两个用户的WebSocket连接
        var user1 = ChatRoomSocketWrapper.create("r1", u1Uid);
        var user2 = ChatRoomSocketWrapper.create("r1", u2Uid);
        var user3 = ChatRoomSocketWrapper.create("r1", u3Uid);
        var user4 = ChatRoomSocketWrapper.create("r2", u4Uid);

        var msg = new ChatRoomWebSocket.ReceivedChatRoomMessage(u2Uid, "hello");
        // 2. 用户1发送消息
        user1.sendMessage(msg);

        // 3. 用户2接收消息
        var rcvdMessage = user2.receivedMessage(1000);
        assert rcvdMessage.getContent().equals(msg.getContent());
        assert Objects.equals(rcvdMessage.getFromUser().getUserId(), u1Uid);
        assert rcvdMessage.getFromUser().getRole() == User.Role.teacher;

        try {
            user3.receivedMessage(500);
            assert false;
        } catch (Exception e) {
            assert e instanceof TimeoutException;
        }
        try {
            user4.receivedMessage(500);
            assert false;
        } catch (Exception e) {
            assert e instanceof TimeoutException;
        }

        // 4. 关闭连接
        user1.close();
        user2.close();

        //重新建立连接
        user1 = ChatRoomSocketWrapper.create("r1", u1Uid);
        user2 = ChatRoomSocketWrapper.create("r1", u2Uid);

        // 5. 用户1发送公开消息
        user1.sendMessage(new ChatRoomWebSocket.ReceivedChatRoomMessage(-1L, "hello"));

        rcvdMessage = user2.receivedMessage(1000);
        assert rcvdMessage.getContent().equals(msg.getContent());
        assert Objects.equals(rcvdMessage.getFromUser().getUserId(), u1Uid);

        rcvdMessage = user3.receivedMessage(1000);
        assert rcvdMessage.getContent().equals(msg.getContent());
        assert Objects.equals(rcvdMessage.getFromUser().getUserId(), u1Uid);

        try {
            user4.receivedMessage(500);
            assert false;
        } catch (Exception e) {
            assert e instanceof TimeoutException;
        }

        user1.close();
        user2.close();
        user3.close();
        user4.close();
    }

}
