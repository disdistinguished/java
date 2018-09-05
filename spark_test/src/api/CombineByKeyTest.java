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
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;

//JavaPairRDD<String, Integer> results = mapRdd.reduceByKey((x, y)->x+y);
public class CombineByKeyTest {
    public static void main(String[] xx){
    	SparkConf conf = new SparkConf();
    	conf.setMaster("local");
    	conf.setAppName("WordCounter");
    	conf.set("spark.default.parallelism", "4");
    	JavaSparkContext ctx = new JavaSparkContext(conf);
    	//����RDD��1��ͨ����ȡ�ⲿ�洢 ----- ��Ⱥ����ʹ�� 2��ͨ���ڴ��еļ���
    	List<Tuple2<String, Integer>> data = new ArrayList<Tuple2<String, Integer>>();
    	data.add(new Tuple2<>("Cake", 2));
    	data.add(new Tuple2<>("Bread", 3));
    	data.add(new Tuple2<>("Cheese", 4));
    	data.add(new Tuple2<>("Milk", 1));
    	data.add(new Tuple2<>("Toast", 2));
    	data.add(new Tuple2<>("Bread", 2));
    	data.add(new Tuple2<>("Egg", 6));

    	JavaRDD<Tuple2<String, Integer>> rdd1 = ctx.parallelize(data, 2);

      JavaPairRDD<String, Integer> mapRdd = rdd1.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {  
	      @Override
	      public Tuple2<String, Integer> call(Tuple2<String, Integer> t) throws Exception {  
	          return t;
	      }
      }).partitionBy(new HashPartitioner(2)).persist(StorageLevel.MEMORY_ONLY());
      
      JavaPairRDD<String, Tuple2<Integer, Integer>> mapRdd1 = mapRdd.mapToPair(new PairFunction<Tuple2<String,Integer>, String, Tuple2<Integer, Integer>>() {
		@Override
		public Tuple2<String, Tuple2<Integer, Integer>> call(Tuple2<String, Integer> t) throws Exception {
			return new Tuple2<String, Tuple2<Integer, Integer>>(t._1(), new Tuple2<Integer, Integer>(t._2() , 1));
		}
	  });
      mapRdd1.foreach(x->System.out.println(x));

      
//   JavaPairRDD<String, Iterable<Tuple2<Integer, Integer>>>  results = mapRdd1.groupByKey();
       JavaPairRDD<String, Iterable<Tuple2<Integer, Integer>>> results = mapRdd1.combineByKey(
    		   new Function<Tuple2<Integer,Integer>, Iterable<Tuple2<Integer, Integer>>>() {
		           @Override
						public Iterable<Tuple2<Integer, Integer>> call(Tuple2<Integer, Integer> value) throws Exception {
							 List<Tuple2<Integer, Integer>> list = new ArrayList<Tuple2<Integer, Integer>>();
							 list.add(value);
							 return list;
						}
	           }, 
	           
	           new Function2<Iterable<Tuple2<Integer, Integer>>, Tuple2<Integer, Integer>, Iterable<Tuple2<Integer, Integer>>>() {
				@Override
				public Iterable<Tuple2<Integer, Integer>> call(
									Iterable<Tuple2<Integer, Integer>> it, 
									Tuple2<Integer, Integer> value) throws Exception {
//					List<Tuple2<Integer, Integer>> list = new ArrayList<Tuple2<Integer, Integer>>();
//					it.forEach(list::add);
//					list.add(value);
					((List<Tuple2<Integer, Integer>>)it).add(value);
					return it;
				}
	            }, 
	           
	           new Function2<Iterable<Tuple2<Integer, Integer>>, Iterable<Tuple2<Integer, Integer>>, Iterable<Tuple2<Integer, Integer>>>() {
					@Override
					public Iterable<Tuple2<Integer, Integer>> call(
							Iterable<Tuple2<Integer, Integer>> it1,
							Iterable<Tuple2<Integer, Integer>> it2) throws Exception {
//						List<Tuple2<Integer, Integer>> list = new ArrayList<Tuple2<Integer, Integer>>();
//						it1.forEach(list::add);
//						it2.forEach(list::add);
//						return list;
						((List)it1).addAll((List)it2);
						return it1;
					}
			   });
       results.foreach(x->System.out.println(x));
       //��ʵ��distinct ���� reduceByKeyʵ��
//       mapRdd1.distinct();
       ctx.stop(); 
    }
}
