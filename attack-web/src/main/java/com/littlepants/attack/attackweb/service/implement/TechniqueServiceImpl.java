package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.mapper.TechniqueMapper;
import com.littlepants.attack.attackweb.service.intf.TechniqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TechniqueServiceImpl implements TechniqueService {
    private final TechniqueMapper techniqueMapper;

    public TechniqueServiceImpl(TechniqueMapper techniqueMapper) {
        this.techniqueMapper = techniqueMapper;
    }

    @Override
    public List<Map<String, Object>> getAllTechnique() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id as value, label, category");
        return techniqueMapper.selectMaps(queryWrapper);
    }
}
