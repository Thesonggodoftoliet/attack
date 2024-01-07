package com.littlepants.attack.attackplus.controller;

import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.graph.Service;
import com.littlepants.attack.attackplus.externalModel.caldera.Link;
import com.littlepants.attack.attackplus.service.ServiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/2
 */
@RestController
@RequestMapping("/service")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/getAll")
    public R<Map<String,Object>> getAllService(){
        List<Map> services = serviceService.getServiceDeployed();
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> page = new HashMap<>();
        page.put("pageSize",10);
        page.put("currentPage",1);
        page.put("total",services.size());
        result.put("page",page);
        result.put("serviceList",services);
        return R.success(result);
    }
}
