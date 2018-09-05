package api;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
    	//创建RDD：1）通过读取外部存储 ----- 集群环境使用 2）通过内存中的集合
    	List<Tuple2<Person, Integer>> data = 
    			new ArrayList<Tuple2<Person, Integer>>();
    	data.add(new Tuple2<Person, Integer>(new Person("Cake",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Bread",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Smith",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Hourse",16), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Mary",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Greey",21), 3));
    	data.add(new Tuple2<Person, Integer>(new Person("Hourse",16), 7));
    	data.add(new Tuple2<Person, Integer>(new Person("Tom",32), 2));
    	data.add(new Tuple2<Person, Integer>(new Person("Gao",21), 3));    	

    	JavaPairRDD<Person, Integer> dataRdd = ctx.parallelizePairs(data);
        dataRdd.reduceByKey((x,y)-> x + y).foreach(x->System.out.println(x));
    	
    	dataRdd.sortByKey(new PersonComparator()).foreach(x->System.out.println(x));
    }
}

class PersonComparator implements java.io.Serializable,Comparator <Person>{
	@Override
	public int compare(Person x, Person y) {
		if(x.name.compareTo(y.name) == 0){
			return x.age - y.age;
		}else{
			return x.name.compareTo(y.name);
		} 
	}
}

class Person implements Serializable{
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public String name;
	public int age;
	
//	@Override
//	public int compareTo(Person p) {
//		if(this.name.compareTo(p.name) == 0){
//			return this.age - p.age;
//		}else{
//			return this.name.compareTo(p.name);
//		}
//	}
	
	@Override
	public String toString() {
		return "name:" + name + " age:" + age;
	}
	
}
