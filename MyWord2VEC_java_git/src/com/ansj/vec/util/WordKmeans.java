package com.ansj.vec.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import coms.Word2VEC;

/**
 * 这是一个聚类算法
 * 
 * @author ansj
 * @explain jf
 */
public class WordKmeans {

	/**
	 * 这是主函数，用来聚类的，使用的是K-means聚类算法
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        //创建一个W2V对象
    	Word2VEC vec = new Word2VEC();
    	//加载谷歌模型，这里可以自己设置自己的模型
        vec.loadGoogleModel("vectors.bin");
        System.out.println("load model ok!");
        //使用聚类算法进行聚类
        WordKmeans wordKmeans = new WordKmeans(vec.getWordMap(), 50, 50);
        Classes[] explain = wordKmeans.explain();
        for (int i = 0; i < explain.length; i++) {
            System.out.println("--------" + i + "---------");
            System.out.println(explain[i].getTop(10));
        }

    }
    private HashMap<String, float[]> wordMap = null;
    private int iter;
    private Classes[] cArray = null;
    /**
     * 初始化整个聚类，包括整个词图和行列大小
     * @param wordMap
     * @param clcn
     * @param iter
     */
    public WordKmeans(HashMap<String, float[]> wordMap, int clcn, int iter) {
        
    	this.wordMap = wordMap;
        this.iter = iter;
        cArray = new Classes[clcn];
    }
    /**
     * 
     * @return 这返回的是
     */
    public Classes[] explain() {
        //
        Iterator<Entry<String, float[]>> iterator = wordMap.entrySet().iterator();
        for (int i = 0; i < cArray.length; i++) {
            Entry<String, float[]> next = iterator.next();
            cArray[i] = new Classes(i, next.getValue());
        }

        for (int i = 0; i < iter; i++) {
            for (Classes classes : cArray) {
                classes.clean();
            }

            iterator = wordMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, float[]> next = iterator.next();
                double miniScore = Double.MAX_VALUE;
                double tempScore;
                int classesId = 0;
                for (Classes classes : cArray) {
                    tempScore = classes.distance(next.getValue());
                    if (miniScore > tempScore) {
                        miniScore = tempScore;
                        classesId = classes.id;
                    }
                }
                cArray[classesId].putValue(next.getKey(), miniScore);
            }

            for (Classes classes : cArray) {
                classes.updateCenter(wordMap);
            }
            System.out.println("iter " + i + " ok!");
        }

        return cArray;
    }
    /**
     * 这是一个静态类，描述的是一个聚类的类别
     * @author jf320
     *
     */
    public static class Classes {
        private int id;
        private float[] center;

        public Classes(int id, float[] center) {
            this.id = id;
            this.center = center.clone();
        }

        Map<String, Double> values = new HashMap<>();
        /**
         * 这是一个计算距离的方法
         * @param value
         * @return 返回的是距离
         */
        public double distance(float[] value) {
            double sum = 0;
            //求每一个值到重心的距离
            for (int i = 0; i < value.length; i++) {
                sum += (center[i] - value[i])*(center[i] - value[i]) ;
            }
            return sum ;
        }
        /**
         * 把词和得分都放到Values里。
         * @param word
         * @param score
         */
        public void putValue(String word, double score) {
            values.put(word, score);
        }

        /**
         * 这是用来更新中心的
         * @param wordMap
         */
        public void updateCenter(HashMap<String, float[]> wordMap) {
            //把所有中心都只置为0
        	for (int i = 0; i < center.length; i++) {
                center[i] = 0;
            }
            float[] value = null;
            
            for (String keyWord : values.keySet()) {
                value = wordMap.get(keyWord);
                for (int i = 0; i < value.length; i++) {
                    center[i] += value[i];
                }
            }
            //计算平均值
            for (int i = 0; i < center.length; i++) {
                center[i] = center[i] / values.size();
            }
        }

		/**
		 * 这就是用来清除值的
		 */
        public void clean() {
            // TODO Auto-generated method stub
            values.clear();
        }

        /**
         * 这是获得前几个词的
         * @param n
         * @return 
         */
        public List<Entry<String, Double>> getTop(int n) {
        	//根据Values的集合里的数据重新建立一个副本
            List<Map.Entry<String, Double>> arrayList = new ArrayList<Map.Entry<String, Double>>(
                values.entrySet());
            //运用Collection来对ArrayList进行排序，使用的是自定义的排序接口
            Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>() {
                @Override
                public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                    // TODO Auto-generated method stub
                    return o1.getValue() > o2.getValue() ? 1 : -1;
                }
            });
            int min = Math.min(n, arrayList.size() - 1);
            if(min<=1)return Collections.emptyList() ;
            return arrayList.subList(0, min);
        }

    }

}
