package test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class TestForkJoin {

    public static void main(String[] xx){
        Instant start=Instant.now();

        ForkJoinPool pool=new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinCalculate(0L, 10000000000L);
        long sum = pool.invoke(task);
        System.out.println(sum);

        Instant end=Instant.now();
        System.out.println("消耗时间"+Duration.between(start, end).toMillis()+"ms");//消耗时间3409ms
    }
}

class ForkJoinCalculate extends RecursiveTask<Long>{

    private static final long serialVersionUID = 1234567890L;//序列号

    private long start;
    private long end;
    private static final long THRESHOLD=1000000L;//临界值

    public ForkJoinCalculate(long start,long end) {
        this.start=start;
        this.end=end;
    }

    @Override
    protected Long compute() {
        long length = end - start;
        if(length <= THRESHOLD){
            long sum=0;
            for(long i = start; i <= end; i++){
                sum += i;
            }
            return sum;
        }else{
            long middle = (start+end)/2;
            ForkJoinCalculate left = new ForkJoinCalculate(start, middle);
            left.fork();

            ForkJoinCalculate right=new ForkJoinCalculate(middle+1, end);
            right.fork();

            return left.join() + right.join();
        }
    }

}
