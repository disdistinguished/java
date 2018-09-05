package api;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.hive.com.esotericsoftware.kryo.serializers.JavaSerializer;
import org.apache.spark.HashPartitioner;
import org.apache.spark.Partitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.AbstractJavaRDDLike;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;

//JavaPairRDD<String, Integer> results = mapRdd.reduceByKey((x, y)->x+y);
public class CogroupApiTest {
    public static void main(String[] xx){
    	SparkConf conf = new SparkConf();
    	conf.setMaster("local");
    	conf.setAppName("WordCounter");
    	conf.set("spark.default.parallelism", "4");
    	JavaSparkContext ctx = new JavaSparkContext(conf);
    	//����RDD��1��ͨ����ȡ�ⲿ�洢 ----- ��Ⱥ����ʹ�� 2��ͨ���ڴ��еļ���
    	List<Tuple2<String, Integer>> data1 = new ArrayList<Tuple2<String, Integer>>();
    	data1.add(new Tuple2<>("Cake", 2));
    	data1.add(new Tuple2<>("Bread", 3));
    	data1.add(new Tuple2<>("Cheese", 4));
    	data1.add(new Tuple2<>("Milk", 1));    	
    	data1.add(new Tuple2<>("Toast", 2));
    	data1.add(new Tuple2<>("Bread", 2));
    	data1.add(new Tuple2<>("Egg", 6));
    	
    	JavaRDD<Tuple2<String, Integer>> rdd1 = ctx.parallelize(data1, 2);

	      JavaPairRDD<String, Integer> mapRdd1 = rdd1.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {  
		      @Override
		      public Tuple2<String, Integer> call(Tuple2<String, Integer> t) throws Exception {  
		          return t;
		      }
	      }).partitionBy(new HashPartitioner(2)).persist(StorageLevel.MEMORY_ONLY());
	      
	  	List<Tuple2<String, Integer>> data2 = new ArrayList<Tuple2<String, Integer>>();
	  	data2.add(new Tuple2<>("Cake", 2));
	  	data2.add(new Tuple2<>("Bread", 3));
	  	data2.add(new Tuple2<>("Cheese", 4));
	  	data2.add(new Tuple2<>("Milk", 1));    	
	  	data2.add(new Tuple2<>("Toast", 2));
	  	JavaRDD<Tuple2<String, Integer>> rdd2 = ctx.parallelize(data2, 2);
	
	    JavaPairRDD<String, Integer> mapRdd2 = rdd2.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {  
		      @Override
		      public Tuple2<String, Integer> call(Tuple2<String, Integer> t) throws Exception {  
		          return t;
		      }
	    }).partitionBy(new HashPartitioner(2)).persist(StorageLevel.MEMORY_ONLY());

	    JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>>  mapRdd3 = mapRdd1.cogroup(mapRdd2);
	    mapRdd3.foreach(x->System.out.println(x));
	    
	    JavaPairRDD<String, Tuple2<Integer, Integer>> mapRdd31 = mapRdd1.join(mapRdd2);
	    mapRdd31.foreach(x->System.out.println(x));
	    
	    JavaPairRDD<String, Tuple2<Optional<Integer>, Integer>>  mapRdd32 = mapRdd2.rightOuterJoin(mapRdd1);
	    mapRdd32.foreach(x->System.out.println(x));

	    JavaPairRDD<String, Tuple2<Integer, Optional<Integer>>>  mapRdd4 = mapRdd1.leftOuterJoin(mapRdd2);
	    mapRdd4.foreach(x->System.out.println(x));  
	    
	    JavaPairRDD<String, Tuple2<Optional<Integer>, Optional<Integer>>> mapRdd5 = mapRdd1.fullOuterJoin(mapRdd2);
	    mapRdd5.foreach(x->System.out.println(x));
	    
	    JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> mapRdd6 = mapRdd1.groupWith(mapRdd2);
	    mapRdd6.foreach(x->System.out.println(x));
    }
}