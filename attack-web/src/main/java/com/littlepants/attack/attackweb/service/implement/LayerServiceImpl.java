package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.littlepants.attack.attackweb.entity.DetectionLayer;
import com.littlepants.attack.attackweb.mapper.LayerMapper;
import com.littlepants.attack.attackweb.service.intf.LayerService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LayerServiceImpl implements LayerService {
    @Autowired
    private LayerMapper layerMapper;
    @Override
    public int addLayer(DetectionLayer detectionLayer) {
        detectionLayer.setId(UUIDGenerator.generateUUID());
        return layerMapper.insert(detectionLayer);
    }

    @Override
    public int updateLayer(DetectionLayer detectionLayer) {
        return layerMapper.updateById(detectionLayer);
    }

    @Override
    public int deleteLayerById(String id) {
        return layerMapper.deleteById(id);
    }

    @Override
    public DetectionLayer getLayerById(String id) {
        return layerMapper.selectById(id);
    }

    @Override
    public List<DetectionLayer> getAllLayers() {
        return layerMapper.selectList(null);
    }

    @Override
    public Map<String, Object> getLayersByPage(int page, int count) {
        Page<DetectionLayer> pages = new Page<>(page,count);
        Page<DetectionLayer> result = layerMapper.selectPage(pages,null);
        Map<String,Object> map = new HashMap<>();
        map.put("data",result.getRecords());
        map.put("count",result.getPages());
        return map;
    }
}
