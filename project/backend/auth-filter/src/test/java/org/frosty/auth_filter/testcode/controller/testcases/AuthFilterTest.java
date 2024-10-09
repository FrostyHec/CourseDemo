package org.frosty.auth_filter.testcode.controller.testcases;

import org.frosty.auth.config.AuthConstant;
import org.frosty.auth.entity.AuthInfo;
import org.frosty.auth.entity.TokenInfo;
import org.frosty.auth.utils.JwtHandler;
import org.frosty.auth.utils.TokenUtils;
import org.frosty.auth_filter.testcode.controller.AuthFilterAPI;
import org.frosty.test_common.annotation.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ControllerTest
public class AuthFilterTest {
    @Autowired
    private AuthFilterAPI authFilterAPI;
    @Autowired
    private JwtHandler jwtHandler;

    @Test
    public void checkCorrectParse() throws Exception {
        var authInfo = new AuthInfo(114);
        var token = jwtHandler.createToken(authInfo);
        authFilterAPI.auth(HttpMethod.GET, token)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(AuthConstant.parsedHeader,
                        TokenUtils.tokenInfoToString(TokenInfo.getWithAuthInfo(authInfo))))
                .andExpect(MockMvcResultMatchers.content().string(""));
        // warn: filter won't contain request param/body, it just parses the header to satisfy the k8s-nginx-ingress
    }
}
