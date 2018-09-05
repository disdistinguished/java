package com.stream.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class StreamConvert1 {
    static List<Employee> employees=Arrays.asList(
            new Employee("����", 18, 9999.99),
            new Employee("����", 58, 5555.55),
            new Employee("����", 26, 3333.33),
            new Employee("����", 36, 6666.66),
            new Employee("����", 12, 8888.88),
            new Employee("����", 12, 8888.88)
            );
    
	public static void main(String[] xxx){
		test4();
    }
    
    /*  ɸѡ����Ƭ
     *  filter--����Lambda���������ų�ĳЩԪ�ء�
     *  limit--�ض�����ʹ��Ԫ�ز���������������
     *  skip(n)--����Ԫ�أ�����һ���ӵ���ǰn��Ԫ�ص�����������Ԫ�ز���n�����򷵻�һ����������limit(n) ����
     *  distinct--ɸѡ��ͨ����������Ԫ�ص� hashCode() �� equals() ȥ���ظ�Ԫ��
     */

    //�ڲ����������������� Stream API ���
    private static void test1(){
        //�м����������ִ���κβ���
        Stream<Employee> stream = employees.stream().filter((e) -> e.getAge() > 35);
        //��ֹ������һ����ִ��ȫ�����ݣ��� ������ֵ
        stream.forEach(System.out::println);
    }
    
    //�ⲿ����
    private static void test2(){
        Iterator<Employee> it = employees.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    private static void test3(){//���֡���·��ֻ��������Σ�˵��ֻҪ�ҵ� 2 �� ���������ľͲ��ټ�������
        employees.stream()
                 .filter((e)->{
                     System.out.println("��·��");
                     return e.getSalary() > 5000;
                 })
                 .limit(2)
                 .forEach(System.out::println);
    }

    private static void test4(){
        employees.stream()
                 .filter((e)->e.getSalary()>5000)
                 .skip(2)//����ǰ����
                 .distinct()//ȥ�أ�ע�⣺��ҪEmployee��дhashCode �� equals ����
                 .forEach(System.out::println);
    }
}
