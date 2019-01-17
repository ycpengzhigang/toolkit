package com.touna.credit.riskmanagement.hash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NormalHashCluster extends Cluster {
    
    public NormalHashCluster() {
        super();
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
        
    }

    /**
     * 通过域名或ip来删除节点
     */
    @Override
    public void removeNode(Node node) {
        Iterator<Node> nodeIterator = nodes.iterator();
        while (nodeIterator.hasNext()) {
            Node each = nodeIterator.next();
            if (each.getIp().equals(node.getIp()) || each.getDomain().equals(node.getDomain())) {
                nodeIterator.remove();
            }
        }
    }

    /**
     * 这里使用的是string的hash算法
     * 存在的问题是:数据经过hash得到的结果会分布不均匀？
     */
    @Override
    public Node getNode(String key) {
        int hash = Util.getHash(key);
        int index =  hash % nodes.size();
        return nodes.get(index);
    }
    
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        // 如果底层集合在迭代过程中被修改，迭代器的行为是未指定的，除非调用此方法
        Iterator<String> lists = list.iterator();

        while (lists.hasNext()) {
            String each = lists.next();
            if ("d".equals(each)) {
             // list.remove(each); 这个操作相当于修改底层数据underlying collection 
                lists.remove(); // 只能通过迭代器进行删除
            }
        }

        System.out.println(list);
    }
    
}
