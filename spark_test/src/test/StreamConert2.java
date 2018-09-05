package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class StreamConert2 {
    static List<Employee> employees=Arrays.asList(
            new Employee("����", 18, 9999.99),
            new Employee("����", 58, 5555.55),
            new Employee("����", 26, 3333.33),
            new Employee("����", 36, 6666.66),
            new Employee("����", 12, 8888.88),
            new Employee("����", 12, 8888.88)
            );
	
	public static void main(String[] a){
		test7();
	}

	//�м����
	/*
	 * ӳ��
	 * map--����Lambda����Ԫ��ת����������ʽ����ȡ��Ϣ������һ��������Ϊ�������ú����ᱻӦ�õ�ÿ��Ԫ���ϣ�������ӳ���һ����Ԫ�ء�
	 * flatMap--����һ��������Ϊ�����������е�ÿ��ֵ��������һ������Ȼ������������ӳ�һ����
	 */
	private static void test5(){
	    List<String> list = Arrays.asList("aaa","bbb","ccc","ddd");
	    list.stream()
	         //.map((str)->str.toUpperCase())
	         .map(String::toUpperCase)
	         .forEach(System.out::println);
	    System.out.println("------------------------");

	    employees.stream()
	             //.map(x -> x.getName())
	             .map(Employee::getName)
	             .forEach(System.out::println);
	    System.out.println("------------------------");

	    Stream<Stream<Character>> stream = list.stream().map(StreamConert2::filterChatacter);
	    stream.forEach((sm)->{
	        sm.forEach(System.out::println);
	        System.out.println();
	    });

	    System.out.println("------------------------");

	    Stream<Character> sm=list.stream().flatMap(StreamConert2::filterChatacter);
	    sm.forEach(System.out::println);
	}
	
	private static Stream<Character> filterChatacter(String str){
	    List<Character> list = new ArrayList<>();
	    for (Character ch : str.toCharArray()) {
	        list.add(ch);
	    }
	    return list.stream();
	}

	private static void test6(){//map��flatMap�Ĺ�ϵ  ������ add(Object)��addAll(Collection coll)
	    List<String> list=Arrays.asList("aaa","bbb","ccc","ddd");
	    List list2 = new ArrayList<>();
	    list2.add(11);
	    list2.add(22);
	    list2.addAll(list);
	    System.out.println(list2);
	}
	
	//�м����
    /*
     * ����
     * sorted()-��Ȼ���򣨰��ն�����ʵ��Comparable�ӿڵ�compareTo()���� ����
     * sorted(Comparator com)-��������Comparator��
     */
    private static void test7(){
        List<String> list = Arrays.asList("ccc","bbb","aaa");
        list.stream()
            //.sorted((x1, x2) -> x2.compareTo(x1))
            .sorted(String::compareTo)
            .forEach(System.out::println);

        System.out.println("------------------------");

        employees.stream()
                 .sorted((e1,e2)->{
                     if(e1.getAge() == (e2.getAge())){
                         return e1.getName().compareTo(e2.getName());
                     }else{
                         return e1.getAge() - e2.getAge();
                     }
                 }).forEach(System.out::println); 
    }
}
