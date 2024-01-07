package com.littlepants.attack.attackweb.service.intf;


import com.littlepants.attack.attackweb.entity.TestCase;

public interface TestcaseService {
    int addTestCase(TestCase testCase);

    String addTestCaseFromTemplate(String testcaseTempId,String campaignId);
    TestCase getTestCaseById(String id);
    int updateTestCaseById(TestCase testCase);
    int deleteTestCaseById(String id);
    int cloneTestCaseById(String id);
}
