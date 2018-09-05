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

public class SparkIO_JSON {
    public static void main(String[] args) {
    	SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("SparkIO");
		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.setLogLevel("WARN");
		readJsonTest(sc);
		sc.stop();
		sc.close();
	}

    //璇籎SON
    static void readJsonTest(JavaSparkContext sc){
    	JavaRDD<String> input = sc.textFile("file:///f:/Tools/song.json");
    	JavaRDD<Mp3Info> result = input.mapPartitions(new ParseJson());
    	result.foreach(x->System.out.println(x));
    }
    //鍐橨SON
    static void writeJsonTest(JavaSparkContext sc){
    	JavaRDD<String> input = sc.textFile("file:///f:/Tools/song.json");
    	JavaRDD<Mp3Info> result = input.mapPartitions(new ParseJson()).
    			                      filter(
    			                          x->x.getAlbum().equals("鎬�鏃т笓杈�")
    			                      );
    	JavaRDD<String> formatted = result.mapPartitions(new WriteJson());
    	result.foreach(x->System.out.println(x));
    	formatted.saveAsTextFile("file:///f:/Tools/oldsong");
    }
}

class ParseJson implements FlatMapFunction<Iterator<String>, Mp3Info>, Serializable {
	public Iterator<Mp3Info> call(Iterator<String> lines) throws Exception {
		ArrayList<Mp3Info> people = new ArrayList<Mp3Info>();
		ObjectMapper mapper = new ObjectMapper();
		while (lines.hasNext()) {
			String line = lines.next();
			try {
				people.add(mapper.readValue(line, Mp3Info.class));
			} catch (Exception e) {
			    e.printStackTrace();
			}
		}
		return people.iterator();
	}
}

class WriteJson implements FlatMapFunction<Iterator<Mp3Info>, String> {
	public Iterator<String> call(Iterator<Mp3Info> song) throws Exception {
		ArrayList<String> text = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		while (song.hasNext()) {
			Mp3Info person = song.next();
		    text.add(mapper.writeValueAsString(person));
		}
		return text.iterator();
	}
}

class Mp3Info implements Serializable{
	/*
{"name":"涓婃捣婊�","singer":"鍙朵附浠�","album":"棣欐腐鐢佃鍓т富棰樻瓕","path":"mp3/shanghaitan.mp3"}
{"name":"涓�鐢熶綍姹�","singer":"闄堢櫨寮�","album":"棣欐腐鐢佃鍓т富棰樻瓕","path":"mp3/shanghaitan.mp3"}
{"name":"绾㈡棩","singer":"鏉庡厠鍕�","album":"鎬�鏃т笓杈�","path":"mp3/shanghaitan.mp3"}
{"name":"鐖卞娼按","singer":"寮犱俊鍝�","album":"鎬�鏃т笓杈�","path":"mp3/airucaoshun.mp3"}
{"name":"绾㈣尪棣�","singer":"闄堟儬瀚�","album":"鎬�鏃т笓杈�","path":"mp3/redteabar.mp3"}
	 */
	private String name;
    private String album;
    private String path;
    private String singer;

    public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
    @Override
	public String toString() {
		return "Mp3Info [name=" + name + ", album=" 
	             + album + ", path=" + path + ", singer=" + singer + "]";
	}
}

/*
{"name":"涓婃捣婊�","singer":"鍙朵附浠�","album":"棣欐腐鐢佃鍓т富棰樻瓕","path":"mp3/shanghaitan.mp3"}
{"name":"涓�鐢熶綍姹�","singer":"闄堢櫨寮�","album":"棣欐腐鐢佃鍓т富棰樻瓕","path":"mp3/shanghaitan.mp3"}
{"name":"绾㈡棩","singer":"鏉庡厠鍕�","album":"鎬�鏃т笓杈�","path":"mp3/shanghaitan.mp3"}
{"name":"鐖卞娼按","singer":"寮犱俊鍝�","album":"鎬�鏃т笓杈�","path":"mp3/airucaoshun.mp3"}
{"name":"绾㈣尪棣�","singer":"闄堟儬瀚�","album":"鎬�鏃т笓杈�","path":"mp3/redteabar.mp3"}
 */