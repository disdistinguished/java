package api;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

public class UpdateStateByKeyDemo {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]")
				                        .setAppName("UpdateStateByKeyDemo");

		JavaStreamingContext jsc = new JavaStreamingContext(conf, new Duration(500));
		jsc.sparkContext().setLogLevel("WARN");
		jsc.checkpoint("file:///d:/checkpoint");

		JavaReceiverInputDStream<String> lines = jsc.socketTextStream("localhost", 9999);
			JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() { 
				@Override
				public Iterator<String> call(String line) throws Exception {
				    return Arrays.asList(line.split(" ")).iterator();
				}
			});

		JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String, Integer>(word, 1);
			}
		});
		
		/*
		      *在这里是通过updateStateByKey来以Batch Interval为单位来对历史状态进行更新，
		      * 这是功能上的一个非常大的改进，否则的话需要完成同样的目的，就可能需要把数据保存在Redis、
		      * Tagyon或者HDFS或者HBase或者数据库中来不断的完成同样一个key的State更新，如果你对性能有极为苛刻的要求，
		      * 且数据量特别大的话，可以考虑把数据放在分布式的Redis或者Tachyon内存文件系统中；
		 */
		JavaPairDStream<String, Integer> wordsCount = pairs.updateStateByKey(
			new Function2<List<Integer>, Optional<Integer>, Optional<Integer>>() {
				@Override
				public Optional<Integer> call(List<Integer> values, Optional<Integer> state)
						                              throws Exception {
					Integer updatedValue = 0 ;
					if(state.isPresent()){
						updatedValue = state.get();
					}
					for(Integer value: values){
						updatedValue += value;
					}
					return Optional.of(updatedValue);
				}
			}
		);

		wordsCount.print();
		/*
		* Spark Streaming执行引擎也就是Driver开始运行，Driver启动的时候是位于一条新的线程中的，
		* 当然其内部有消息循环体，用于 接受应用程序本身或者Executor中的消息；
		*/
		jsc.start();
		try {
			jsc.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		jsc.close();
    }
}