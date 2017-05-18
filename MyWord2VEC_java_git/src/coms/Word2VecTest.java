package coms;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.lang.model.element.VariableElement;

import com.ansj.vec.domain.WordEntry;

public class Word2VecTest {
    private static final File sportCorpusFile = new File("已分好词的训练语料.txt");

    public static void main(String[] args) throws IOException {
        //进行分词训练
        //Learn lean = new Learn() ;
        //lean.learnFile(sportCorpusFile) ;
    	//这里保存的是JAVA模型
        //lean.saveModel(new File("已训练好的模型.model")) ;
        //加载测试
        Word2VEC w2v = new Word2VEC() ;
        w2v.loadGoogleModel("这里是利用Google自己的源码跑出的模型.model") ; 
        Word2VEC w2v2 = new Word2VEC() ;
        w2v2.loadJavaModel("这里是用JAVA源码跑出的模型.model") ;
        Set<WordEntry> result=w2v.distance("狗");
        for(int i=0;i<5;i++){
        	System.out.print(result.toArray()[i]+",");
        }
        System.out.print("\n--------------------------\n");
        Set<WordEntry> result2=w2v2.distance("狗");
        for(int i=0;i<5;i++){
        	System.out.print(result2.toArray()[i]+",");
        }
    }

    
 
}