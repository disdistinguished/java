package CombineByKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SortByKeyTest {
	public static void main(String[] args){
		SparkConf conf = new SparkConf();
		conf.setMaster("local");
		conf.setAppName("sort");
		conf.set("Spark.default.parallelism", "4");
		JavaSparkContext ctx = new JavaSparkContext(conf);
		List<Tuple2<Person,Integer>> data = new ArrayList<Tuple2<Person,Integer>>();
		data.add(new Tuple2<Person, Integer>(new Person("Cake",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Bread",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Smith",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Hourse",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Mary",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Greey",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Tom",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Gao",21), 3));  
    	JavaPairRDD dataRdd = ctx.parallelizePairs(data);
    	dataRdd.foreach(x-> System.out.println(x));
	}

}
class Person implements Serializable,Comparable<Person>{

	@Override
	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return 0;
	}
	String name;
	int age ;
	public Person(String name, int age){
		super();
		this.name = name;
		this.age = age;
	}
}
