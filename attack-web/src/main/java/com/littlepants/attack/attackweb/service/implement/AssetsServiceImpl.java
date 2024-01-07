package com.littlepants.attack.attackweb.service.implement;

import com.littlepants.attack.attackweb.entity.Assets;
import com.littlepants.attack.attackweb.mapper.AssetsMapper;
import com.littlepants.attack.attackweb.service.intf.AssetsService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class AssetsServiceImpl implements AssetsService {
    private final AssetsMapper assetsMapper;

    public AssetsServiceImpl(AssetsMapper assetsMapper) {
        this.assetsMapper = assetsMapper;
    }

    @Override
    public int addAssets(Assets assets) {
        assets.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        assets.setCreateTime(timestamp);
        assets.setUpdateTime(timestamp);
        return assetsMapper.insert(assets);
    }

    @Override
    public int updateAssetsById(Assets assets) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        assets.setUpdateTime(timestamp);
        return assetsMapper.updateById(assets);
    }

    @Override
    public int deleteAssetsById(String id) {
        return assetsMapper.deleteById(id);
    }

    @Override
    public Assets getAssetsById(String id) {
        return assetsMapper.selectById(id);
    }

    @Override
    public List<Assets> getAllAssets() {
        return assetsMapper.selectList(null);
    }
}
