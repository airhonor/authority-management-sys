package com.hz.authority.management.sys.constant;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-26 09:59
 **/

public class Constant {
    private Constant() {
    }

    /**
     * 产生jwt定义的时间常量，实际使用中可以作为配置项加载进系统中
     */
    public static final String CLAIM_KEY_USER_NAME = "username";
    public static final String CLAIM_KEY_USER_ID = "user_id";
    public static final String CLAIM_KEY_AUTHORITIES = "scope";

    public static final String JWT_SECRET = "secret-jwt-%%%%%";

    /**
     * 有效期 30分钟
     */
    public static final Long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000L;

    /**
     * 刷新有效期
     */
    public static final Long REFRESH_TOKEN_EXPIRATION = 30 * 60 * 1000L;
    public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_STARTER = "Bearer";

    //访问的接口包含的字段
    public static final String LOGIN_URL = "/login";

}
