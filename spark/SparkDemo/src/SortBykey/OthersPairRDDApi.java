import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;


public class OthersPairRDDApi {
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
      
       //mapRdd.sortByKey().foreach(System.out::println);
       mapRdd.sortByKey(false).foreach(System.out::println);
       
//       mapRdd.sortByKey(new Comparator<Tuple2<String, Integer>>() {
//					@Override
//					public int compare(Tuple2<String, Integer> o1, Tuple2<String, Integer> o2) {
//						return 0;
//					}
//	    });
       
//        mapRdd.f
//       mapRdd.mapValues(x->x+1).foreach(System.out::println);
//       mapRdd.flatMapValues(()->Arrays.asList(1,1,1));

       ctx.stop(); 
    }
}


