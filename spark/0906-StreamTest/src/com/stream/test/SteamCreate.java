package com.stream.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SteamCreate {

	public static void main(String[] a){
		test1();
	}

	//����Stream
    private static void test1(){
        //1.����ͨ��Collection ϵ�м����ṩ��stream()��parallelStream()
        List<String> list = new ArrayList<>();
        Stream<String> stream1 = list.stream();

        //2.ͨ�� Arrays �еľ�̬����stream()��ȡ������
        Employee[] emps = new Employee[10];
        Stream<Employee> stream2 = Arrays.stream(emps);

        //3.ͨ��Stream ���еľ�̬����of()
        Stream<String> stream3 = Stream.of("aa","bb","cc");

        //4.����������
        //����
        Stream<Integer> stream4 = Stream.iterate(0, x -> x+2);
        stream4.limit(10).forEach(System.out::println);
        //stream4.limit(10).forEach(x -> System.out.println(x));

        //����
//        Stream.generate(() -> Math.random())
//              .limit(5)
//              .forEach(System.out::println);
    }
	
}

