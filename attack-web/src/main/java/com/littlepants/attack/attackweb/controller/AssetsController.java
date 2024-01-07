package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.Assets;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.service.intf.AssetsService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "资产相关接口")
@RequestMapping("/logger")
public class AssetsController {
    private final AssetsService assetsService;

    public AssetsController(AssetsService assetsService) {
        this.assetsService = assetsService;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addAssets")
    @ApiOperation(value = "添加资产",response = CustomResponseEntity.class)
    public String addAssets(@RequestBody @Validated Assets assets, BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            assetsService.addAssets(assets);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+assets.getAssetsName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/getAllAssets")
    @ApiOperation(value = "获取所有的资产",response = CustomResponseEntity.class)
    public String getAllAssets(){
        List<Assets> assets = assetsService.getAllAssets();
        return new CustomResponseEntity(ResponseCode.SUCCESS, JsonUtils.toString(assets)).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/updateAssets")
    @ApiOperation(value = "更新资产信息",response = CustomResponseEntity.class)
    public String updateAssets(@RequestBody @Validated Assets assets,BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            assetsService.updateAssetsById(assets);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+assets.getAssetsName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getAssets")
    @ApiOperation(value = "获取资产详细信息",response = CustomResponseEntity.class)
    public String getAssetsById(@RequestBody Assets assets){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(assetsService.getAssetsById(assets.getId()))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteAssets")
    @ApiOperation(value = "删除资产",response = CustomResponseEntity.class)
    public String deleteAssets(@RequestBody Assets assets){
        try{
            assetsService.deleteAssetsById(assets.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+assets.getAssetsName()+"时出错，请重试！",null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }
}
