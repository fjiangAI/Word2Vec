package coms;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.Set;
import java.util.Vector;
import com.ansj.vec.domain.WordEntry;
public class Word2VecHelper {

	public Word2VEC w;
	/**
	 * 这是用来初始化的
	 * @param modelFilePath
	 * 这是模型的路径
	 * @param types
	 * 这是模型的类型，Google是谷歌模型,Java是java模型
	 * @throws IOException
	 */
	public Word2VecHelper(String modelFilePath,String types) throws IOException {
		w = new Word2VEC();
		if(types=="Google")
			w.loadGoogleModel(modelFilePath);
		else if(types=="Java")
			w.loadJavaModel(modelFilePath);
	}
	/**
	 * 这是获取句子向量
	 * 这是一种简单的句子向量的获取方法，使用的是词向量相加的，但实际上效果不好
	 * 在Mikolov的《Distributed Representations of Sentences and Documents》中提到了
	 * 句子向量的组合方式，一个简单的方法就是使用文本的所有词向量加权平均。
	 * 一个更复杂的方法是使用矩阵操作，根据句子的短语结构分析树给出的顺序组合词向量。
	 * 但这两种方法都不好。
	 * @param words 句子内的词
	 * @param weight 句子内的词的权重
	 * @return
	 */
	public float[] getSentenceVector(String[] words,float[] weight){
		float[] sentencevector=new float[100];
		for(int i=0;i<words.length;i++){
			float[] wordvector=w.getWordVector(words[i]);
			if(wordvector==null){
				wordvector=new float[100];
			}
			float wordweight=weight[i];
			for(int j=0;j<100;j++){
				sentencevector[j]+=wordvector[j]*wordweight;
			}
		}
		return sentencevector;
	}
	/***
	 * 获得目标词的词向量
	 * @param word 要查询的目标词
	 * @return
	 */
	public float[] getWordVector(String word){
		return w.getWordVector(word);
	}
	/**
	 * 求相似词的
	 * @param word
	 * 这是待查询词
	 * @param num
	 * 这是前N个
	 * @return
	 * 返回的是以空格隔开的最相思的前N个最相似的词
	 */
	public String distance(String word,int num){
		String result="";
		Set<WordEntry> resultset=this.w.distance(word);
		for(int i=0;i<num;i++){
			WordEntry we=(WordEntry)resultset.toArray()[i];
        	result+=we.name;
        	if(i<num-1){
        		result+=" ";
        	}
        }
		return result;
	}
	/**
	 * 这是求两个句子的相似度的，方法三
	 * @param s1
	 * s1是待求的句子1
	 * @param s2
	 * s2是待求的句子2
	 * @return
	 * 返回的是两个句子的相似度
	 */
	public float sentenceSimilairy3(String s1, String s2){
		//t1是句子1分词后的数组
		String[] t1 = s1.split(" ");
		//t2是句子2分词后的数组
		String[] t2 = s2.split(" ");
		double scoret1=0,scoret2=0;
		double sumt1=t1.length,sumt2=t2.length;
		//如果两个总词数有一个为0，则相似度就为0
		if(sumt1==0||sumt2==0){
			return (float)0;
		}
		//计算第一个的得分
		for(int i=0;i<t1.length;i++){
			scoret1+=getMaxSimilarity(t1[i], t2);
		}
		//计算第二个的得分
		for(int i=0;i<t2.length;i++){
			scoret2+=getMaxSimilarity(t2[i], t1);
		}
		//计算最终相似度
		double similarityscore=(scoret1+scoret2)/(sumt1+sumt2);
		return (float)similarityscore;
		
	}
	/**
	 * 求句子最大相似度的
	 * @param word 这是目标词
	 * @param s2 这是目标距离
	 * @return 返回的是最大的相似度
	 */
	private double getMaxSimilarity(String word,String[] s2){
		double maxscore=0;
		for(int i=0;i<s2.length;i++){
			double score=wordSimilarity(word, s2[i]);
			if(score>maxscore){
				maxscore=score;
			}
		}
		return maxscore;
	}
	/**
	 * 这是求两个句子的相似度的。
	 * @param s1
	 * s1是待求的句子1
	 * @param s2
	 * s2是待求的句子2
	 * @return
	 * 返回的是两个句子的相似度
	 */
	public float sentenceSimilairy(String s1, String s2) {
		//t1是句子1分词后的数组
		String[] t1 = s1.split(" ");
		//t2是句子2分词后的数组
		String[] t2 = s2.split(" ");
		//创建一个集合t
		Vector<String> t = new Vector<String>();
		
		for (int i = 0; i < t1.length; i++) {
			//这是保证集合t中具有唯一性
			int j = 0;
			for (; j < t.size(); j++) {
				if (t1[i].equals(t.get(j))) {
					break;
				}
			}
			//如果集合中没有t1[i]就把其加入t中
			if (j == t.size()) {
				t.add(t1[i]);
			}
		}
		//对t2进行和t1同样的操作
		for (int i = 0; i < t2.length; i++) {
			int j = 0;
			for (; j < t.size(); j++) {
				if (t2[i].equals(t.get(j))) {
					break;
				}
			}
			if (j == t.size()) {
				t.add(t2[i]);
			}
		}
		//此时t里就是包含了t1和t2里所有不同的词
		//创建一个v1的数组，大小等同t
		float[] v1 = new float[t.size()];
		//
		for (int i = 0; i < v1.length; i++) {
			//t中第i个词出现在t1中，就把v1第i位标为1
			int j = 0;
			for (; j < t1.length; j++) {
				if (t.get(i).equals(t1[j])) {
					v1[i] = 1;
					break;
				}
			}
			//如果这个词没有在t1中出现
			if (j == t1.length) {
				//score=0
				float score = (float) 0.0;
				//使用这个词与t1的每个词做比较
				for (int k = 0; k < t1.length; k++) {
					float tmp = wordSimilarity(t.get(i), t1[k]);
					//如果现有得分是最大的
					if (tmp > score && tmp >= 0) {
						//更新得分情况
						score = tmp;
					}
				}
				//把最大的相似度赋值给第i位。
				v1[i] = score;
			}
		}
		//对v2做v1同样的处理
		float[] v2 = new float[t.size()];
		for (int i = 0; i < v2.length; i++) {
			int j = 0;
			for (; j < t2.length; j++) {
				if (t.get(i).equals(t2[j])) {
					v2[i] = 1;
					break;
				}
			}
			if (j == t2.length) {
				float score = (float) 0.0;
				for (int k = 0; k < t2.length; k++) {
					float tmp = wordSimilarity(t.get(i), t2[k]);
					if (tmp > score) {
						score = tmp;
					}
				}
				v2[i] = score;
			}
		}
		//至此，句子t1的相似度数组v1和句子t2的相似度数组v2都已经准备完毕
		//求v1的绝对值
		float n1 = 0;
		for (int i = 0; i < v1.length; i++) {
			n1 += v1[i] * v1[i];
		}
		n1 = (float) Math.sqrt(n1);
		//求v2的绝对值
		float n2 = 0;
		for (int i = 0; i < v2.length; i++) {
			n2 += v2[i] * v2[i];
		}
		n2 = (float) Math.sqrt(n2);
		//求v1*v2
		float n = 0;
		for (int i = 0; i < v1.length; i++) {
			n += v1[i] * v2[i];
		}
		// System.out.println(n1 + " " + n2 + " " + n);
		return n / (n1 * n2);
	}
	public float sentenceSimilairy2(String s1, String s2) {
		//t1是句子1分词后的数组
		String[] t1 = s1.split(" ");
		//t2是句子2分词后的数组
		String[] t2 = s2.split(" ");
		//创建一个集合t
		Vector<String> t = new Vector<String>();
		
		for (int i = 0; i < t1.length; i++) {
			//这是保证集合t中具有唯一性
			int j = 0;
			for (; j < t.size(); j++) {
				if (t1[i].equals(t.get(j))) {
					break;
				}
			}
			//如果集合中没有t1[i]就把其加入t中
			if (j == t.size()) {
				t.add(t1[i]);
			}
		}
		//对t2进行和t1同样的操作
		for (int i = 0; i < t2.length; i++) {
			int j = 0;
			for (; j < t.size(); j++) {
				if (t2[i].equals(t.get(j))) {
					break;
				}
			}
			if (j == t.size()) {
				t.add(t2[i]);
			}
		}
		//此时t里就是包含了t1和t2里所有不同的词
		//创建一个v1的数组，大小等同t
		float[] v1 = new float[t.size()];
		//
		for (int i = 0; i < v1.length; i++) {
			//t中第i个词出现在t1中，就把v1第i位标为1
			int j = 0;
			for (; j < t1.length; j++) {
				if (t.get(i).equals(t1[j])) {
					v1[i] = 1;
					break;
				}
			}
			//如果这个词没有在t1中出现
			if (j == t1.length) {
				//score=0
				float score = (float) 0.0;
				//使用这个词与t1的每个词做比较
				for (int k = 0; k < t1.length; k++) {
					float tmp = wordSimilarity2(t.get(i), t1[k]);
					//如果现有得分是最大的
					if (tmp > score && tmp >= 0) {
						//更新得分情况
						score = tmp;
					}
				}
				//把最大的相似度赋值给第i位。
				v1[i] = score;
			}
		}
		//对v2做v1同样的处理
		float[] v2 = new float[t.size()];
		for (int i = 0; i < v2.length; i++) {
			int j = 0;
			for (; j < t2.length; j++) {
				if (t.get(i).equals(t2[j])) {
					v2[i] = 1;
					break;
				}
			}
			if (j == t2.length) {
				float score = (float) 0.0;
				for (int k = 0; k < t2.length; k++) {
					float tmp = wordSimilarity2(t.get(i), t2[k]);
					if (tmp > score) {
						score = tmp;
					}
				}
				v2[i] = score;
			}
		}
		//至此，句子t1的相似度数组v1和句子t2的相似度数组v2都已经准备完毕
		//求v1的绝对值
				float n1 = 0;
				for (int i = 0; i < v1.length; i++) {
					n1 += v1[i] * v1[i];
				}
				n1 = (float) Math.sqrt(n1);
				//求v2的绝对值
				float n2 = 0;
				for (int i = 0; i < v2.length; i++) {
					n2 += v2[i] * v2[i];
				}
				n2 = (float) Math.sqrt(n2);
				//求v1*v2
				float n = 0;
				for (int i = 0; i < v1.length; i++) {
					n += v1[i] * v2[i];
				}
				// System.out.println(n1 + " " + n2 + " " + n);
				return n / (n1 * n2);
	}
	/**
	 * 求两个词的相似度
	 * @param word1
	 * 词1
	 * @param word2
	 * 词2
	 * @return
	 * 返回的是两个词的相似度
	 */
	public float wordSimilarity(String word1, String word2) {
		float[] v1 = w.getWordVector(word1);
		float[] v2 = w.getWordVector(word2);
		float n1 = 0;
		if (v1 == null || v2 == null) {
			return (float) 0;
		}
		for (int i = 0; i < v1.length; i++) {
			n1 += v1[i] * v1[i];
		}
		n1 = (float) Math.sqrt(n1);
		float n2 = 0;
		for (int i = 0; i < v2.length; i++) {
			n2 += v2[i] * v2[i];
		}
		n2 = (float) Math.sqrt(n2);
		float n = 0;
		for (int i = 0; i < v1.length; i++) {
			n += v1[i] * v2[i];
		}
		return n / (n1 * n2);
	}
	/***
	 * 使用第二种方式求词与词的相似度，这是使用内积和作为距离指标
	 * @param word1
	 * @param word2
	 * @return
	 */
	public float wordSimilarity2(String word1, String word2) {
		float[] v1 = w.getWordVector(word1);
		float[] v2 = w.getWordVector(word2);
		if (v1 == null || v2 == null) {
			return (float) 0;
		}
		float n = 0;
		for (int i = 0; i < v1.length; i++) {
			n += v1[i] * v2[i];
		}
		return n;
	}
}
