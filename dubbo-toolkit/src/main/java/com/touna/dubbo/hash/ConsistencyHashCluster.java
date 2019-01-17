package com.touna.credit.riskmanagement.hash;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
/**
 * 通过增加虚拟节点解决没有虚拟节点的水平扩展问题
 * @author PENGZHIGANG
 *
 */
public class ConsistencyHashCluster extends Cluster{
    
    private SortedMap<Integer, Node> virNodes = new TreeMap<Integer, Node>();
    
    /** 虚拟节点的数量  */
    private static final int VIR_NODE_COUNT = 512;
    
    /** 虚拟节点拼接符  */
    private static final String SPLIT = "#";
   
    public ConsistencyHashCluster() {
        super();
    }

    /**
     * 增加节点得同时增加真实节点和虚拟节点
     */
    @Override
    public void addNode(Node node) {
        nodes.add(node);
        
        /*
         * 将实际节点进行拆分为如下的格式:
         * 10.0.4.185#0
         * 10.0.4.185#1
         * 10.0.4.185#2
         * 10.0.4.185#3
         */
        for (int i = 0 ; i < VIR_NODE_COUNT ; i++) {
            int hash = Util.getHash(node.getIp() + SPLIT + i);
            virNodes.put(hash, node);
        }
    }

    /**
     * 删除节点得同时删除真实节点和虚拟节点
     */
    @Override
    public void removeNode(Node node) {
        // 删除真实节点
        Iterator<Node> nodeIterator = nodes.iterator();
        while (nodeIterator.hasNext()) {
            Node each = nodeIterator.next();
            if (each.getIp().equals(node.getIp()) || each.getDomain().equals(node.getDomain())) {
                nodeIterator.remove();
            }
        }
        
        // 删除虚拟节点
        for (int i = 0 ; i < VIR_NODE_COUNT ; i++) {
            int hash = Util.getHash(node.getIp() + SPLIT + i);
            virNodes.remove(hash);
        }
    }

    @Override
    public Node getNode(String key) {
       int hash = Util.getHash(key);
       SortedMap<Integer, Node> subMap = (hash >= virNodes.lastKey() ? virNodes.tailMap(0) : virNodes.tailMap(hash));
       if (subMap.isEmpty()) {
           return null;
       }
        
       return subMap.get(subMap.firstKey());
    }
    
}
