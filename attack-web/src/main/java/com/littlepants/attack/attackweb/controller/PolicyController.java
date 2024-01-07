package com.littlepants.attack.attackweb.controller;

import com.littlepants.attack.attackweb.entity.PolicyGroup;
import com.littlepants.attack.attackweb.service.PolicyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PolicyController {
    @Resource
    PolicyService policyService;
    @GetMapping("/getAll")
    public void getAll(){
        PolicyGroup policyGroup = policyService.getPolicyGroupById(1);
        System.out.println(policyGroup.toString());
    }
}
