package com.littlepants.attack.attackplus.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.Host;
import com.littlepants.attack.attackplus.entity.OperationIp;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.service.HostService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 每个场景包含的IP 前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-07-05
 */
@RestController
@RequestMapping("/host")
@Api(tags = "每个场景包含的IP")
public class HostController {
    @Value("${attackplus.server}")
    private String server;
    private final Logger logger = LoggerFactory.getLogger(HostController.class);
    private final HostService hostService;

    public HostController(HostService hostService) {
        this.hostService = hostService;
    }

    @GetMapping("/getAll/{topologyId}")
    public R<List<Host>> getHosts(@PathVariable("topologyId")Long topologyId){
        return R.success(hostService.getHostsByTopology(topologyId));
    }

    @GetMapping("/getDeployed/{topologyId}")
    public R<List<Host>> getDeployedAgents(@PathVariable("topologyId")Long topologyId){
        return R.success(hostService.deployedAgent(topologyId));
    }


    @GetMapping("/get")
    public R<Map<String,Object>> getHostByIp(@RequestParam("ip")String ip,@RequestParam("topologyId")Long topologyId){
        QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
        Map<String,Object> param = new HashMap<>();
        param.put("ip",ip);
        param.put("topology_id",topologyId);
        queryWrapper.allEq(param);
        Map<String,Object> result = new HashMap<>();
        result.put("host",hostService.getOne(queryWrapper));
        result.put("server",server);
        return R.success(result);
    }

    @GetMapping("/get/{topologyId}/{id}")
    public R<Host> getHostById(@PathVariable("id")Long id, @PathVariable("topologyId") Long topologyId){
        QueryWrapper<Host> queryWrapper = new QueryWrapper<>();
        Map<String,Object> param = new HashMap<>();
        param.put("id", id);
        param.put("topology_id", topologyId);
        queryWrapper.allEq(param);
        return R.success(hostService.getOne(queryWrapper));
    }

    @GetMapping("/match/{topologyId}")
    public R<Boolean> match(@PathVariable("topologyId")Long topologyId){
        try {
            hostService.matchPawAndIp(topologyId);
        }catch (NullPointerException e){
          logger.error(e.getMessage());
          return R.fail(ExceptionCode.NULL_POINT_EX);
        } catch (TimeoutException e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/add")
    public R<Boolean> add(@RequestBody Host host){
        try {
            hostService.save(host);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/update")
    public R<Boolean> update(@RequestBody Host host){
        Host sqlHost = new Host();
        sqlHost.setId(host.getId());
        sqlHost.setIp(host.getIp());
        try {
            hostService.updateById(host);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @DeleteMapping("/delete/{hostId}")
    public R<Boolean> deleteByHostId(@PathVariable("hostId")Long hostId){
        try {
            hostService.removeById(hostId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

}
