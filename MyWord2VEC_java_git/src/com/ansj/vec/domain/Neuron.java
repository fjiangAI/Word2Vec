package com.ansj.vec.domain;
/**
 * 这是一个抽象类，意思是神经元
 * @author jf320
 *
 */
public abstract class Neuron implements Comparable<Neuron> {
    //这是节点的频率
	public int freq;
	//这是节点的父节点
    public Neuron parent;
    //这是节点的编号
    public int code;
    /**
     * 重写了一下比较类，主要比较的是频率大小
     */
    @Override
    public int compareTo(Neuron o) {
        // TODO Auto-generated method stub
        if (this.freq > o.freq) {
            return 1;
        } else {
            return -1;
        }
    }

}
