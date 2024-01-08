package com.littlepants.attack.attackplus.listener;

import cn.hutool.json.JSONUtil;
import com.littlepants.attack.attackplus.dto.CaseCalderaDTO;
import com.littlepants.attack.attackplus.service.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/8
 */
@Service
@Slf4j
public class RpcServer {
    private final CaseService caseService;

    public RpcServer(CaseService caseService) {
        this.caseService = caseService;
    }

    @RabbitListener(queues = "rpc.request")
    public String commonRpc(String in){
        Map<String,Object> data = JSONUtil.toBean(in,Map.class);
        log.info("远程调用的方法为{},参数为{}",data.get("method"),data.get("params"));
        if (data.get("method").toString().equals("updateFromCaldera")){
            try {
                List<CaseCalderaDTO> calderaDTOS = JSONUtil.toList(data.get("params").toString(), CaseCalderaDTO.class);
                caseService.updateFromCaldera(calderaDTOS);
            }catch (Exception e){
                log.error(e.getMessage());
                return "Fail";
            }
            return "Success";
        }
        return "Method Not Found";
    }
}
