package com.littlepants.attack.attackweb.service.intf;


import com.littlepants.attack.attackweb.entity.DetectionLayer;

import java.util.List;
import java.util.Map;

public interface LayerService {
    int addLayer(DetectionLayer detectionLayer);
    int updateLayer(DetectionLayer detectionLayer);
    int deleteLayerById(String id);
    DetectionLayer getLayerById(String id);
    List<DetectionLayer> getAllLayers();
    Map<String,Object> getLayersByPage(int page, int count);
}
