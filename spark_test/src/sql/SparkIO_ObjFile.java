package sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class SparkIO_ObjFile {
    public static void main(String[] args) {
    	SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("SparkIO");
		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.setLogLevel("WARN");
		readObjFile(sc);
		sc.stop();
		sc.close();
	}

	private static void readObjFile(JavaSparkContext sc) {
//		JavaRDD<Object> input = sc.objectFile("file:///D:/objFile.txt");
//		input.foreach(x->System.out.println(x));
	}
    
	private static void writeObjFile(JavaSparkContext sc) {
	    List<Person> data = new ArrayList<Person>();
	    data.add(new Person("ABC", 1));
	    data.add(new Person("DEF", 3));
	    data.add(new Person("GHI", 2));
	    data.add(new Person("JKL", 4));
	    data.add(new Person("ABC", 1));

	    JavaRDD<Person> rdd = sc.parallelize(data, 1);
	    String dir = "file:///D:/textFile";
        rdd.saveAsTextFile(dir); 
        rdd.foreach(x->System.out.println(x));
//	    String dir = "file:///D:/objFile1.txt";
//        rdd.saveAsObjectFile(dir);
	}
	
	static class Person implements Serializable{
		public Person(String name, int id) {
			super();
			this.name = name;
			this.id = id;
		}
		@Override
		public String toString() {
			return "Person [name=" + name + ", id=" + id + "]";
		}
		String name;
		int id;
	}
}
