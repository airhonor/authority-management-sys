package com.hz.authority.management.sys.config;

import com.hz.authority.management.sys.aspect.AuthenticationTokenFilter;
import com.hz.authority.management.sys.aspect.MyAuthenticationEntryPoint;
import com.hz.authority.management.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-25 20:52
 **/
@Configuration
@EnableWebSecurity
public class SysSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private final MyAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private final AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private final AuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private final SysLogoutHandler sysLogoutHandler;

    @Autowired
    private final SysLogoutSuccessHandler sysLogoutSuccessHandler;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    public SysSecurityConfig(MyAuthenticationEntryPoint unauthorizedHandler,
                             @Qualifier("RestAuthenticationAccessDeniedHandler") AccessDeniedHandler accessDeniedHandler, AuthenticationTokenFilter authenticationTokenFilter, SysLogoutHandler sysLogoutHandler, SysLogoutSuccessHandler sysLogoutSuccessHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.sysLogoutHandler = sysLogoutHandler;
        this.sysLogoutSuccessHandler = sysLogoutSuccessHandler;
    }

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 这里是对认证管理器的添加配置，添加自定义的用户查询服务
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysUserService).passwordEncoder(new BCryptPasswordEncoder());
        ;
    }

    /**
     * 配置不需要安全验证的接口地址
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/login", "/success/logout-page");
    }


    /**
     * 安全请求配置,这里配置的是security的部分，这里配置全部通过，安全拦截在资源服务的配置文件中配置，
     * 要不然访问未验证的接口将重定向到登录页面，前后端分离的情况下这样并不友好，无权访问接口返回相关错误信息即可
     *
     * @param http
     * @return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().addLogoutHandler(sysLogoutHandler).logoutSuccessHandler(sysLogoutSuccessHandler)
                .and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 权限不足处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // 对于登录login要允许匿名访问
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // 访问/user 需要拥有admin权限
                .antMatchers("/test/page").hasAuthority("admin")
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        // 禁用缓存
        http.headers().cacheControl();
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Collections.singletonList("*"));
//        config.setAllowedMethods(Collections.singletonList("*"));
//        config.setAllowedHeaders(Collections.singletonList("*"));
//        // 暴露 header 中的其他属性给客户端应用程序
//        config.setExposedHeaders(Arrays.asList(
//                "Authorization", "X-Total-Count", "Link",
//                "Access-Control-Allow-Origin",
//                "Access-Control-Allow-Credentials"
//        ));
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

}
