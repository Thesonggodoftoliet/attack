package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.Assets;

import java.util.List;

public interface AssetsService {
    int addAssets(Assets assets);
    int updateAssetsById(Assets assets);
    int deleteAssetsById(String id);
    Assets getAssetsById(String id);
    List<Assets> getAllAssets();
}
