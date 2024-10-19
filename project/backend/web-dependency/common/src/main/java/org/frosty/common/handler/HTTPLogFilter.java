package org.frosty.common.handler;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HTTPLogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            // Log request details
            log.info("Request: method: {}, uri: {}, headers: {}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    getHeaders(httpRequest));

            // Proceed with the filter chain
            chain.doFilter(request, response);

            // Log response details
            log.info("Response: status: {}", httpResponse.getStatus());
        } else {
            chain.doFilter(request, response);
        }
    }

    private String getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers.toString();
    }

    @Override
    public void destroy() {
        // 清理资源（如果需要）
    }
}
