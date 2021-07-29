package com.hz.authority.management.sys.aspect;

import com.hz.authority.management.sys.constant.Constant;
import com.hz.authority.management.sys.entity.authority.SysUserDetails;
import com.hz.authority.management.sys.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-26 12:02
 **/


@Slf4j
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!request.getRequestURL().toString().contains(Constant.LOGIN_URL)) {
            String token = request.getHeader(Constant.TOKEN_HEADER);
            if (StringUtils.isNotEmpty(token) && token.startsWith(Constant.TOKEN_STARTER)) {
                token = token.substring(Constant.TOKEN_STARTER.length()).trim();
            } else {
                token = null;
            }

            //关于token形式验证通过，验证token的内容
            String username = jwtTokenUtil.getUsername(token);
            if (username != null && jwtTokenUtil.checkToken(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("{} access the {} API", username, request.getRequestURL());
                SysUserDetails userDetails = jwtTokenUtil.getUserDetails(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info(String.format("Authenticated userDetail %s, setting security context", username));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
