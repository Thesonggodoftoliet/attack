package com.littlepants.attack.attackweb.model;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Agent {
    private String group;
    private int sleep_min;
    private List<String> host_ip_addrs;
    private String paw;
    private int ppid;
    private String created;
    private boolean deadman_enabled;
    private int pid;
    private List<String> available_contacts;
    private int sleep_max;
    private String display_name;
    private String upstream_dest;
    private String server;
    private boolean trusted;
    private List<List<String>> proxy_chain;
    private int watchdog;
    private String host;
    private String pending_contact;
    private String username;
    private String exe_name;
    private List<String> executors;
    private Map<String,List<String>> proxy_receivers;
    private String platform;
    private String last_seen;
    private String contact;
    private String architecture;
    private String location;
    private String origin_link_id;
    private List<Link> links;
    private String privilege;
}
