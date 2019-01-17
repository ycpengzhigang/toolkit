package com.touna.credit.riskmanagement.hash;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点集群
 * @author PENGZHIGANG
 *
 */
public abstract class Cluster {
    
    protected List<Node> nodes;

    /** 使用的是列表 */
    public Cluster() {
        this.nodes = new ArrayList<>();
    }
    
    /**
     * 往集群中增减节点
     * @param node
     */
    public abstract void addNode(Node node);
    
    
    /**
     * 从集群中删除节点
     * @param node
     */
    public abstract void removeNode(Node node);
    
    
    /**
     * 通过key从集群中获取节点
     * @param node
     */
    public abstract Node getNode(String key);
    
    public List<Node> getNodes() {
        return nodes;
    }

}
