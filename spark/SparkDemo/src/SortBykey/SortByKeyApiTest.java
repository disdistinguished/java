import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SortByKeyApiTest {
    public static void main(String[] xx){
    	SparkConf conf = new SparkConf();
    	conf.setMaster("local");
    	conf.setAppName("WordCounter");
    	conf.set("spark.default.parallelism", "4");
    	JavaSparkContext ctx = new JavaSparkContext(conf);
    	//����RDD��1��ͨ����ȡ�ⲿ�洢 ----- ��Ⱥ����ʹ�� 2��ͨ���ڴ��еļ���
    	List<Tuple2<Person, Integer>> data = 
    			new ArrayList<Tuple2<Person, Integer>>();
    	data.add(new Tuple2<Person, Integer>(new Person("Cake",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Bread",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Smith",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Hourse",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Mary",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Greey",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Tom",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Gao",21), 3));    	

    	JavaPairRDD dataRdd = ctx.parallelizePairs(data);
//    	dataRdd.sortByKey
    }
}



class Person implements Serializable,Comparable<Person>{
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	String name;
	int age;
	@Override
	public int compareTo(Person arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
