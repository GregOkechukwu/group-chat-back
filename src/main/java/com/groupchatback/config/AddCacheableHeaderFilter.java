package com.groupchatback.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter(urlPatterns = {
        "/user",
        "/image/profilepic",
        "/image/defaultpic",
        "/image/icons"
})
public class AddCacheableHeaderFilter implements Filter {

    private Set<String> getNonCacheableMethodLookup() {
        Set<String> methodLookup = new HashSet<>();
        methodLookup.add("POST");
        methodLookup.add("PUT");
        methodLookup.add("DELETE");

        return methodLookup;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Set<String> nonCacheableMethodLookup = getNonCacheableMethodLookup();

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!nonCacheableMethodLookup.contains(request.getMethod())) {
            response.setHeader("cacheable", "true");
        }

        chain.doFilter(req, res);
    }
}
