package com.hz.authority.management.sys.ctrl;

import com.hz.authority.management.sys.entity.LoginUserEntity;
import com.hz.authority.management.sys.response.BaseResponse;
import com.hz.authority.management.sys.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: authority-management-sys
 * @author: zgr
 * @create: 2021-07-25 22:47
 **/

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;


    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody LoginUserEntity loginUser) {
        String token = loginService.loginSys(loginUser);
        log.info("用户 {} 登录成功", loginUser.getUsername());
        return BaseResponse.success(token);
    }

    @GetMapping("/test/page")
    public BaseResponse<String> testPage() {
        return BaseResponse.success("test page");
    }
}
