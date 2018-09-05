package pageRank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;

import scala.Tuple2;


public class JoinOpt {
    public static void main(String[] xx){
    	SparkConf conf = new SparkConf();
    	conf.setMaster("local");
    	conf.setAppName("JoinOpt");

    	JavaSparkContext ctx = new JavaSparkContext(conf);
    	//����RDD��1��ͨ����ȡ�ⲿ�洢 ----- ��Ⱥ����ʹ�� 2��ͨ���ڴ��еļ���

    	List<Tuple2<String,String>> users = new ArrayList<Tuple2<String,String>>();
    	Tuple2<String,String> user1 = new Tuple2("1121", "zhangsan");
    	Tuple2<String,String> user2 = new Tuple2("1221", "lisi");
    	Tuple2<String,String> user3 = new Tuple2("1221", "wangwu");
    	Tuple2<String,String> user4 = new Tuple2("1241", "zhaoliu");
    	Tuple2<String,String> user5 = new Tuple2("1221", "Tianqi");
    	users.add(user1);
    	users.add(user2);
    	users.add(user3);
    	users.add(user4);
    	users.add(user5);
    	JavaPairRDD<String,String> usersRdd = ctx.parallelizePairs(users);
        JavaPairRDD<String,String> partitionedUserRdd = usersRdd.partitionBy(new HashPartitioner(3)).persist(StorageLevel.MEMORY_ONLY());
    	
    	List<Tuple2<String,String>> events = new ArrayList<Tuple2<String,String>>();
    	Tuple2<String,String> t1 = new Tuple2("1221", "http://www.baidu.com/about.html");
    	Tuple2<String,String> t2 = new Tuple2("1223", "http://www.ali.com/index.html");
    	Tuple2<String,String> t3 = new Tuple2("1241", "http://www.sohu.com/index.html");
    	Tuple2<String,String> t4 = new Tuple2("1231", "http://www.baidu.com/help.html");
    	Tuple2<String,String> t5 = new Tuple2("1121", "http://www.baidu.com/about.html");
        events.add(t1);
        events.add(t2);
        events.add(t3);
        events.add(t4);
        events.add(t5);    	
        JavaPairRDD<String,String> eventsRdd = ctx.parallelizePairs(events);
        
        
        while(true){
            JavaPairRDD joinedRdd  = usersRdd.join(eventsRdd);
            System.out.println(joinedRdd.collect());
            try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
       
        
    }
}
