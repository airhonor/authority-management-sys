package com.hz.authority.management.sys.ctrl;


import com.hz.authority.management.sys.entity.LoginUserEntity;
import com.hz.authority.management.sys.response.BaseResponse;
import com.hz.authority.management.sys.service.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;


    @Test
    public void loginSuccessTest() {
        String token = "this is token";
        LoginUserEntity loginUserEntity = LoginUserEntity.builder()
                .username("admin")
                .password("admin")
                .build();
        Mockito.when(loginService.loginSys(loginUserEntity)).thenReturn(token);
        BaseResponse<String> baseResponse = loginController.login(loginUserEntity);
        Assert.assertEquals(200, baseResponse.getCode());
        Assert.assertEquals("success", baseResponse.getMsg());
        Assert.assertEquals(token, baseResponse.getData());
    }

    @Test
    public void testPage() {
        BaseResponse<String> baseResponse = loginController.testPage();
        Assert.assertEquals(200, baseResponse.getCode());
        Assert.assertEquals("success", baseResponse.getMsg());
    }
}