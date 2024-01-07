package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.Tool;
import com.littlepants.attack.attackweb.entity.Vendor;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.ToolService;
import com.littlepants.attack.attackweb.service.intf.VendorService;
import com.littlepants.attack.attackweb.util.JsonUtils;
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
public class VendorController {
    private final ToolService toolService;
    private final VendorService vendorService;

    public VendorController(ToolService toolService, VendorService vendorService) {
        this.toolService = toolService;
        this.vendorService = vendorService;
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/user/getVendorInfo")
    public String getVendorInfo(){
        Map<String, List> map = new HashMap();
        map.put("vendors",vendorService.getAllVendor());
        map.put("tools",toolService.getAllTool());
        return new CustomResponseEntity(ResponseCode.SUCCESS, JsonUtils.toString(map)).toString();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/user/getAllVendor")
    public String getAllVendor(){
        List<Vendor> vendors = vendorService.getAllVendor();
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(vendors)).toString();
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/logger/addVendor")
    public String addVendor(@RequestBody @Validated Vendor vendor, BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()){
            for (ObjectError error:result.getAllErrors()){
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),
                    null).msgToString();
        }
        try {
            vendorService.addVendor(vendor);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+vendor.getVendorName()+"时出错，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),
                    null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/logger/addTool")
    public String addTool(@RequestBody @Validated Tool tool, BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),
                    null).msgToString();
        }
        try {
            toolService.addTool(tool);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+tool.getToolName()+"时出错，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null)
                    .msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/logger/updateTool")
    public String updateTool(@RequestBody @Validated Tool tool,BindingResult result){
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
            toolService.updateTool(tool);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+tool.getToolName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/logger/updateVendor")
    public String updateVendor(@RequestBody @Validated Vendor vendor,BindingResult result){
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
            vendorService.updateVendorById(vendor);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+vendor.getVendorName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/logger/deleteTool")
    public String deleteTool(@RequestBody Tool tool){
        String id = tool.getId();
        try {
            toolService.deleteToolById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+tool.getToolName()+"时出现错误，请重试！",null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/logger/deleteVendor")
    public String deleteVendor(@RequestBody Vendor vendor){
        String id = vendor.getId();
        try {
            vendorService.deleteVendorById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+vendor.getVendorName()+"时出现错误，请重试！"
                    ,null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @GetMapping("/logger/getToolsByVendor")
    @ResponseBody
    public String getToolsByVendor(@RequestBody Vendor vendor){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(vendorService.getToolsByVendor(vendor.getId()))).toString();
    }

    @CrossOrigin
    @GetMapping("/logger/getBlueTools")
    @ResponseBody
    public String getBlueTools(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(toolService.getDifferentTools(true)))
                .toString();
    }

    @CrossOrigin
    @GetMapping("/logger/getRedTools")
    @ResponseBody
    public String getRedTools(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(toolService.getDifferentTools(false)))
                .toString();
    }
}
