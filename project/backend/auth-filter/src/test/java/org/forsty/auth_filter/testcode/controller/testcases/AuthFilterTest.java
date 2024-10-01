package org.forsty.auth_filter.testcode.controller.testcases;

import org.forsty.auth.config.AuthConstant;
import org.forsty.auth.entity.AuthInfo;
import org.forsty.auth.entity.TokenInfo;
import org.forsty.auth_filter.testcode.controller.AuthFilterAPI;
import org.forsty.auth.utils.JwtHandler;
import org.forsty.auth.utils.TokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
