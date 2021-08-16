package com.hz.authority.management.sys.utils;

import com.hz.authority.management.sys.constant.Constant;
import com.hz.authority.management.sys.entity.Role;
import com.hz.authority.management.sys.entity.authority.SysUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-25 21:06
 **/
@Component
@Slf4j
public class JwtTokenUtil {

    private Map<String, String> tokenMap = new ConcurrentHashMap<>(32);

    private Date generateExpirationDate(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration);
    }

    /**
     * 生成令牌
     *
     * @param userDetail 用户
     * @return 令牌
     */
    public String generateAccessToken(SysUserDetails userDetail) {
        Map<String, Object> claims = generateClaims(userDetail);
        return generateAccessToken(userDetail.getUsername(), claims);
    }

    public boolean checkToken(String userName, String token) {
        /*1.token对比，是否存在
         *2.token是否过期，过期应该重新登录
         **/
        Long expirationTime = getExpirationTime(token);
        return userName != null
                && tokenMap.containsKey(userName)
                && tokenMap.get(userName).equals(token)
                && expirationTime != null
                && expirationTime > System.currentTimeMillis();
    }

    public void putToken(String userName, String token) {
        tokenMap.put(userName, token);
    }

    public void deleteToken(String userName) {
        tokenMap.remove(userName);
    }


    /**
     * 生成token
     *
     * @param subject 用户名
     * @param claims
     * @return
     */
    private String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims);
    }

    /**
     * 根据token 获取用户信息
     *
     * @param token
     * @return
     */
    public SysUserDetails getUserDetails(String token) {
        SysUserDetails userDetail;
        try {
            final Claims claims = getClaims(token);
            Integer userId = Integer.parseInt(claims.get(Constant.CLAIM_KEY_USER_ID).toString());
            String username = getUsername(token);
            String roleName = claims.get(Constant.CLAIM_KEY_AUTHORITIES).toString();
            Role role = Role.builder().name(roleName).build();
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            userDetail = new SysUserDetails(userId, username, null, roles);
            log.info("user details {}", userDetail.toString());
        } catch (Exception e) {
            log.error("获取用户详情出错", e);
            userDetail = null;
        }
        return userDetail;
    }

    /**
     * 生成token
     *
     * @param subject 用户名
     * @param claims  声明
     * @return
     */
    private String generateToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
                .setSubject(subject)
                // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                // iat: jwt的签发时间
                .setIssuedAt(new Date())
                //有效时长
                .setExpiration(generateExpirationDate(Constant.ACCESS_TOKEN_EXPIRATION))
                //压缩格式
                .compressWith(CompressionCodecs.DEFLATE)
                //secret在实际使用中可以做成配置项
                .signWith(Constant.SIGNATURE_ALGORITHM, Constant.JWT_SECRET)
                .compact();
    }

    /**
     * 根据token 获取用户名
     *
     * @param token
     * @return
     */
    public String getUsername(String token) {
        String username;
        try {
            final Claims claims = getClaims(token);
            username = claims.getSubject();
        } catch (Exception e) {
            log.error("获取用户名出错", e);
            username = null;
        }
        return username;
    }

    public Long getExpirationTime(String token) {
        Long expirationTime;
        try {
            final Claims claims = getClaims(token);
            expirationTime = claims.getExpiration().getTime();
        } catch (Exception e) {
            expirationTime = null;
        }
        return expirationTime;
    }

    /**
     * 根据token 获取用户ID
     *
     * @param token
     * @return
     */
    private Integer getUserId(String token) {
        Integer userId;
        try {
            final Claims claims = getClaims(token);
            userId = Integer.parseInt((String) claims.get(Constant.CLAIM_KEY_USER_ID));
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /***
     * 解析token 信息
     * @param token
     * @return
     */
    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Constant.JWT_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Map<String, Object> generateClaims(SysUserDetails userDetail) {
        Map<String, Object> userDetails = new HashMap<>(16);
        userDetails.put(Constant.CLAIM_KEY_USER_ID, userDetail.getId());
        userDetails.put(Constant.CLAIM_KEY_AUTHORITIES, authoritiesToArray(userDetail.getAuthorities()).get(0));
        return userDetails;
    }


    private List authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        for (GrantedAuthority ga : authorities) {
            list.add(ga.getAuthority());
        }
        return list;
    }

}
