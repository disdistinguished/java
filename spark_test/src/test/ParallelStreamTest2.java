package test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
	Parallel Streams , �������������
	��������˳���Ҳ�����ǲ��еġ�˳�����Ĳ������ڵ��߳���ִ�еģ����������Ĳ������ڶ��߳��ϲ���ִ�еġ�
 */
public class ParallelStreamTest2 {
	
	public static void main(String[] args){
		ParallelStreamTest2 ps = new ParallelStreamTest2();
//		ps.sequentialSort();
		ps.parallelSort();
	}
	
	int max = 1000000;
	List<String> values;
	
	public ParallelStreamTest2(){
		//����һ������ΨһԪ�صĴ�������		
		values = new ArrayList<String>();
		for(int i = max; i > 0; i--){
			UUID uuid = UUID.randomUUID();
			values.add(uuid.toString());	
		}
	}
	
	//����������ЩԪ����Ҫ�೤ʱ�䡣
	
	//Sequential Sort, ����˳������������
	private void sequentialSort(){	
		long t0 = System.nanoTime();
		
		long count = values.stream().sorted().count();
		System.err.println("count = " + count);
		
		long t1 = System.nanoTime();
		
		long millis  = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("sequential sort took: %d ms", millis));  
		//sequential sort took: 1932 ms
		
	}

	//parallel Sort, ���ò�������������
	private void parallelSort(){	
		long t0 = System.nanoTime();
		
		long count = values.parallelStream().sorted().count();
		System.err.println("count = " + count);
		
		long t1 = System.nanoTime();
		
		long millis  = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("parallel sort took: %d ms", millis));  
		//parallel sort took: 1373 ms �������������ѵ�ʱ���Լ��˳�������һ�롣
	}
}

