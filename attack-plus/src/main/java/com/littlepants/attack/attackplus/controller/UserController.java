package com.littlepants.attack.attackplus.controller;

import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.dto.LoginDTO;
import com.littlepants.attack.attackplus.dto.UserDTO;
import com.littlepants.attack.attackplus.entity.Role;
import com.littlepants.attack.attackplus.entity.User;
import com.littlepants.attack.attackplus.mapper.UserMapper;
import com.littlepants.attack.attackplus.service.UserService;
import com.littlepants.attack.attackplus.service.ValidateCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-21
 */
@RestController
@RequestMapping("/user")
@Api(tags = "登录",value = "UserController")
@Slf4j
public class UserController {
    private final ValidateCodeService validateCodeService;
    private final UserService userService;

    public UserController(ValidateCodeService validateCodeService, UserService userService) {
        this.validateCodeService = validateCodeService;
        this.userService = userService;
    }

    @ApiOperation(value = "验证码",notes = "验证码")
    @GetMapping(value = "/captcha",produces = "image/png")
    public void captcha(@RequestParam(value = "key") String key, HttpServletResponse response) throws IOException{
        validateCodeService.generate(key,response);
    }

    @GetMapping("/getUserInfo")
    public R<LoginDTO> getUserInfo(HttpServletRequest httpServletRequest){
        User user = userService.getUserByAccount((String) httpServletRequest.getAttribute("requestUser"));
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDto(user);
        List<String> role = new ArrayList<>();
        for (Role role1: user.getRoles())
            role.add(role1.getName());
        return R.success(new LoginDTO(userDTO,"",role));
    }
}
