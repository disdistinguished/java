import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.HashPartitioner;
import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;


public class UnionDemo {
    public static void main(String[] xx){
    	SparkConf conf = new SparkConf();
    	conf.setMaster("local");
    	conf.setAppName("WordCounter");

    	JavaSparkContext ctx = new JavaSparkContext(conf);
    	//����RDD��1��ͨ����ȡ�ⲿ�洢 ----- ��Ⱥ����ʹ�� 2��ͨ���ڴ��еļ���

    	List<Tuple2<String,Integer>> urls = new ArrayList<Tuple2<String,Integer>>();
    	urls.add(new Tuple2<String,Integer>("http://www.baidu.com/about.html", 3));
    	urls.add(new Tuple2<String,Integer>("http://www.ali.com/index.html", 2));
    	urls.add(new Tuple2<String,Integer>("http://www.sina.com/first.html", 4));
    	urls.add(new Tuple2<String,Integer>("http://www.sohu.com/index.html", 3));
    	urls.add(new Tuple2<String,Integer>("http://www.baidu.com/index.jsp",7));
    	urls.add(new Tuple2<String,Integer>("http://www.sina.com/help.html",1));
    	JavaPairRDD<String, Integer> urlRdd1 = ctx.parallelizePairs(urls).
    			                         partitionBy(new HashPartitioner(2));
    	System.out.println("urlRdd1:" + urlRdd1.partitioner());

    	List<Tuple2<String,Integer>> anotherUrls = new ArrayList<Tuple2<String,Integer>>();
    	anotherUrls.add(new Tuple2<String,Integer>("http://www.163.com/about.html", 3));
    	anotherUrls.add(new Tuple2<String,Integer>("http://www.taobao.com/index.html", 2));
    	anotherUrls.add(new Tuple2<String,Integer>("http://www.sina.com/first.html", 4));
    	anotherUrls.add(new Tuple2<String,Integer>("http://www.csdn.com/index.html", 3));
    	anotherUrls.add(new Tuple2<String,Integer>("http://www.facebook.com/index.jsp",7));
    	anotherUrls.add(new Tuple2<String,Integer>("http://www.sina.com/help.html",1));
    	JavaPairRDD<String, Integer> urlRdd2 = ctx.parallelizePairs(anotherUrls).
    			                      partitionBy(new HashPartitioner(3));
    	System.out.println("urlRdd2:" + urlRdd2.partitioner());

    	JavaPairRDD<String, Integer> rdd3 = urlRdd1.union(urlRdd2);
    	System.out.println("rdd3:" + rdd3.partitioner());
    	System.out.println("rdd3:" + rdd3.getNumPartitions());

    	rdd3.foreach(System.out::println);
    	
    }
}
