package org.frosty.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;

@Log4j2
public class HTTPLogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest && response instanceof HttpServletResponse httpResponse) {
            // log whole incoming request
            log.info("Request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());

            chain.doFilter(request, response);
            // log whole outgoing response
            log.info("Response: {}", httpResponse.getStatus());


        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // 清理资源（如果需要）
    }
}
