package com.littlepants.attack.attackplus.listener;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.generator.UUIDGenerator;
import cn.hutool.json.JSONUtil;
import com.littlepants.attack.attackplus.annotation.MultiTransaction;
import com.littlepants.attack.attackplus.entity.Host;
import com.littlepants.attack.attackplus.entity.OperationIp;
import com.littlepants.attack.attackplus.entity.Timeline;
import com.littlepants.attack.attackplus.entity.Topology;
import com.littlepants.attack.attackplus.entity.graph.Asset;
import com.littlepants.attack.attackplus.entity.graph.WebInfo;
import com.littlepants.attack.attackplus.service.*;
import com.littlepants.attack.attackplus.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 队列接收器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/6/5
 */

@Service
public class MQReceiver {
    private static final Logger logger = LoggerFactory.getLogger(MQReceiver.class);
    private final TimelineService timelineService;
    private final AssetService assetService;
    private final TopologyService topologyService;
    private final HostService hostService;

    public MQReceiver(TimelineService timelineService, AssetService assetService,
                      TopologyService topologyService,  HostService hostService) {
        this.timelineService = timelineService;
        this.assetService = assetService;
        this.topologyService = topologyService;
        this.hostService = hostService;
    }

    @RabbitListener(queues = "#{nessusQueue.name}")
    public void receiveNessus(String in){
        //commonReceive(in);
        nessusReceive(in);
    }

    @RabbitListener(queues = "#{calderaQueue.name}")
    public void receiveCaldera(String in){
        timelineReceive(in);
    }

    public void commonReceive(String in){
        System.out.println(in);
        try {
            List<Map> result = JSONUtil.toList(in, Map.class);
            assert result != null;
            for (Map map:result)
                System.out.println(map.size());
        }catch (Exception e){
            logger.error(e.toString());
        }
    }

    @MultiTransaction
    public void nessusReceive(String in){
        Topology topology = topologyService.getLatest();
        try {
            List<Map> result = JSONUtil.toList(in,Map.class);
            assert  result!=null;
            List<Host> hosts = new ArrayList<>();
            for (Map map:result){
                Asset asset = new Asset();
                asset.setOs((String) map.get("os"));
                asset.setIp((String) map.get("ip"));
                asset.setName((String) map.get("host_name"));
                asset.setHostId((Integer) map.get("host_id"));
                List<WebInfo> webInfos = new ArrayList<>();
                for (Map map1:(List<Map<String,Object>>)map.get("webinfo")){
                    webInfos.add(new WebInfo(map1));
                }
                asset.setWebInfos(JSONUtil.toJsonStr(webInfos));
                assetService.createOrUpdateAsset(asset);
                Host host = new Host();
                host.setIp(asset.getIp());
                host.setTopologyId(topology.getId());
                host.setGroupName(UUID.randomUUID().toString());
                hosts.add(host);
            }
            for (Map map:result){
                List<Integer> vuls = (List<Integer>) map.get("vuls");
                for (Integer pluginId:vuls)
                    assetService.createExistRelation((String) map.get("ip"), String.valueOf(pluginId));
                List<Map<String,Object>> services = (List<Map<String,Object>>) map.get("services");
                for (Map<String,Object> service:services)
                    assetService.createDeployRelation(String.valueOf(map.get("ip")), String.valueOf(service.get("plugin_id")),
                            String.valueOf(service.get("version")), (List<String>) service.get("ports"));
            }
            hostService.saveBatch(hosts);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public void timelineReceive(String in){
        try {
            Map<String,Object> result = JsonUtil.toMap(in,String.class,Object.class);
            assert result != null;
            String dateString = (String) result.get("time");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateString = dateString.replace("T"," ");
            dateString = dateString.replace("Z","");
            Timeline timeline = new Timeline( Long.valueOf(result.get("operation_id").toString()),
                    (String) result.get("filed"), (String) result.get("action"));
            timeline.setTimeline(LocalDateTime.parse(
                    dateString.replace("Z","UTC"),dateTimeFormatter).plusHours(8L));
            timelineService.save(timeline);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
