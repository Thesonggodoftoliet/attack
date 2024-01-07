package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.*;
import com.littlepants.attack.attackweb.response.ResponseCode;
import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import com.littlepants.attack.attackweb.service.UploadService;
import com.littlepants.attack.attackweb.service.intf.PhaseService;
import com.littlepants.attack.attackweb.service.intf.TechniqueService;
import com.littlepants.attack.attackweb.service.intf.TestcaseService;
import com.littlepants.attack.attackweb.util.JsonUtils;
import com.littlepants.attack.attackweb.util.StringProcessUtils;
import com.littlepants.attack.attackweb.validation.BlueTeamValidation;
import com.littlepants.attack.attackweb.validation.EditTeamInfoValidation;
import com.littlepants.attack.attackweb.validation.RedTeamValidation;
import com.littlepants.attack.attackweb.validation.TeamInfoValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/logger")
public class TestcaseController {
    private final TestcaseService testcaseService;
    private final UploadService uploadService;
    private final TechniqueService techniqueService;
    private final PhaseService phaseService;

    public TestcaseController(TestcaseService testcaseService, UploadService uploadService,
                              TechniqueService techniqueService, PhaseService phaseService) {
        this.testcaseService = testcaseService;
        this.uploadService = uploadService;
        this.techniqueService = techniqueService;
        this.phaseService = phaseService;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/addTestCase")
    public String addTestCase(@RequestBody @Validated({TeamInfoValidation.class,BlueTeamValidation.class,
            RedTeamValidation.class}) TestCase testCase, BindingResult result){
//        System.out.println(testCase);
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
            testcaseService.addTestCase(testCase);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","添加"+testCase.getTestcaseName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/getTestCase")
    public String getTestCaseById(@RequestBody Map map){
        String id = (String) map.get("id");
        TestCase testCase =  testcaseService.getTestCaseById(id);
        return new CustomResponseEntity(ResponseCode.SUCCESS, JsonUtils.toString(testCase)).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PutMapping("/updateTestCase")
    public String updateTestCase(@RequestBody @Validated({EditTeamInfoValidation.class,RedTeamValidation.class,
            BlueTeamValidation.class}) TestCase testCase,
                                 BindingResult result){
        List<Map<String,Object>> errors = new ArrayList<>();
        if (result.hasErrors()){
            for (ObjectError error:result.getAllErrors()) {
                Map<String,Object> msg = new HashMap<>();
                msg.put("error",error.getDefaultMessage());
                errors.add(msg);
            }
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        }
        try {
            testcaseService.updateTestCaseById(testCase);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> msg = new HashMap<>();
            msg.put("error","修改"+testCase.getTestcaseName()+"时出现错误，请重试！");
            errors.add(msg);
        }
        if (!errors.isEmpty())
            return new CustomResponseEntity(500,JsonUtils.toString(errors),null).msgToString();
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/delTestCase")
    public String deleteTestCase(@RequestBody TestCase testCase){
        try {
            testcaseService.deleteTestCaseById(testCase.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"删除"+testCase.getTestcaseName()+"时出现错误，请重试！",null)
                    .toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public String uploadFile(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            return new CustomResponseEntity(500,"文件为空",null).toString();
        String contentType = multipartFile.getContentType();
        InputStream inputStream = multipartFile.getInputStream();
        byte str[] = new byte[10];
        inputStream.read(str,0,str.length);
        String code = StringProcessUtils.bytesToHexString(str);
//        System.out.println(code);
        if (!contentType.equals("image/jpeg")&&!contentType.equals("image/png"))
            return new CustomResponseEntity(500,"仅支持jpg或png",null).toString();
        if (!code.equals("89504e470d0a1a0a0000")&&!code.equals("ffd8ffe000104a464946"))
            return new CustomResponseEntity(500,"仅支持jpg或png",null).toString();
        String result = uploadService.uploadImg(multipartFile,"/img");
        if (result.equals("error"))
            return new CustomResponseEntity(500,"上传"+multipartFile.getName()+"失败",null).toString();
        Map<String,Object> msg= new HashMap<>();
        msg.put("url",result);
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(msg)).toString();
    }

    @ResponseBody
    @CrossOrigin
    @PostMapping("/cloneTestCase")
    public String cloneTestCase(@RequestBody TestCase testCase){
        try {
            testcaseService.cloneTestCaseById(testCase.getId());
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponseEntity(500,"克隆"+testCase.getTestcaseName()+"时出错,请重试！",
                    null).toString();
        }
        return new CustomResponseEntity(ResponseCode.SUCCESS,null).toString();
    }

    @ResponseBody
    @CrossOrigin
    @GetMapping("/getTechniques")
    public String getTechniques(){
        List<Map<String,Object>> techniques = techniqueService.getAllTechnique();
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(techniques)).toString();
    }

    @ResponseBody
    @CrossOrigin
    @GetMapping("/getPhases")
    public String getPhase(){
        List<Map<String,Object>> list= phaseService.getPhases();
        return new CustomResponseEntity(ResponseCode.SUCCESS,JsonUtils.toString(list)).toString();
    }

}
