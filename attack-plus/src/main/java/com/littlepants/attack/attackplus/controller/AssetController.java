package com.littlepants.attack.attackplus.controller;

import cn.hutool.json.JSONUtil;
import com.littlepants.attack.attackplus.base.R;
import com.littlepants.attack.attackplus.entity.graph.Asset;
import com.littlepants.attack.attackplus.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/11/6
 */
@RestController
@RequestMapping("/asset")
@Slf4j
public class AssetController {
    private final AssetService assetService;
    public AssetController(AssetService assetService){
        this.assetService = assetService;
    }

    @GetMapping("/getAll")
    public R<List<Asset>> getAllAssets(){
        return R.success(assetService.getAllAssets());
    }

    @GetMapping("/getAll/{page}/{count}")
    public R<Map<String,Object>> getAllAssetsPage(@PathVariable Integer page,@PathVariable Integer count){
        List<Asset> assets = assetService.getAllAssets();
        List<Asset> results = null;
        if (assets.size()<=count)
            results =assets;
        else
            results = assets.subList(page*(count-1),page*count);
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> pages = new HashMap<>();
        pages.put("current",page);
        pages.put("total",assets.size());
        pages.put("size",count);
        result.put("page",pages);
        result.put("result",results);
        return R.success(result);
    }

    @GetMapping("/{id}")
    public R<Asset> getAsset(@PathVariable Long id){
        Asset asset = assetService.getAssetById(id);
        return R.success(asset);
    }
}
