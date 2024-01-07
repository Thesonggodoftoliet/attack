package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.Tool;
import com.littlepants.attack.attackweb.entity.Vendor;
import com.littlepants.attack.attackweb.mapper.ToolMapper;
import com.littlepants.attack.attackweb.mapper.VendorMapper;
import com.littlepants.attack.attackweb.service.intf.VendorService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorMapper vendorMapper;
    private final ToolMapper toolMapper;

    public VendorServiceImpl(VendorMapper vendorMapper, ToolMapper toolMapper) {
        this.vendorMapper = vendorMapper;
        this.toolMapper = toolMapper;
    }

    @Override
    public int addVendor(Vendor vendor) {
        vendor.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        vendor.setCreateTime(timestamp);
        vendor.setUpdateTime(timestamp);
        return vendorMapper.insert(vendor);
    }

    @Override
    public int updateVendorById(Vendor vendor) {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        vendor.setUpdateTime(timestamp);
        return vendorMapper.updateById(vendor);
    }

    @Override
    @Transactional
    public int deleteVendorById(String id) {
        List<String> toolIds = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper.eq("vendor_id", id);
        toolIds = toolMapper.selectObjs(queryWrapper);
        if (toolIds.isEmpty()) {
            vendorMapper.deleteById(id);
            return 1;
        }
        int tag = toolMapper.deleteBatchIds(toolIds);
        if (tag != toolIds.size())
            throw new RuntimeException();
        try {
            vendorMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return 1;
    }

    @Override
    public Vendor getVendorById(String id) {
        return vendorMapper.selectById(id);
    }

    @Override
    public List<Vendor> getAllVendor() {
        List<Vendor> vendors = vendorMapper.selectList(null);
        for (Vendor vendor : vendors)
            vendor.setCountOfTools(getCountOfTools(vendor.getId()));
        return vendors;
    }

    @Override
    public List<Tool> getToolsByVendor(String vendorId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vendor_id", vendorId);
        List<Tool> tools = toolMapper.selectList(queryWrapper);
        return tools;
    }

    @Override
    public int getCountOfTools(String vendorId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vendor_id", vendorId);
        return Math.toIntExact(toolMapper.selectCount(queryWrapper));
    }
}
