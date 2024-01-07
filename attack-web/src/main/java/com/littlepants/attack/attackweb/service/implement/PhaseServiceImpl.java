package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.Phase;
import com.littlepants.attack.attackweb.mapper.PhaseMapper;
import com.littlepants.attack.attackweb.service.intf.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PhaseServiceImpl implements PhaseService {
    private final PhaseMapper phaseMapper;

    public PhaseServiceImpl(PhaseMapper phaseMapper) {
        this.phaseMapper = phaseMapper;
    }

    @Override
    public List<Phase> getAllPhase() {
        return phaseMapper.selectList(null);
    }

    @Override
    public List<Map<String, Object>> getPhases() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id as value","phase_name as label");
        List<Map<String,Object>> list = phaseMapper.selectMaps(queryWrapper);
        return list;
    }
}
