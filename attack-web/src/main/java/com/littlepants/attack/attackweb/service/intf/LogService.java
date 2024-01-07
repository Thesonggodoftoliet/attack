package com.littlepants.attack.attackweb.service.intf;


import com.littlepants.attack.attackweb.entity.AccessLog;

import java.sql.Timestamp;
import java.util.List;

public interface LogService {
    int addLog(AccessLog accessLog);
    int deleteLogBefore(Timestamp timestamp);
    List<AccessLog> getAccessLog();
}
