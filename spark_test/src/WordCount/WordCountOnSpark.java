package WordCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class WordCountOnSpark {
	public static void main( String[] args){
			SparkConf conf = new SparkConf();
			conf.setMaster("local");
			conf.setAppName("WordCount");
			
			JavaSparkContext  ctx = new JavaSparkContext(conf);
//			
						
			List<String> str = new ArrayList<String>();
			str.add("how a wondful day");
			str.add("you are a beautful gril");
			str.add("what can i do for you");
	
			ctx.parallelize(str).flatMap(x->Arrays.asList(x.split(" ")).iterator())
			                     .mapToPair(x->new Tuple2<String,Integer>(x,1))
			                     .reduceByKey((x,y) -> x + y)
			                     .foreach(x->System.out.println(x));
//			JavaRDD<String> rdd1 = ctx.parallelize(str);
//			
//			JavaRDD<String> rdd2 = rdd1.flatMap(x->Arrays.asList(x.split(" ")).iterator());
//			
//			JavaPairRDD<String,Integer> rdd3 = rdd2.mapToPair(x->new Tuple2<String,Integer>(x,1));
//			
//			JavaPairRDD rdd4 = rdd3.reduceByKey((x,y) -> x + y);
//			
//			JavaRDD<String> rdd2 = rdd1.flatMap(new FlatMapFunction< String, String>(){
//
//				@Override
//				public Iterator<String> call(String t) throws Exception {
//					return Arrays.asList(t.split(" ")).iterator();
//				}});
//			rdd2.foreach(x->System.out.println(x));
//			JavaPairRDD rdd3 = rdd2.mapToPair(new PairFunction<String,String,Integer>(){
//
//				@Override
//				public Tuple2<String, Integer> call(String t) throws Exception {
//					
//					return new Tuple2<String,Integer>(t,1);
//				}});
//			rdd3.foreach(x->System.out.println(x));
//			
//			JavaPairRDD rdd4 = rdd3.reduceByKey(new Function2<Integer,Integer, Integer>(){
//
//				@Override
//				public Integer call(Integer v1, Integer v2) throws Exception {
//				
//					return v1+v2;
//				}
//				
//			});
//			rdd4.foreach(x->System.out.println(x));
			
	}

}
