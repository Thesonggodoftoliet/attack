package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.Assessment;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.AssessmentService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
@Api(tags = "评估报告相关接口")
public class AssessmentController {
    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @CrossOrigin
    @GetMapping("/getAllAssessments")
    @ResponseBody
    @ApiOperation(value = "获取所有的行动报告",response = CustomResponseEntity.class)
    public String getAllAssessments(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(assessmentService.getAllAssessments())).toString();
    }

    @CrossOrigin
    @PostMapping("/getAssessment")
    @ResponseBody
    @ApiOperation(value = "获取单个评估报告简略信息",response = CustomResponseEntity.class)
    public String getAssessment(@RequestBody @Validated Assessment assessment){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(assessmentService.getAssessmentById(assessment.getId()))).toString();
    }

    @CrossOrigin
    @PostMapping("/getAssessmentDetail")
    @ResponseBody
    @ApiOperation(value = "获取单个评估报告详细信息",response = CustomResponseEntity.class)
    public String getAssessmentDetail(@RequestBody @Validated Assessment assessment){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(assessmentService.getCampaignsById(assessment.getId()))).toString();
    }

    @CrossOrigin
    @PostMapping("/addAssessment")
    @ResponseBody
    @ApiOperation(value = "添加评估报告",response = CustomResponseEntity.class)
    public String addAssessment(@RequestBody @Validated Assessment assessment, BindingResult result){
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
            assessmentService.addAssessment(assessment);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+assessment.getAssessName()+"时出现错误，请重试！");
            errors.add(msg);
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @PostMapping("/updateAssessment")
    @ResponseBody
    @ApiOperation(value = "修改评估报告",response = CustomResponseEntity.class)
    public String updateAssessment(@RequestBody @Validated Assessment assessment,BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String,Object>  msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            assessmentService.updateAssessment(assessment);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+assessment.getAssessName()+"时发生错误，请重试！");
            errors.add(msg);
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @DeleteMapping("/deleteAssessment")
    @ResponseBody
    @ApiOperation(value = "删除评估报告",response = CustomResponseEntity.class)
    public String deleteAssessment(@RequestBody Assessment assessment){
        try {
            assessmentService.delAssessmentById(assessment.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,
                    "删除"+assessment.getAssessName()+"时发生错误，请重试！",null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @PostMapping("/cloneAssessment")
    @ResponseBody
    @ApiOperation(value = "克隆评估报告",response = CustomResponseEntity.class)
    public String cloneAssessment(@RequestBody Assessment assessment){
        try {
            assessmentService.cloneAssessmentById(assessment.getId());
        }catch (DuplicateKeyException e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"评估组名重复",null).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"克隆"+assessment.getAssessName()+"时出错，请重试！",null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }
}
