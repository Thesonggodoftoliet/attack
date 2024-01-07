package com.littlepants.attack.attackplus.controller;

import cn.hutool.json.JSONUtil;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.dto.TopologyDTO;
import com.littlepants.attack.attackplus.entity.Topology;
import com.littlepants.attack.attackplus.exception.BizException;
import com.littlepants.attack.attackplus.exception.code.ExceptionCode;
import com.littlepants.attack.attackplus.mapper.TopologyMapper;
import com.littlepants.attack.attackplus.service.TopologyService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/topology")
@Api(tags = "")
public class TopologyController {
    private final Logger logger = LoggerFactory.getLogger(TopologyController.class);
    private final TopologyService topologyService;

    public TopologyController(TopologyService topologyService) {
        this.topologyService = topologyService;
    }

    @GetMapping("/getLatestTopology")
    public R<TopologyDTO> getLatestTopology(){
        Topology topology = topologyService.getLatest();
        return R.success(TopologyMapper.INSTANCE.topologyToTopologyDto(topology));
    }

    @GetMapping("/getFullLatestTopology")
    public R<Topology> getFullLatestTopology(){
        Topology topology = topologyService.getLatest();
        System.out.println(JSONUtil.toJsonStr(topology));
        System.out.println(JSONUtil.toJsonStr(R.success(topology)));
        return R.success(topology);
    }

    @GetMapping("/getTopology")
    public R<TopologyDTO> getTopologyByMeta2dId(@RequestParam(value = "meta2dId") String meta2dId){
        Topology topology = topologyService.getByMeta2dId(meta2dId);
        return R.success(TopologyMapper.INSTANCE.topologyToTopologyDto(topology));
    }

    @PostMapping("/updateTopology")
    public R<Boolean> updateTopology(@RequestBody Topology topology){
        boolean tag;
        try {
            Topology sql = topologyService.getByMeta2dId(topology.getMeta2dId());
            if (sql!=null)
                topology.setId(sql.getId());
            topologyService.saveOrUpdate(topology);
        }catch (BizException e){
            return R.fail(e);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

    @PostMapping("/updateScanId")
    public R<Boolean> updateSimpleInfo(@RequestBody Topology topology){

        try {
            Topology sql = topologyService.getByMeta2dId(topology.getMeta2dId());
            if (topology.getScanId()!=null&&topology.getScanId()!=0)
                sql.setScanId(topology.getScanId());
            if (topology.getHostNum()!=null&&topology.getHostNum()!=0)
                sql.setHostNum(topology.getHostNum());
            if ((topology.getScanId()!=null&&topology.getScanId()!=0)||
                    (topology.getHostNum()!=null&&topology.getHostNum()!=0))
                topologyService.updateById(sql);
        }catch (BizException e){
            return R.fail(e);
        }catch (Exception e){
            logger.error(e.getMessage());
            return R.fail(ExceptionCode.SQL_EX);
        }
        return R.success();
    }

}
