package com.littlepants.attack.attackweb.service;

import com.littlepants.attack.attackweb.entity.PolicyGroup;
import com.littlepants.attack.attackweb.mapper.PolicyMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyService {
    @Setter
    PolicyMapper policyMapper;

    public int addPolicyGroup(PolicyGroup policyGroup){
        return policyMapper.insert(policyGroup);
    }
    public int updatePolicyGroup(PolicyGroup policyGroup){
        return policyMapper.updateById(policyGroup);
    }
    public int deletePolicyGroup(Integer id){
        return policyMapper.deleteById(id);
    }
    public PolicyGroup getPolicyGroupById(Integer id){
        return policyMapper.selectById(id);
    }
    public List<PolicyGroup> getAllPolicyGroups(){
        return policyMapper.selectList(null);
    }
}
