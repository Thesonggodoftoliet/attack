package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.config.ConstantKit;
import com.littlepants.attack.attackweb.config.RsaKeyProperties;
import com.littlepants.attack.attackweb.entity.PolicyGroup;
import com.littlepants.attack.attackweb.entity.User;
import com.littlepants.attack.attackweb.entity.UserStatus;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.PolicyService;
import com.littlepants.attack.attackweb.service.intf.UserService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.util.JwtUtils;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import com.littlepants.attack.attackweb.validation.EditUserInfoValidation;
import com.littlepants.attack.attackweb.validation.PasswordChangeValidation;
import com.littlepants.attack.attackweb.validation.PasswordNotCorrectException;
import com.littlepants.attack.attackweb.validation.RegisterValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController {
    private final RsaKeyProperties rsaKeyProperties;
    private final UserService userService;
    private final PolicyService policyService;
    private final JedisPool jedisPool;

    public UserController(RsaKeyProperties rsaKeyProperties, UserService userService, PolicyService policyService,
                          JedisPool jedisPool) {
        this.rsaKeyProperties = rsaKeyProperties;
        this.userService = userService;
        this.policyService = policyService;
        this.jedisPool = jedisPool;
    }

    @CrossOrigin
    @GetMapping(value = "/get")
    @ResponseBody
    @ApiOperation(value = "获取用户信息",response = CustomResponseEntity.class)
    public String getUserInfo(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("Authorization");
        String token = header.replace("AttackToken ","");
        User user = JwtUtils.getInfoFromToken(token, rsaKeyProperties.getPublicKey(),User.class).getUserinfo();
        user = (User) userService.loadUserByUsername(user.getUsername());
        user.setPassword(null);
        user.setGroup_id(null);
        String result = JsonUtils.toString(user);
        CustomResponseEntity response = new CustomResponseEntity(ResponseCode.SUCCESS,result);
        return response.toString();
    }

    @CrossOrigin
    @PostMapping(value = "/add")
    @ResponseBody
    @ApiOperation(value = "添加用户",response = CustomResponseEntity.class)
    public org.springframework.http.ResponseEntity addUser(@RequestBody @Validated(RegisterValidation.class) User user,
                                                           BindingResult result) {
        List<String> errors = new ArrayList<>();
        if (result.hasErrors()){
            List<ObjectError> errorList = result.getAllErrors();
            for (ObjectError error:errorList){
                errors.add(error.getDefaultMessage());
            }
            CustomResponseEntity response = new CustomResponseEntity(500,errors.toString(),null);
            return org.springframework.http.ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.toString());
        }
        user.setId(UUIDGenerator.generateUUID());
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        user.setLastPasswordChange(timestamp);
        user.setUserStatus(String.valueOf(UserStatus.ACTIVE));
        user.setGroupId(3);
        int tag = userService.addUser(user);
//        System.out.println(result);
        CustomResponseEntity response;
        if (tag == 1) {
            response = new CustomResponseEntity(ResponseCode.SUCCESS, null);
            User userinfo = new User();
            userinfo.setUsername(user.getUsername());
            PolicyGroup policyGroup = policyService.getPolicyGroupById(user.getGroupId());
            List<PolicyGroup> policyGroups = new ArrayList<>();
            policyGroups.add(policyGroup);
            userinfo.setGroup_id(policyGroups);
            String token = JwtUtils.generateTokenExpireInMillis(userinfo,rsaKeyProperties.getPrivateKey(),10*60*1000);
            Jedis jedis = jedisPool.getResource();
            jedis.set(userinfo.getUsername(),token);
            jedis.expire(userinfo.getUsername(), ConstantKit.TOKEN_EXPIRE_TIME);
            jedis.set(token,userinfo.getUsername());
            jedis.expire(token,ConstantKit.TOKEN_EXPIRE_TIME);
            Long currentTime = System.currentTimeMillis();
            jedis.set(token+userinfo.getUsername(),currentTime.toString());
            jedis.close();
            return org.springframework.http.ResponseEntity.ok()
                    .header("Authorization","AttackToken "+token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.toString());
        }
        else {
            response = new CustomResponseEntity(500, "用户名、手机或邮箱重复", null);
            return org.springframework.http.ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.toString());
        }

    }

    @CrossOrigin
    @PostMapping("/edit")
    @ResponseBody
    @ApiOperation(value = "修改用户信息",response = CustomResponseEntity.class)
    public String editUserInfo(@RequestBody @Validated(EditUserInfoValidation.class) User user,
                               BindingResult result,HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        String token = header.replace("AttackToken ","");
        User userInfo = JwtUtils.getInfoFromToken(token,rsaKeyProperties.getPublicKey(),User.class).getUserinfo();
        List<String> errors = new ArrayList<>();
        if (result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error:allErrors){
                errors.add(error.getDefaultMessage());
            }
            return new CustomResponseEntity(500,errors.toString(),null).toString();
        }
        user.setUsername(userInfo.getUsername());
        try {
            userService.updateUserInfoByName(user);
        } catch (DuplicateKeyException e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"手机号、邮箱已注册",null).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(ResponseCode.WARN,null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @PutMapping("/changePassword")
    @ResponseBody
    @ApiOperation(value = "修改密码",response = CustomResponseEntity.class)
    public String changePassword(@RequestBody @Validated(PasswordChangeValidation.class) User user,
                                 BindingResult result,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token = header.replace("AttackToken ","");
        User userInfo = JwtUtils.getInfoFromToken(token,rsaKeyProperties.getPublicKey(),User.class).getUserinfo();
        List<String> errors = new ArrayList<>();
        if (result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error:allErrors){
                errors.add(error.getDefaultMessage());
            }
            return new CustomResponseEntity(500,errors.toString(),null).toString();
        }
        user.setUsername(userInfo.getUsername());
        try {
            userService.changePasswordByName(user);
        }catch (PasswordNotCorrectException e){
            e.printStackTrace();
            errors.add("密码不正确，修改失败");
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,errors.toString(),null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

}
