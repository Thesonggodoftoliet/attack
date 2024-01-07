package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.AccessLog;
import com.littlepants.attack.attackweb.mapper.LogMapper;
import com.littlepants.attack.attackweb.service.intf.LogService;
import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    private final LogMapper logMapper;

    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }

    @Override
    public int addLog(AccessLog accessLog) {
        accessLog.setId(UUIDGenerator.generateUUID());
        Timestamp timestamp = new Timestamp(new Date().getTime());
        accessLog.setCreateTime(timestamp);
        return logMapper.insert(accessLog);
    }

    @Override
    public int deleteLogBefore(Timestamp timestamp) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.le("create_time",timestamp);
        return logMapper.delete(queryWrapper);
    }

    @Override
    public List<AccessLog> getAccessLog() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return logMapper.selectList(queryWrapper);
    }
}
