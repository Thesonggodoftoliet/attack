package com.littlepants.attack.attackweb.service.intf;


import com.littlepants.attack.attackweb.entity.Phase;

import java.util.List;
import java.util.Map;

public interface PhaseService {
    List<Phase> getAllPhase();
    List<Map<String,Object>> getPhases();
}
