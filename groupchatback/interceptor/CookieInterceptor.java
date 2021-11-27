package com.groupchatback.interceptor;

import com.groupchatback.util.CookieUtil;
import com.groupchatback.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class CookieInterceptor extends HandlerInterceptorAdapter {

    private static String refreshTkn;
    private static String oldAccessTkn;
    private static String newAccessTkn;
    private static String decodedUserId;

    static {
        oldAccessTkn = newAccessTkn = decodedUserId = refreshTkn = null;
    }

    public CookieInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
            return super.preHandle(request, response, handler);
        }

        Cookie[] cookies = request.getCookies();
        Cookie accessCookie = CookieUtil.getCookie(cookies, "ACCESS_TKN");
        Cookie refreshCookie = CookieUtil.getCookie(cookies, "REFRESH_TKN");

        this.oldAccessTkn = accessCookie != null ? accessCookie.getValue() : null;
        this.refreshTkn = refreshCookie != null ? refreshCookie.getValue() : null;

        String[] encodedTokens = TokenUtil.manageTokens(oldAccessTkn, refreshTkn);
        boolean validRequest = encodedTokens[0] != null && encodedTokens[1] != null;

        if (validRequest) {
            this.newAccessTkn = encodedTokens[0];
            this.decodedUserId = encodedTokens[1];
        }
        else {
            response.setStatus(401);
        }

        return validRequest;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (StringUtils.equals(request.getMethod(), "OPTIONS") || StringUtils.equals(oldAccessTkn, newAccessTkn)) {
            return;
        }

        Cookie newAccessCookie = CookieUtil.createCookie("ACCESS_TKN", newAccessTkn);
        Cookie newRefreshCookie = CookieUtil.createCookie("REFRESH", refreshTkn);

        CookieUtil.configureCookie(newAccessCookie);
        CookieUtil.configureCookie(newRefreshCookie);

        response.addCookie(newAccessCookie);
        response.addCookie(newRefreshCookie);

        super.postHandle(request, response, handler, modelAndView);
    }

    public static String getDecodedUserId() {
        return decodedUserId;
    }
}
