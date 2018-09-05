package sql;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import scala.Tuple2;
import au.com.bytecode.opencsv.CSVReader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SparkIO_File {
    public static void main(String[] args) {
    	SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("SparkIO");
		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.setLogLevel("WARN");
		fileTest(sc);
		sc.stop();
		sc.close();
	}

    static void fileTest(JavaSparkContext sc){
		JavaRDD<String> rdd = sc.textFile("file:///F:/workspace/PartitionDemo/src/bigdata34_1106");
		//JavaPairRDD<String, String> rdd = sc.wholeTextFiles("file:///F:/workspace/PartitionDemo/src/bigdata12_1103");
		rdd.foreach(x->{
			System.out.println("褰撳墠鍏冪礌锛�" + x);
		});
		System.out.println(rdd.count());
		rdd.saveAsTextFile("file:///d:/test");
    }
}

