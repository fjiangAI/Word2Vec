//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ansj.vec.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
/**
 * 这是用来整合词图的类
 * 主要用处就是在HashMap上再进行整合，让它更符合我们所需要的表现，主要体现在添加上面，还有获得整个词典输出上面
 * @author jf320
 *
 * @param <T>
 */
public class MapCount<T> {
	//
    private HashMap<T, Integer> hm = null;

    public MapCount() {
        this.hm = new HashMap();
    }

    public MapCount(int initialCapacity) {
        this.hm = new HashMap(initialCapacity);
    }
	/**
	 * 这是添加一个元素，他并不是简单的添加，他是看有无元素在集合内，如果在了，就在原来数量上面加1，否则就置为1。
	 * @param t 这是要添加的对象
	 * @param n 这是添加对象的数量
	 */
    public void add(T t, int n) {
        Integer integer = null;
        //
        if((integer = (Integer)this.hm.get(t)) != null) {
            this.hm.put(t, Integer.valueOf(integer.intValue() + n));
        } else {
            this.hm.put(t, Integer.valueOf(n));
        }

    }

    public void add(T t) {
        this.add(t, 1);
    }
    //返回集合的条目
    public int size() {
        return this.hm.size();
    }

    public void remove(T t) {
        this.hm.remove(t);
    }

    public HashMap<T, Integer> get() {
        return this.hm;
    }
    /**
     * 这相当于重写tostring了，不过原来没有tostring，所以他就写了这个获得词典的方法，应该都能看懂吧。
     * @return
     */
    public String getDic() {
        Iterator iterator = this.hm.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        Entry next = null;
        while(iterator.hasNext()) {
            next = (Entry)iterator.next();
            sb.append(next.getKey());
            sb.append("\t");
            sb.append(next.getValue());
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(9223372036854775807L);
    }
}
