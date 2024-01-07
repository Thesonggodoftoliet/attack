package com.littlepants.attack.attackplus.service.impl;

import cn.hutool.json.JSONUtil;
import com.littlepants.attack.attackplus.entity.graph.Asset;
import com.littlepants.attack.attackplus.entity.graph.FirewallPolicy;
import com.littlepants.attack.attackplus.entity.graph.WebInfo;
import com.littlepants.attack.attackplus.repository.AssetRepository;
import com.littlepants.attack.attackplus.service.AssetService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

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

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public List<Asset> getAllAssets() {
        List<Asset> assets = assetRepository.getAll();
        for (Asset asset:assets){
            asset.setWebInfoList(JSONUtil.toList(asset.getWebInfos(), WebInfo.class));
            asset.setFirewallPolicyList(JSONUtil.toList(asset.getFirewallPolicies(), FirewallPolicy.class));
        }
        return assets;
    }

    @Override
    public void createOrUpdateAsset(Asset asset) {
        assetRepository.save(asset);
    }

    @Override
    public void deleteAssetById(Long id) {
        assetRepository.deleteById(id);
    }

    @Override
    public Asset getAssetById(Long id) {
        Asset asset = assetRepository.findById(id).orElse(null);
        if (asset!=null) {
            asset.setWebInfoList(JSONUtil.toList(asset.getWebInfos(), WebInfo.class));
            asset.setFirewallPolicyList(JSONUtil.toList(asset.getFirewallPolicies(), FirewallPolicy.class));
        }
        return asset;
    }

    @Override
    public Asset getAssetByIp(String ip) {
        Asset asset = new Asset();
        asset.setIp(ip);
        return assetRepository.findOne(Example.of(asset)).orElse(null);
    }

    @Override
    public void saveAssetsFromJson() {
        
    }

    @Override
    public void saveEdgesFromCSV() {
        String path = "D:\\Projects\\attack-plus\\src\\main\\resources\\static\\example.csv";

    }

    /**
     * 根据CVE ID获取有此漏洞的所有资产
     * @param cveId String
     * @return List
     */
    @Override
    public List<Asset> getAssetExistVulByVulId(String cveId) {
        return assetRepository.getAssetExistVulByVulId(cveId);
    }

    @Override
    public void createExistRelation(String ip, String cveId,String pluginId) {
        assetRepository.createExistRelation(ip,cveId,pluginId);
    }

    @Override
    public void createExistRelation(String ip, String pluginId) {
        assetRepository.createExistRelation(ip,pluginId);
    }

    @Override
    public void createDeployRelation(String ip, String name) {
        assetRepository.createDeployRelation(ip,name);
    }

    @Override
    public void createDeployRelation(String ip, String pluginId,String version,List<String> ports) {
        assetRepository.createDeployRelation(ip,pluginId,version,ports);
    }

    @Override
    public void createCommunicateRelation(String ip1, String ip2) {
        assetRepository.createCommunicateRelation(ip1,ip2);
    }

    @Override
    public void createBelongRelation(String ip, String name) {
        assetRepository.createBelongRelation(ip, name);
    }

    @Override
    public void deleteAllRelationOfAsset(String ip) {
        assetRepository.deleteAllRelationOfAsset(ip);
    }

    @Override
    public void deleteVulRelation(String ip, String cve_id) {
        assetRepository.deleteVulRelation(ip, cve_id);
    }

    @Override
    public void deleteServiceRelation(String ip, String name) {
        assetRepository.deleteServiceRelation(ip, name);
    }

    @Override
    public void deleteDomainRelation(String ip, String name) {
        assetRepository.deleteDomainRelation(ip, name);
    }

}
