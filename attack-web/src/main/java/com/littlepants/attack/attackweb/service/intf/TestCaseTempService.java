package com.littlepants.attack.attackweb.service.intf;


import com.littlepants.attack.attackweb.entity.TestCaseTemplate;

import java.util.List;
import java.util.Map;

public interface TestCaseTempService {
    int addTemplate(TestCaseTemplate testCaseTemplate);
    int updateTemplate(TestCaseTemplate testCaseTemplate);
    int deleteTemplateById(String id);

    int cloneTemplateById(String id, String templateName);
    int createTemplateFromTestCase(String id,String templateName);
    TestCaseTemplate getTemplateById(String id);
    List<Map<String,Object>> getAllTemplate();

    Map<String,Object> getTemplatesByPage(int page,int count);
}
