package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.BaseCommon;
import com.lbx.distribution.manageserver.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Validated
@Api(value = "登录退出")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "login")
    @ApiOperation(value = "登录", notes = "用户登录验证")
    public String login(@RequestBody Map<String, Object> request){
        return loginService.login(request);
    }

    @PostMapping(value = "/getUser")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    public String getUser(@RequestBody Map<String, Object> request){
        return loginService.getUser(request);
    }

    @PostMapping(value = "/isExist")
    @ApiOperation(value = "验证账号", notes = "验证账号是否存在")
    public String isExist(@RequestBody Map<String, Object> request){
        return loginService.isExist(request);
    }

    @PostMapping(value = "/checkPassword")
    @ApiOperation(value = "验证密码是否正确", notes = "验证密码是否正确")
    public String checkPassword(@RequestBody Map<String, Object> request){
        return loginService.checkPassword(request);
    }

    @PostMapping(value = "exit")
    @ApiOperation(value = "退出登录", notes = "退出登录")
    public String exit(HttpServletRequest req, HttpServletResponse response){
        return loginService.exit(req, response);
    }

}
