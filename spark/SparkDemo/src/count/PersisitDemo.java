import java.util.Iterator;

import org.apache.spark.SparkConf;  
import org.apache.spark.api.java.JavaPairRDD;  
import org.apache.spark.api.java.JavaRDD;  
import org.apache.spark.api.java.JavaSparkContext;  
import org.apache.spark.api.java.function.FlatMapFunction;  
import org.apache.spark.api.java.function.Function2;  
import org.apache.spark.api.java.function.PairFunction;  
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;  
  


import java.util.Arrays;  
import java.util.Iterator;
import java.util.Scanner;


public class PersisitDemo {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("wordcount");
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        ctx.setCheckpointDir("file:///d:/checkpoint");
        final JavaRDD<String> lines = ctx.textFile(args[0]);

        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
            	System.out.println("flatMap...");
                return Arrays.asList(s.split(" ")).iterator();
            }
        })
        //.persist(StorageLevel.MEMORY_ONLY());
        .cache();
        
        while(true){
        	Scanner sc = new Scanner(System.in);
        	String line = sc.next();
        	if(line.equals("END")){
        		break;
        	}
        	
	        JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {  
	            @Override
	            public Tuple2<String, Integer> call(String s) throws Exception {  
	            	System.out.println("mapToPair...");
	                return new Tuple2<String, Integer>(s, 1);
	            }
	        });
	        
	        JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {  
	            @Override  
	            public Integer call(Integer integer, Integer integer2) throws Exception {  
	            	System.out.println("reduceByKey...");
	                return integer + integer2;  
	            }  
	        });
	        //counts.saveAsTextFile(args[1]);
	        counts.foreach(System.out::println);

	        
//	        lines.unpersist();
        }
        ctx.stop();
    }  
}
