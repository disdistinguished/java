package test;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;


public class ParellelStreamTest1 {
    public static void main(String[] args){
        test1();
    }
    
    private static void test1(){
        Instant start=Instant.now();

        Long sum = LongStream.rangeClosed(0L, 10000000000L)
                             .parallel()
                             .reduce(0,Long::sum);
        System.out.println(sum);

        Instant end=Instant.now();
        System.out.println("����ʱ��" + Duration.between(start, end).toMillis()+"ms");//����ʱ��2418ms
    }    
}
