package org.frosty.server.services.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.common.response.Response;
import org.frosty.common.utils.Ex;
import org.frosty.common_service.im.api.MessagePushService;
import org.frosty.common_service.storage.api.SharedBiMapService;
import org.frosty.server.controller.user.AuthController.LoginInfo;
import org.frosty.server.controller.user.AuthController.LoginSuccessInfo;
import org.frosty.server.entity.converter.CommonConverter;
import org.frosty.server.mapper.user.UserMapper;
import org.frosty.sse.constant.MessageBodyType;
import org.frosty.sse.entity.SiteMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper mapper;
    private final JwtHandler jwtHandler;
    private final PasswordEncoder passwordEncoder;
    private final CommonConverter converter;
    private final SharedBiMapService sharedBiMapService;
    private final MessagePushService messagePushService;
    private final ObjectMapper objectMapper;
    @PostConstruct
    public void init() {
        sharedBiMapService.init("auth.token-pool");
    }

    public LoginSuccessInfo login(LoginInfo loginInfo) {
        var user = mapper.selectByEmail(loginInfo.getEmail());
        Ex.check(user != null, Response.getNotFound("user-not-found"));
        Ex.check(passwordEncoder.matches(loginInfo.getPassword(), user.getPassword()),
                Response.getBadRequest("incorrect-password"));

        // pass check
        var uid = user.getUserId();
        String token = jwtHandler.createToken(new AuthInfo(uid));
        var publicInfo = converter.toUserPublicInfo(user);
        // put token into pool
        String key = "user-" + uid;
        if(sharedBiMapService.exist(key)) {
            // Notifying other devices to logout
            notifyNewLogin(uid,token);
            sharedBiMapService.delete(key);
        }
        sharedBiMapService.put(key, token);
        return new LoginSuccessInfo(publicInfo, token);
    }

    public void logout(TokenInfo tokenInfo) {
        sharedBiMapService.delete("user-" + tokenInfo.getAuthInfo().getUserID());
        // TODO eval correctness
    }

    private void notifyNewLogin(long userId, String newToken) {
        JsonNode body = objectMapper.valueToTree(Map.of("token", newToken));
        messagePushService.pushSite(SiteMessage.getSimpleSystemNewMessage(userId, MessageBodyType.new_login,body));
    }
}
