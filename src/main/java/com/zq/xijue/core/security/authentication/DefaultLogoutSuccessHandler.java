package com.zq.xijue.core.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.zq.xijue.core.security.SecurityConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的退出处理逻辑
 * @author jitwxs
 * @since 2019/1/10 21:49
 */
@Slf4j
@Component
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = ((User) authentication.getPrincipal()).getUsername();
        log.info("退出成功，用户名：{}", username);

        response.sendRedirect(SecurityConstants.UN_AUTHENTICATION_URL);
    }
}
