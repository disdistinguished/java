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
		      *��������ͨ��updateStateByKey����Batch IntervalΪ��λ������ʷ״̬���и��£�
		      * ���ǹ����ϵ�һ���ǳ���ĸĽ�������Ļ���Ҫ���ͬ����Ŀ�ģ��Ϳ�����Ҫ�����ݱ�����Redis��
		      * Tagyon����HDFS����HBase�������ݿ��������ϵ����ͬ��һ��key��State���£������������м�Ϊ���̵�Ҫ��
		      * ���������ر��Ļ������Կ��ǰ����ݷ��ڷֲ�ʽ��Redis����Tachyon�ڴ��ļ�ϵͳ�У�
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
		* Spark Streamingִ������Ҳ����Driver��ʼ���У�Driver������ʱ����λ��һ���µ��߳��еģ�
		* ��Ȼ���ڲ�����Ϣѭ���壬���� ����Ӧ�ó��������Executor�е���Ϣ��
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