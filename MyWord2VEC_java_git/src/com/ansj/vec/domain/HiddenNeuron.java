package com.ansj.vec.domain;
/**
 * 这是隐藏节点，继承于神经元的，其多出的部分也就是输出的部分，是一个数组
 * @author jf320
 *
 */
public class HiddenNeuron extends Neuron{
    
    public double[] syn1 ; //hidden->out
    
    public HiddenNeuron(int layerSize){
        syn1 = new double[layerSize] ;
    }
    
}
