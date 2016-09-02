package com.ansj.vec.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
/**
 * 词神经元继承于神经元
 * @author jf320
 *
 */
public class WordNeuron extends Neuron {
    public String name;
    public double[] syn0 = null; //input->hidden
    public List<Neuron> neurons = null;//路径神经元
    public int[] codeArr = null;

    public List<Neuron> makeNeurons() {
        if (neurons != null) {
            return neurons;
        }
        Neuron neuron = this;
        neurons = new LinkedList<>();
        System.out.println("查找父节点并把路径记下");
        while ((neuron = neuron.parent) != null) {
            neurons.add(neuron);
        }
        System.out.println("反转路径");
        Collections.reverse(neurons);
        codeArr = new int[neurons.size()];
        System.out.println("新建路径");
        for (int i = 1; i < neurons.size(); i++) {
        	System.out.println("获取到第"+i+"个的编码");
        	//其实就是序列01的序列。
            codeArr[i - 1] = neurons.get(i).code;
        }
        System.out.println("获取最后一个的编码");
        codeArr[codeArr.length - 1] = this.code;
        return neurons;
    }

    public WordNeuron(String name, int freq, int layerSize) {
        this.name = name;
        this.freq = freq;
        this.syn0 = new double[layerSize];
        //随机数？
        Random random = new Random();
        for (int i = 0; i < syn0.length; i++) {
            syn0[i] = (random.nextDouble() - 0.5) / layerSize;
        }
    }

}