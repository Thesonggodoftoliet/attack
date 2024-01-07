package com.littlepants.attack.attackplus.service.impl;

import com.littlepants.attack.attackplus.entity.graph.Service;
import com.littlepants.attack.attackplus.repository.ServiceRepository;
import com.littlepants.attack.attackplus.service.ServiceService;
import org.neo4j.driver.Record;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.domain.Example;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.HashMap;
import java.util.HashSet;
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

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    final private ServiceRepository serviceRepository;
    private final Neo4jClient neo4jClient;

    public ServiceServiceImpl(ServiceRepository serviceRepository, Neo4jClient neo4jClient) {
        this.serviceRepository = serviceRepository;
        this.neo4jClient = neo4jClient;
    }

    @Override
    public List<Service> getAllServices() {
        return serviceRepository.getServices();
    }

    @Override
    public List<Service> getServicesDeployed() {
        return serviceRepository.getServicesByAsset();
    }

    @Override
    public List<Map> getServiceDeployed() {
        List<Map> services = (List<Map>) neo4jClient
                .query("MATCH (s:Service)<-[r:DEPLOY]-(a:Asset) RETURN s,collect(r) as r,elementId(s) as id")
                .fetchAs(Map.class).mappedBy((TypeSystem t, Record r)->{
                    Map<String,Object> result = new HashMap<>();
                    Map<String,Object> map = r.get("s").asMap();
                    Long id = Long.parseLong(r.get("id").asString().split(":")[2]);
                    result.put("id",id);
                    List<String> ports = r.get("r").asList(v-> v.get("ports")
                            .asList(value -> value.asString().split("/")[0].trim()).get(0));
                    HashSet<String> strings = new HashSet<>(ports);
                    StringBuilder stringBuffer = new StringBuilder();
                    for (String s:strings) {
                        stringBuffer.append(s);
                        stringBuffer.append(",");
                    }
                    stringBuffer.deleteCharAt(stringBuffer.length()-1);
                    result.put("port",stringBuffer.toString());
                    result.put("name",map.get("name").toString());
                    result.put("description",map.get("description").toString());
                    result.put("hostNum",ports.size());
                    result.put("pluginId",map.get("plugin_id").toString());
                    return result;
                }).all();
        return services;
    }

    @Override
    public List<Service> getServicesDeployedOnAsset(String ip) {
        return serviceRepository.getServicesByAsset(ip);
    }

    @Override
    public void createService(Service service) {
        serviceRepository.save(service);
    }

    @Override
    public Service getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    @Override
    public Service getServiceByName(String name) {
        Service service = new Service();
        service.setName(name);
        return serviceRepository.findOne(Example.of(service)).orElse(null);
    }

    @Override
    public void updateServiceById(Service service) {
        serviceRepository.save(service);
    }

    @Override
    public void deleteServiceById(Long id) {
        serviceRepository.deleteRelationById(id);
        serviceRepository.deleteServiceById(id);
    }
}
