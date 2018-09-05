package api;
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


public class IntersectionDemo {
    public static void main(String[] xx){
    	SparkConf conf = new SparkConf();
    	conf.setMaster("local");
    	conf.setAppName("WordCounter");
    	JavaSparkContext ctx = new JavaSparkContext(conf);

    	List<String> lines1 = new ArrayList<String>();
    	lines1.add("Hello");
    	lines1.add("How");
    	lines1.add("Moon");
    	JavaPairRDD<String, Integer> rdd1 = ctx.parallelize(lines1, 2).
    			mapToPair(new PairFunction<String, String, Integer>() {  
    	            @Override  
    	            public Tuple2<String, Integer> call(String s) throws Exception {  
    	                return new Tuple2<String, Integer>(s, 1);  
    	            }  
    	        }).partitionBy(new HashPartitioner(3));
   	
    	System.out.println("rdd1:" + rdd1.partitioner());
//    	rdd1.foreach(x -> {
//    		int index = x.hashCode() % 2;
//    		System.out.println("当前数据：" + x + " 它的hashindex：" + index);
//    	});
//    	System.out.println(rdd1.glom().collect());
    	
    	
    	List<String> lines2 = new ArrayList<String>();
    	lines2.add("Hello");
    	lines2.add("How");
    	lines2.add("Good");
    	JavaPairRDD<String, Integer> rdd2 = ctx.parallelize(lines2, 2).
    			mapToPair(new PairFunction<String, String, Integer>() {  
    	            @Override  
    	            public Tuple2<String, Integer> call(String s) throws Exception {  
    	                return new Tuple2<String, Integer>(s, 1);  
    	            }  
    	        }).partitionBy(new HashPartitioner(2));
    	System.out.println("rdd2:" + rdd2.partitioner());
    	
    	JavaPairRDD<String, Integer> rdd3 = rdd1.intersection(rdd2);
//    	JavaPairRDD<String, Integer> rdd3 = rdd1.subtract(rdd2);
//    	JavaPairRDD<String, Integer> rdd3 = rdd1.union(rdd2);
    	System.out.println("rdd3:" + rdd3.partitioner());
    	System.out.println("rdd3:" + rdd3.getNumPartitions());
    	rdd3.foreach(x->System.out.println(x));
    }
}
