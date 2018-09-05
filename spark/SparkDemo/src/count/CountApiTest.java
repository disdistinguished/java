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
import org.apache.spark.partial.BoundedDouble;
import org.apache.spark.partial.PartialResult;
import org.apache.spark.storage.StorageLevel;





import scala.Tuple2;

//JavaPairRDD<String, Integer> results = mapRdd.reduceByKey((x, y)->x+y);
public class CountApiTest {
    public static void main(String[] xx){
    	SparkConf conf = new SparkConf();
    	conf.setMaster("local");
    	conf.setAppName("Count API");
//    	conf.set("spark.default.parallelism", "4");
    	JavaSparkContext ctx = new JavaSparkContext(conf);
    	//����RDD��1��ͨ����ȡ�ⲿ�洢 ----- ��Ⱥ����ʹ�� 2��ͨ���ڴ��еļ���

    	List<Integer> list = new ArrayList<Integer>();
        for(int i = 0; i < 10000; i++){
    	    list.add(i);
        }
    	JavaRDD<Integer> rdd1 = ctx.parallelize(list, 2);
    	JavaRDD<Integer> rdd2 = rdd1.union(rdd1).union(rdd1).union(rdd1);
    	
    	//System.out.println(rdd2.count());
    	PartialResult<BoundedDouble> result = rdd2.countApprox(2000);//1000, 300
    	System.out.println(result.initialValue().mean());
    	System.out.println(result.initialValue().low());
    	System.out.println(result.initialValue().high());
    	System.out.println(result.initialValue().confidence());
    	
        //System.out.println(rdd2.countApproxDistinct(0.01));


	      
        
    }
}