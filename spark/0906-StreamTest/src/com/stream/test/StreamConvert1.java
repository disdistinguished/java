package com.stream.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class StreamConvert1 {
    static List<Employee> employees=Arrays.asList(
            new Employee("张三", 18, 9999.99),
            new Employee("李四", 58, 5555.55),
            new Employee("王五", 26, 3333.33),
            new Employee("赵六", 36, 6666.66),
            new Employee("田七", 12, 8888.88),
            new Employee("田七", 12, 8888.88)
            );
    
	public static void main(String[] xxx){
		test4();
    }
    
    /*  筛选与切片
     *  filter--接收Lambda，从流中排除某些元素。
     *  limit--截断流，使其元素不超过给定数量。
     *  skip(n)--跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流。与limit(n) 互补
     *  distinct--筛选，通过流所生成元素的 hashCode() 和 equals() 去掉重复元素
     */

    //内部迭代：迭代操作由 Stream API 完成
    private static void test1(){
        //中间操作：不会执行任何操作
        Stream<Employee> stream = employees.stream().filter((e) -> e.getAge() > 35);
        //终止操作：一次性执行全部内容，即 惰性求值
        stream.forEach(System.out::println);
    }
    
    //外部迭代
    private static void test2(){
        Iterator<Employee> it = employees.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }

    private static void test3(){//发现“短路”只输出了两次，说明只要找到 2 个 符合条件的就不再继续迭代
        employees.stream()
                 .filter((e)->{
                     System.out.println("短路！");
                     return e.getSalary() > 5000;
                 })
                 .limit(2)
                 .forEach(System.out::println);
    }

    private static void test4(){
        employees.stream()
                 .filter((e)->e.getSalary()>5000)
                 .skip(2)//跳过前两个
                 .distinct()//去重，注意：需要Employee重写hashCode 和 equals 方法
                 .forEach(System.out::println);
    }
}
