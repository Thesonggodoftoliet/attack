package com.littlepants.attack.attackplus.service;


import com.littlepants.attack.attackplus.entity.graph.Asset;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/27
 */

public interface AssetService{
    List<Asset> getAllAssets();
    void createOrUpdateAsset(Asset asset);
    void deleteAssetById(Long id);
    Asset getAssetById(Long id);
    Asset getAssetByIp(String ip);
    void saveAssetsFromJson();
    void saveEdgesFromCSV();
    List<Asset> getAssetExistVulByVulId(String cveId);
    void createExistRelation(String ip,String cveId,String pluginId);
    void createExistRelation(String ip,String pluginId);
    void createDeployRelation(String ip,String name);
    void createDeployRelation(String ip,String pluginId,String version,List<String> ports);
    void createCommunicateRelation(String ip1,String ip2);
    void createBelongRelation(String ip,String name);
    void deleteAllRelationOfAsset(String ip);
    void deleteVulRelation(String ip,String cve_id);
    void deleteServiceRelation(String ip,String name);
    void deleteDomainRelation(String ip,String name);
}
