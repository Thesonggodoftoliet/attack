package com.littlepants.attack.attackplus.service;


import com.littlepants.attack.attackplus.entity.graph.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/27
 */

public interface ServiceService {
    List<Service> getAllServices();
    List<Service> getServicesDeployed();
    List<Map> getServiceDeployed();
    List<Service> getServicesDeployedOnAsset(String ip);
    void createService(Service service);
    Service getServiceById(Long id);
    Service getServiceByName(String name);
    void updateServiceById(Service service);
    void deleteServiceById(Long id);
}
