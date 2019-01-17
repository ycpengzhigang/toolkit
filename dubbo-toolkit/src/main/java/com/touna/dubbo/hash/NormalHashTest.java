package com.touna.credit.riskmanagement.hash;

import java.util.List;

public class NormalHashTest {
    
    public static final String PRE_KEY = "touna";
    
    
    public static void main(String[] args) {
        Cluster cluster = new NormalHashCluster();
        cluster.addNode(new Node("c1.touna.info", "10.0.4.182"));
        cluster.addNode(new Node("c2.touna.info", "10.0.4.183"));
        cluster.addNode(new Node("c3.touna.info", "10.0.4.184"));
        cluster.addNode(new Node("c4.touna.info", "10.0.4.185"));
        
        // key的生成是比较随意的
        for (int i = 0 ; i < 10000 ; i++) { // 加了1万次
            Node node = cluster.getNode(PRE_KEY+i);
            node.put(PRE_KEY+i, "test data");
        }
        
        System.out.println("数据分布如下:");
        
        List<Node> nodes = cluster.getNodes();
        for (Node each : nodes) {
            System.out.println("IP:" + each.getIp() + ",数据量:" + each.getData().size());
        }
        
        // 计算命中率
        int countfirst = 0;
        List<Node> nodeFirst = cluster.getNodes();
        int sizeFirst = nodeFirst.size();
        for (int i = 0 ; i < sizeFirst ; i++) {
            countfirst += nodeFirst.get(i).getData().size();
        }
        
        System.out.println("命中率1:"+ countfirst * 1f / 10000);
        
        // 增加一个节点
//        cluster.addNode(new Node("c5.touna.info", "10.0.4.186"));
//        // 增加一个节点
//        cluster.addNode(new Node("c6.touna.info", "10.0.4.187"));
//        // 增加一个节点
//        cluster.addNode(new Node("c7.touna.info", "10.0.4.188"));
//        // 增加一个节点
//        cluster.addNode(new Node("c8.touna.info", "10.0.4.189"));
        
//        for (int i = 0 ; i < 10000 ; i++) { // 加了1万次
//            Node node = cluster.getNode(PRE_KEY+i);
//            node.put(PRE_KEY+i, "test data");
//        }
//        
//        // 计算命中率
//        int countSencond = 0;
//        List<Node> nodeSecond= cluster.getNodes();
//        int sizeSecond = nodeSecond.size();
//        for (int i = 0 ; i < sizeSecond ; i++) {
//            countSencond += nodeSecond.get(i).getData().size();
//        }
//        
//        System.out.println("命中率2:" + ((countSencond * 1f) / 10000));
//        
        
        // 删除一个节点
        cluster.removeNode(new Node("c4.touna.info", "10.0.4.185"));
        
        for (int i = 0 ; i < 10000 ; i++) { // 加了1万次
            Node node = cluster.getNode(PRE_KEY+i);
            node.put(PRE_KEY+i, "test data");
        }
        
        // 计算命中率
        int countSencond = 0;
        List<Node> nodeSecond= cluster.getNodes();
        int sizeSecond = nodeSecond.size();
        for (int i = 0 ; i < sizeSecond ; i++) {
            countSencond += nodeSecond.get(i).getData().size();
        }
        System.out.println("命中率2:" + ((countSencond * 1f) / 10000));
    }
    
}
