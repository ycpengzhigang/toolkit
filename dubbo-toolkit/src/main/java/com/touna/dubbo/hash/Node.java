package com.touna.credit.riskmanagement.hash;

import java.util.HashMap;
import java.util.Map;

public class Node {
    
    /** 节点域名 */
    private String domain;
    
    /** 节点ip */
    private String ip;
    
    /** 节点存的数据 */
    private Map<String,Object> data;

    public Node(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
        this.data = new HashMap<>();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * 在节点增加数据
     */
    public <T> void put(String key, T value) {
        data.put(key, value);
    }

    /**
     * 通过key删除节点数据
     * @param key
     */
    public void remove(String key){
        data.remove(key);
    }

    /**
     * 通过可以获取节点上的数据
     * @param key
     * @return
     */
    public <T> T get(String key) {
        return (T) data.get(key);
    }
}
