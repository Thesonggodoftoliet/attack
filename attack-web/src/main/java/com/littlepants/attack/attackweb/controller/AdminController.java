package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.AccessLog;
import com.littlepants.attack.attackweb.entity.User;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.LogService;
import com.littlepants.attack.attackweb.service.intf.UserService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.util.RandomPassword;
import com.littlepants.attack.attackweb.validation.EditUserInfoValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "管理员相关接口")
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final LogService logService;

    public AdminController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @PostMapping("/test")
    @ResponseBody
    @ApiOperation(value = "示例用",hidden = true)
    public CustomResponseEntity Test(@RequestParam("file")String id, @RequestBody CustomResponseEntity response) {
        System.out.println(id);
        return new CustomResponseEntity(100,"0",null);
    }

    @CrossOrigin
    @PutMapping("/changeGroup")
    @ResponseBody
    @ApiOperation(value = "根据用户名username更改用户组",response = CustomResponseEntity.class)
    public String changeGroup(@RequestBody @Validated(EditUserInfoValidation.class) User user , HttpServletRequest request) {
        AccessLog accessLog = new AccessLog();
        accessLog.setRemoteAddress(request.getRemoteAddr());
        accessLog.setUserName((String) request.getAttribute("requestUser"));
        if (user.getGroupId() == 1)
            accessLog.setLogEvent("将"+user.getUsername()+"角色变更为管理员");
        else if (user.getGroupId() == 2)
            accessLog.setLogEvent("将"+user.getUsername()+"角色变更为数据记录员");
        else if (user.getGroupId() == 3)
            accessLog.setLogEvent("将"+user.getUsername()+"角色变更为数据分析员");
        logService.addLog(accessLog);
        List<String> errors = new ArrayList<>();
        try {
            userService.changeGroupByName(user);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            errors.add("请检查'" + user.getUsername() + "'是否正确");
        } catch (Exception e) {
            e.printStackTrace();
            errors.add("修改" + user.getUsername() + "时出现未知错误，请重试");
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500, errors.toString(), null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS, null).toString();
    }

    @CrossOrigin
    @GetMapping("/getAllUser")
    @ResponseBody
    @ApiOperation(value = "获取所有用户信息",response = CustomResponseEntity.class)
    public String getAllUser() {
        List<User> users = userService.queryAll();
        return new CustomResponseEntity(ResponseCode.SUCCESS, JsonUtils.toString(users)).toString();
    }

    @CrossOrigin
    @PostMapping("/resetPassword")
    @ResponseBody
    @ApiOperation(value = "根据用户名username生成随机密码",response = CustomResponseEntity.class)
    public String resetPassword(@RequestBody User user,HttpServletRequest request) {
        AccessLog accessLog = new AccessLog();
        accessLog.setRemoteAddress(request.getRemoteAddr());
        accessLog.setUserName((String) request.getAttribute("requestUser"));
        accessLog.setLogEvent("重置了"+user.getUsername()+"的密码");
        logService.addLog(accessLog);
        List<String> errors = new ArrayList<>();
        try {
            user.setResetPassword(RandomPassword.generateRandomPassword());
            userService.resetPasswordByName(user);
        }catch (IndexOutOfBoundsException e){
          e.printStackTrace();
          errors.add("请检查"+user.getUsername()+"是否正确");
        } catch (Exception e) {
            e.printStackTrace();
            errors.add("修改" + user.getUsername() + "时出现未知错误，请重试");
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500, errors.toString(), null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS, JsonUtils.toString(user)).toString();
    }

    @CrossOrigin
    @PostMapping("/delUserByName")
    @ResponseBody
    @ApiOperation(value = "根据用户名username删除用户",response = CustomResponseEntity.class)
    public String delUserByName(@RequestBody User user,HttpServletRequest request) {
        AccessLog accessLog = new AccessLog();
        accessLog.setRemoteAddress(request.getRemoteAddr());
        accessLog.setUserName((String) request.getAttribute("requestUser"));
        accessLog.setLogEvent("删除了用户"+user.getUsername());
        logService.addLog(accessLog);
        List<String> errors = new ArrayList<>();
        try {
            userService.deleteBy("username", user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            errors.add("删除" + user.getUsername() + "时出现未知错误，请重试");
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500, errors.toString(), null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS, null).toString();
    }
    @CrossOrigin
    @PostMapping("/getUserCount")
    @ResponseBody
    @ApiOperation(value = "自定义条件'列名'查询用户数量",response = CustomResponseEntity.class)
    public String getUserCountByCondition(@RequestBody Map map){
        Map<String,Object> result = new HashMap<>();
        String condition;
        String value;
        Integer count;
        try {
            condition= (String) map.get("condition");
            value= (String) map.get("value");
            if (condition == null||value == null)
                throw new NullPointerException();
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"参数错误",null).toString();
        }
        if (condition.isEmpty())
            count= userService.count();
        else
            count = userService.count(condition,value);
        result.put("count",count);
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(result)).toString();
    }

    @CrossOrigin
    @GetMapping("/getGroupCount")
    @ResponseBody
    @ApiOperation(value = "获取各分组人数",response = CustomResponseEntity.class)
    public String getGroupCount(){
        Map<String,Object> result = new HashMap<>();
        Integer adminCount = userService.count("group_id","1");
        Integer loggerCount = userService.count("group_id","2");
        Integer userCount = userService.count("group_id","3");
        result.put("admin",adminCount);
        result.put("logger",loggerCount);
        result.put("user",userCount);
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(result)).toString();
    }

    @CrossOrigin
    @GetMapping("/getLogs")
    @ResponseBody
    @ApiOperation(value = "获取日志",response = CustomResponseEntity.class)
    public String getLogs(){
        List<AccessLog> logs = logService.getAccessLog();
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(logs)).toString();
    }
}
