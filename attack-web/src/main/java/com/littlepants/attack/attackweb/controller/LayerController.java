package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.DetectionLayer;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.LayerService;
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
@RequestMapping("/logger")
public class LayerController {
    private final LayerService layerService;

    public LayerController(LayerService layerService) {
        this.layerService = layerService;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addDetectionLayer")
    public String addDetectionLayer(@RequestBody @Validated DetectionLayer detectionLayer, BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()){
            for (ObjectError error:result.getAllErrors()){
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500, JsonUtils.toString(errors),null).msgToString();
        }
        try {
            layerService.addLayer(detectionLayer);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+detectionLayer.getLayerName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/updateDetectionLayer")
    public String updateDetectionLayer(@RequestBody @Validated DetectionLayer detectionLayer,BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String, Object> msg = new HashMap<>();
                msg.put("error", error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            layerService.updateLayer(detectionLayer);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+detectionLayer.getLayerName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/deleteDetectionLayer")
    public String deleteDetectionLayer(@RequestBody DetectionLayer detectionLayer){
        String id = detectionLayer.getId();
        try {
            layerService.deleteLayerById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+detectionLayer.getLayerName()+"时出现错误，请重试！",
                    null).msgToString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getDetectionLayer")
    public String getDetectionLayer(@RequestBody Map map){
        String id = (String) map.get("id");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(layerService.getLayerById(id))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/getDetectionLayers")
    public String getDetectionLayers(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(layerService.getAllLayers())).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getDetectionLayersByPage")
    public String getDetectionLayersByPage(@RequestBody Map map){
        int page = (int) map.get("page");
        int count = (int) map.get("count");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(layerService.getLayersByPage(page,count))).toString();
    }
}
