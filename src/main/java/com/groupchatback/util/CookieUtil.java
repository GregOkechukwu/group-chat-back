package com.groupchatback.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    private final static long maxAge =  1 * 24 * 60 * 60;

    public static Cookie createCookie(String name, String value) {
        return new Cookie(name, value);
    }

    public static void configureCookie(Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int)maxAge);
    }

    public static void expireCookie(Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setValue("");
        cookie.setMaxAge(0);
    }

    public static void expireCookies(Cookie[] cookies, HttpServletResponse response) {
        for (Cookie cookie : cookies) {
            expireCookie(cookie);
            response.addCookie(cookie);
        }
    }

    public static Cookie getCookie(Cookie[] cookies, String name) {
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            if (StringUtils.equals(cookies[i].getName(), name)) {
                return cookies[i];
            }
        }

        return null;
    }

    public static void addCookiesInResponse(String accessTkn, String refreshTkn, HttpServletRequest request, HttpServletResponse response) {
        Cookie accessCookie, refreshCookie;
        Cookie[] cookies = request.getCookies();
        final int numOfCookies = cookies == null ? 0 : cookies.length;

        String accessTknBase64 = TokenUtil.encodeBase64(accessTkn);
        String refreshTknBase64 = TokenUtil.encodeBase64(refreshTkn);

        if (numOfCookies == 0) {
            accessCookie = createCookie("ACCESS_TKN", accessTknBase64);
            refreshCookie = createCookie("REFRESH_TKN", refreshTknBase64);
        } else {
            accessCookie = getCookie(cookies, "ACCESS_TKN");
            refreshCookie = getCookie(cookies, "REFRESH_TKN");

            accessCookie.setValue(accessTknBase64);
            refreshCookie.setValue(refreshTknBase64);
        }

        configureCookie(accessCookie);
        configureCookie(refreshCookie);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}
