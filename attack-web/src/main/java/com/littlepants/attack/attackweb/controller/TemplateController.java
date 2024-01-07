package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.AssessmentTemplate;
import com.littlepants.attack.attackweb.entity.CampaignTemplate;
import com.littlepants.attack.attackweb.entity.TestCaseTemplate;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.intf.AssessmentTemplateService;
import com.littlepants.attack.attackweb.service.intf.CampaignTempService;
import com.littlepants.attack.attackweb.service.intf.TestCaseTempService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.validation.BlueTeamValidation;
import com.littlepants.attack.attackweb.validation.RedTeamValidation;
import com.littlepants.attack.attackweb.validation.TestCaseTempValidation;
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
@RequestMapping("/admin")
public class TemplateController {

    private final TestCaseTempService testCaseTempService;
    private final CampaignTempService campaignTempService;
    private final AssessmentTemplateService assessmentTemplateService;

    public TemplateController(TestCaseTempService testCaseTempService, CampaignTempService campaignTempService,
                              AssessmentTemplateService assessmentTemplateService) {
        this.testCaseTempService = testCaseTempService;
        this.campaignTempService = campaignTempService;
        this.assessmentTemplateService = assessmentTemplateService;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addTestCaseTemplate")
    public String addTestCaseTemplate(@RequestBody @Validated({TestCaseTempValidation.class, RedTeamValidation.class,
            BlueTeamValidation.class})TestCaseTemplate testCaseTemplate, BindingResult result){
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
            testCaseTempService.addTemplate(testCaseTemplate);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+testCaseTemplate.getTemplateName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addCampaignTemplate")
    public String addCampaignTemplate(@RequestBody @Validated CampaignTemplate campaignTemplate,BindingResult result){
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
            campaignTempService.addCampaignTemplate(campaignTemplate);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+campaignTemplate.getTemplateName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addAssessmentTemplate")
    public String addAssessmentTemplate(@RequestBody @Validated AssessmentTemplate assessmentTemplate,
                                        BindingResult result){
        List<String> errors = new ArrayList<>();
        if (result.hasErrors())
            for (ObjectError error:result.getAllErrors())
                errors.add(error.getDefaultMessage());
        try {
            assessmentTemplateService.addTemplate(assessmentTemplate);
        }catch (Exception e){
            e.printStackTrace();
            errors.add("添加"+assessmentTemplate.getTemplateName()+"时发生错误，请重试！");
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,errors.toString(),null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/updateTestCaseTemplate")
    public String updateTestCaseTemplate(@RequestBody @Validated({TestCaseTempValidation.class,RedTeamValidation.class,
            BlueTeamValidation.class})TestCaseTemplate testCaseTemplate,BindingResult result){
        List<Map<String,Object>> errors  = new ArrayList<>();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            testCaseTempService.updateTemplate(testCaseTemplate);
        }catch (DuplicateKeyException e){
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","模板名重复");
            errors.add(msg);
        } catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+testCaseTemplate.getTemplateName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/updateCampaignTemplate")
    public String updateCampaignTemplate(@RequestBody @Validated CampaignTemplate campaignTemplate,
                                         BindingResult result){
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
            campaignTempService.updateCampaignTemplate(campaignTemplate);
        }catch (DuplicateKeyException e){
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","模板名重复");
            errors.add(msg);
        } catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+campaignTemplate.getTemplateName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/updateAssessmentTemplate")
    public String updateAssessmentTemplate(@RequestBody @Validated AssessmentTemplate assessmentTemplate,
                                           BindingResult result){
        List<String> errors = new ArrayList<>();
        if (result.hasErrors()){
            for (ObjectError error:result.getAllErrors())
                errors.add(error.getDefaultMessage());
        }else {
            try {
                assessmentTemplateService.updateTemplate(assessmentTemplate);
            }catch (Exception e){
                e.printStackTrace();
                errors.add("修改"+assessmentTemplate.getTemplateName()+"时出错，请重试！");
            }
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,errors.toString(),null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/deleteTestCaseTemplate")
    public String deleteTestCaseTemplate(@RequestBody TestCaseTemplate testCaseTemplate){
        String id = testCaseTemplate.getId();
        try {
            testCaseTempService.deleteTemplateById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+testCaseTemplate.getTemplateName()+"时出现错误，请重试！",
                    null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/deleteCampaignTemplate")
    public String deleteCampaignTemplate(@RequestBody CampaignTemplate campaignTemplate){
        String id = campaignTemplate.getId();
        try {
            campaignTempService.deleteCampaignTemplateById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+campaignTemplate.getTemplateName()+"时出现错误，请重试！",
                    null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @DeleteMapping("/deleteAssessmentTemplate")
    public String deleteAssessmentTemplate(@RequestBody AssessmentTemplate assessmentTemplate){
//        String id = assessmentTemplate.getId();
        try {
            assessmentTemplateService.deleteTemplate(assessmentTemplate);
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+assessmentTemplate.getTemplateName()+"时出现错误，请重试！",
                    null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/cloneTestCaseTemplate")
    public String cloneTestCaseTemplate(@RequestBody @Validated({TestCaseTempValidation.class})
                                        TestCaseTemplate testCaseTemplate,BindingResult result){
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
            testCaseTempService.cloneTemplateById(testCaseTemplate.getId(),testCaseTemplate.getTemplateName());
        }catch (Exception e){
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","克隆"+testCaseTemplate.getTemplateName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/cloneCampaignTemplate")
    public String cloneCampaignTemplate(@RequestBody CampaignTemplate campaignTemplate){
        List<Map<String,Object>> errors = new ArrayList<>();
        try {
            campaignTempService.cloneCampaignTemplateById(campaignTemplate.getId());
        }catch (Exception e){
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","克隆"+campaignTemplate.getTemplateName()+"时出现错误，请重试！");
            errors.add(msg);
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getTestCaseTemplate")
    public String getTestCaseTemplate(@RequestBody Map map){
        String id = (String) map.get("id");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(testCaseTempService.getTemplateById(id))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getCampaignTemplate")
    public String getCampaignTemplate(@RequestBody Map map){
        String id = (String) map.get("id");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(campaignTempService.getTemplateById(id))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/getAssessmentTemplate")
    public String getAssessmentTemplate(@RequestBody Map map){
        String id = (String) map.get("id");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(assessmentTemplateService.getTemplateById(id))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/getAllTestCaseTemplates")
    public String getAllTestCaseTemplates(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(testCaseTempService.getAllTemplate())).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getTestcaseTempByPage")
    public String getTestcaseTemplatesByPage(@RequestBody Map map){
        int page = (int) map.get("page");
        int count = (int) map.get("count");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(testCaseTempService.getTemplatesByPage(page,count))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/getAllCampaignTemplates")
    public String getAllCampaignTemplates(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(campaignTempService.getAllTemplates())).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getCampaignTempDetail")
    public String getCampaignTempDetail(@RequestBody Map map){
        String id = (String) map.get("id");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(campaignTempService.getTestcaseTempByCampaignId(id))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getCampaignTempByPage")
    public String getCampaignTemplatesByPage(@RequestBody Map map){
        int page = (int) map.get("page");
        int count = (int) map.get("count");
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(campaignTempService.getTemplatesByPage(page,count))).toString();
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/getAllAssessmentTemplates")
    public String getAllAssessmentTemplates(){
        return new CustomResponseEntity(ResponseCode.SUCCESS,
                JsonUtils.toString(assessmentTemplateService.getAllTemplates())).toString();
    }
    @CrossOrigin
    @ResponseBody
    @PostMapping("/createTemplateFromTestCase")
    public String createTemplateFromTestCase(@RequestBody @Validated({TestCaseTempValidation.class})
                                             TestCaseTemplate testCaseTemplate,BindingResult result){
        List<String> errors = new ArrayList<>();
        if (result.hasErrors())
            for (ObjectError error:result.getAllErrors())
                errors.add(error.getDefaultMessage());
        try {
            testCaseTempService.createTemplateFromTestCase(testCaseTemplate.getId(),testCaseTemplate.getTemplateName());
        }catch (Exception e){
            e.printStackTrace();
            errors.add("创建模板"+testCaseTemplate.getTemplateName()+"时出现错误，请重试！");
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,errors.toString(),null).toString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

}
