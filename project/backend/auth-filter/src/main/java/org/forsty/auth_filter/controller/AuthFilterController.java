package org.forsty.auth_filter.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.forsty.auth_filter.config.CommonConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(CommonConstant.API_VERSION + "/auth")
public class AuthFilterController {

    @RequestMapping
    public void auth(HttpServletRequest request, HttpServletResponse response) {
        // TODO check token, set user info
        // if token invalid or expired, throw UnauthorizedException
        response.setHeader("X-Forwarded-User",null);// TODO use token info converter
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
