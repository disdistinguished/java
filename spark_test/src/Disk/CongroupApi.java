package Disk;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class CongroupApi {
	public static void main(String[] args){
		SparkConf conf = new SparkConf();
		conf.setMaster("local");
		conf.setAppName("wordcount");
		conf.set("spark.default.parallelism", "4");
		JavaSparkContext ctx = new JavaSparkContext(conf);
		
		List<Tuple2<String,Integer>> data = new ArrayList<Tuple2<String,Integer>>();
		
		data.add(new Tuple2<>("cake",2));
		data.add(new Tuple2<>("Bread",3));
		data.add(new Tuple2<>("Cheese",4));
		data.add(new Tuple2<>("milk",1));
		data.add(new Tuple2<>("Toast",2));
		data.add(new Tuple2<>("Bread",2));
		data.add(new Tuple2<>("Egg",4));
		
		List<Tuple2<String,Integer>> data2 = new ArrayList<Tuple2<String,Integer>>();
		
		data2.add(new Tuple2<>("cake",2));
		data2.add(new Tuple2<>("Bread",3));
		data2.add(new Tuple2<>("Cheese",5));
		data2.add(new Tuple2<>("Milk",3));
		data2.add(new Tuple2<>("Egg",3));
		
		JavaRDD<Tuple2<String,Integer>> rdd1 = ctx.parallelize(data,2);
		JavaRDD<Tuple2<String, Integer>> rdd2 = ctx.parallelize(data2, 2);
		JavaPairRDD<String, Integer> mapRdd1 = rdd1.mapToPair(x->x);
		JavaPairRDD<String,Integer> mapRdd2 = rdd2.mapToPair(x->x);
//		 JavaPairRDD<String, Integer> mapRdd1 = rdd1.mapToPair(
//	    		  new PairFunction<Tuple2<String, Integer>, String, Integer>() {  
//		      @Override
//		      public Tuple2<String, Integer> call(Tuple2<String, Integer> t) throws Exception {  
//		          return t;
//		      }
//	      });
//		 JavaPairRDD<String, Integer> mapRdd2 = rdd2.mapToPair(
//	    		  new PairFunction<Tuple2<String, Integer>, String, Integer>() {  
//		      @Override
//		      public Tuple2<String, Integer> call(Tuple2<String, Integer> t) throws Exception {  
//		          return t;
//		      }
//	      });
//		 JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>>  mapRdd3 = mapRdd1.cogroup(mapRdd2);
		 JavaPairRDD<String, Tuple2<Integer, Integer>> mapRdd3 = mapRdd1.join(mapRdd2);
//		 JavaPairRDD<String, Tuple2<Optional<Integer>, Integer>>  mapRdd3 = mapRdd2.rightOuterJoin(mapRdd1);
//		 JavaPairRDD<String, Tuple2<Integer, Optional<Integer>>>  mapRdd3 = mapRdd1.leftOuterJoin(mapRdd2);
//		 JavaPairRDD<String, Tuple2<Optional<Integer>, Optional<Integer>>> mapRdd3 = mapRdd1.fullOuterJoin(mapRdd2);
//		 JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> mapRdd3 = mapRdd1.groupWith(mapRdd2);
		 
		 mapRdd3.foreach(x->System.out.println(x));
//		 JavaPairRDD<String, Tuple2<Integer, Integer>> results = mapRdd.combineByKey(
//				 new Function<Integer, Tuple2<Integer, Integer>>(){
//
//			@Override
//			public Tuple2<Integer, Integer> call(Integer v1) throws Exception {
//				return new Tuple2<Integer, Integer>(v1 ,1);
//			}
//			}, new Function2<Tuple2<Integer, Integer>, Integer, Tuple2<Integer, Integer>>() {
//				public Tuple2<Integer, Integer> call(Tuple2<Integer, Integer> v1, Integer v2) throws Exception {
//					return new Tuple2<Integer, Integer>(v1._1() + v2, v1._2() + 1);
//				}
//			}, new Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
//				public Tuple2<Integer, Integer> call(Tuple2<Integer, Integer> v1, Tuple2<Integer, Integer> v2) throws Exception {
//					return new Tuple2<Integer, Integer>(v1._1() + v2._1(), v1._2() + v2._2());
//				}
//			});
		 
		 
//		 JavaPairRDD<String, Tuple2<Integer, Integer>> results =  mapRdd.combineByKey(
//                 (Integer value) -> new Tuple2<Integer, Integer>(value,1), 
//                (Tuple2<Integer, Integer> acc, Integer value) -> new Tuple2<Integer, Integer>(acc._1() + value, acc._2() + 1), 
//                (Tuple2<Integer, Integer> acc1, Tuple2<Integer, Integer> acc2) -> new Tuple2<Integer, Integer>(acc1._1() + acc2._1(), acc1._2() + acc2._2()),
//                new HashPartitioner(3),
//                true,
//                null
//              );
		 
		 
//		 JavaPairRDD<String, Tuple2<Integer, Integer>> results =  mapRdd.aggregateByKey(
//	    		  new Tuple2<Integer, Integer>(0,0),
//	              (acc, value) -> new Tuple2<Integer, Integer>(acc._1() + value, acc._2() + 1), 
//	              (acc1, acc2) -> new Tuple2<Integer, Integer>(acc1._1() + acc2._1(), acc1._2() + acc2._2())	  
//	    		  );
		 
		 
//		 JavaPairRDD<String, Tuple2<Integer, Integer>> mapRdd1 = mapRdd.mapToPair(new PairFunction<Tuple2<String,Integer>, String, Tuple2<Integer, Integer>>() {
//				@Override
//				public Tuple2<String, Tuple2<Integer, Integer>> call(Tuple2<String, Integer> t) throws Exception {
//					return new Tuple2<String, Tuple2<Integer, Integer>>(t._1(), new Tuple2<Integer, Integer>(t._2() , 1));
//				}
//			  });
//		      
//		       JavaPairRDD<String, Tuple2<Integer, Integer>> results =  mapRdd1.reduceByKey(
//		    		   new Function2<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>() {
//					@Override
//					public Tuple2<Integer, Integer> call(Tuple2<Integer, Integer> acc1, Tuple2<Integer, Integer> acc2) throws Exception {
//						return new Tuple2<Integer, Integer>(acc1._1() + acc2._1(), acc1._2() + acc2._2());
//					}
//			   });  
//		results.foreach(x->System.out.println(x));
	}

}
