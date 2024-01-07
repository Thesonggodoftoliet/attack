package com.littlepants.attack.attackweb.service.intf;

import com.littlepants.attack.attackweb.entity.Tool;
import com.littlepants.attack.attackweb.entity.Vendor;

import java.util.List;

public interface VendorService {
    int addVendor(Vendor vendor);
    int updateVendorById(Vendor vendor);
    int deleteVendorById(String id);
    Vendor getVendorById(String id);
    List<Vendor> getAllVendor();

    List<Tool> getToolsByVendor(String vendorId);

    int getCountOfTools(String vendorId);
}
