package com.touna.dubbo.consumer;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import sun.misc.Unsafe;

/**
 * unsafe 测试
 * @author PENGZHIGANG
 *
 */
public class UnsafeTest {
    
    private static final Unsafe THE_UNSAFE;
    
    static {
        final PrivilegedExceptionAction<Unsafe> action = new PrivilegedExceptionAction<Unsafe>() {

            @Override
            public Unsafe run() throws Exception {
                // 获取申明的字段
                Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
                // 设置为可见
                unsafe.setAccessible(true);
                return (Unsafe) unsafe.get(null);
            }
        };
        try {
            THE_UNSAFE = AccessController.doPrivileged(action);
        } catch (PrivilegedActionException e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    /**
     * 获取unsafe实例
     * @return
     */
    public static Unsafe getUnsafe(){
        return THE_UNSAFE;
    }

    public static void main(String[] args){
//        int[] arr = {1,2,3,4,5,6,7,8,9,10};
        
//        Unsafe unsafe = getUnsafe(); 
        
        // 通过unsafe操作数组
        // 返回当前数组第一个元素地址相对于数组起始地址的偏移值
//        int b = unsafe.arrayBaseOffset(int[].class);
        
        // 获取偏移地址 通过这个来定位偏移地址
        // 返回当前数组一个元素占用的字节数
//        long s = unsafe.arrayIndexScale(long[].class);
        
//        System.out.println(b +"," + s);
        // 操作数组 偏移 9 索引是从0开始计算
//        unsafe.putInt(arr, (long)(b + s * 9), 100);
//        for(int i=0;i<10;i++){
//            int v = unsafe.getInt(arr, (long)b + s * i);
//            System.out.print(v + " ");
//        }
        // 数组测试
//        int[] src = {1,2,3,4,5,6,7,8,9,10};
        // 扩容
//        int[] tar = Arrays.copyOf(src, src.length + 10);
//        System.out.println(tar[9]);
        
        // 截取数组中的一部分 会排除前一个
//        int[] spilt = Arrays.copyOfRange(src,9,10);
        
//        for (int i = 0; i < spilt.length; i ++) {
//            System.out.println(spilt[i]);
//        }
        
        // 原子更新测试
        AtomicReferenceFieldUpdater<Data, Person> updater = AtomicReferenceFieldUpdater.newUpdater(Data.class, Person.class, "person");
        Data data = new Data();
        Person person = new Person("Jack", 128);
        
        // 这里替换的是地址
        if (updater.compareAndSet(data, data.person, person)) {
            System.out.println(data.person);
        }
    }
    
}


class Person {
    private String name;
    
    private Integer age;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public Person() {
        super();
    }

    public Person(String name, Integer age) {
        super();
        this.name = name;
        this.age = age;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + "]";
    }
}

class Data {
    public volatile Person person = new Person("Lucy", 256);
}   



