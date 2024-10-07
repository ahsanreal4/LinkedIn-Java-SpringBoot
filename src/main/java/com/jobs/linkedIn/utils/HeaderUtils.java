package com.jobs.linkedIn.utils;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtils {
    private final String TOKEN_HEADER_KEY = "Authorization";

    public String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER_KEY);

        if (token == null) return null;

        String[] tokenSplit = token.split(" ");

        if (tokenSplit.length < 2) return null;

        return tokenSplit[1];
    }
}
