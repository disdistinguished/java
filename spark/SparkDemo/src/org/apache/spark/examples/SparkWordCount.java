package org.apache.spark.examples;

import org.apache.spark.SparkConf;  
import org.apache.spark.api.java.JavaPairRDD;  
import org.apache.spark.api.java.JavaRDD;  
import org.apache.spark.api.java.JavaSparkContext;  
import org.apache.spark.api.java.function.FlatMapFunction;  
import org.apache.spark.api.java.function.Function2;  
import org.apache.spark.api.java.function.PairFunction;  
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;  
  


import java.util.Arrays;  
import java.util.Iterator;
import java.util.Scanner;


public class SparkWordCount {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local[6]").setAppName("wordcount");  

        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        final JavaRDD<String> lines = ctx.textFile(args[0]);
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });

        JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {  
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {  
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {  
            @Override  
            public Integer call(Integer integer, Integer integer2) throws Exception {  
                return integer + integer2;  
            }  
        });  
  
        VoidFunction<Tuple2<String, Integer>> f = x -> System.out.println(x);
		counts.foreach(f);
        //counts.saveAsTextFile(args[1]);
		
        Scanner scanner = new Scanner(System.in);
        scanner.next();
        scanner.close();		
		
		ctx.close();
        ctx.stop();
    }  
}  