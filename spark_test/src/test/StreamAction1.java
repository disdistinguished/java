package test;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import test.Employee.Status;

public class StreamAction1 {
    static List<Employee> employees = Arrays.asList(
							            new Employee("����", 18, 9999.99, Status.FREE),
							            new Employee("����", 58, 5555.55, Status.BUSY),
							            new Employee("����", 26, 3333.33, Status.VOCATION),
							            new Employee("����", 36, 6666.66, Status.FREE),
							            new Employee("����", 12, 8888.88, Status.BUSY)
                              );

	public static void main(String[] args){
    	test3();
    }

    /*
     * ������ƥ��
     */
    private static void test1(){
        boolean b1=employees.stream() //allMatch-����Ƿ�ƥ������Ԫ��
                            .allMatch((e)->e.getStatus().equals(Status.BUSY));
        System.out.println(b1);//false

        boolean b2=employees.stream()//anyMatch-����Ƿ�����ƥ��һ��Ԫ��
                            .anyMatch((e)->e.getStatus().equals(Status.BUSY));
        System.out.println(b2);//true

        boolean b3=employees.stream()//noneMatch-����Ƿ�û��ƥ������Ԫ��
                            .noneMatch((e)->e.getStatus().equals(Status.BUSY));
        System.out.println(b3);//false

        Optional<Employee> op=employees.stream()//findFirst-���ص�һ��Ԫ��//Optional��Java8�б����ָ���쳣��������
                 .sorted((e1,e2)->Double.compare(e1.getSalary(), e2.getSalary()))
                 .findFirst();
        System.out.println(op.get());//Employee [name=����, age=26, salary=3333.33, Status=VOCATION]

        Optional<Employee> op2=employees.stream() //findAny-���ص�ǰ���е�����Ԫ��
                                        .filter((e)->e.getStatus().equals(Status.FREE))
                                        .findAny();
        System.out.println(op2.get());//Employee [name=����, age=36, salary=6666.66, Status=FREE]

        Long count=employees.stream()//count-��������Ԫ�ص��ܸ���
                            .count();
        System.out.println(count);//5

        Optional<Employee> op3=employees.stream()//max-�����������ֵ
                                        .max((e1,e2)->Double.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(op3.get());//Employee [name=����, age=18, salary=9999.99, Status=FREE]

        Optional<Double> op4=employees.stream()//min-����������Сֵ
                                      .map(Employee::getSalary)
                                      .min(Double::compare);
        System.out.println(op4.get());//3333.33
    }

    /*
     * ��Լ
     * reduce(T identity,BinaryOperator b) / reduce(BinaryOperator b)-���Խ�����Ԫ�ط�������������õ�һ��ֵ��
     */
    private static void test2(){
        List<Integer> list=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer sum=list.stream()//reduce(T identity,BinaryOperator b)
                        .reduce(0, (x,y)->x+y);//0Ϊ��ʼֵ
        System.out.println(sum);

        System.out.println("--------------------------");

        Optional<Double> op = employees.stream()//reduce(BinaryOperator b)//û����ʼֵ��map���ؿ���Ϊ�գ����Է���Optional����
                                     .map(Employee::getSalary)
                                     .reduce(Double::sum);
        System.out.println(op.get());
    }

    /*
     * �ռ�
     * collect-����ת��Ϊ������ʽ������һ��Collector�ӿڵ�ʵ�֣����ڸ�Stream��Ԫ�������ܵķ�����
     */
    private static void test3(){
        List<String> list = employees.stream()
                                   .map(Employee::getName)
                                   .collect(Collectors.toList());
        list.forEach(System.out::println);

        System.out.println("----------------------------");

        Set<String> set=employees.stream()
                                 .map(Employee::getName)
                                 .collect(Collectors.toSet());
        set.forEach(System.out::println);

        System.out.println("----------------------------");

        HashSet<String> hs=employees.stream()
                                    .map(Employee::getName)
                                    .collect(Collectors.toCollection(() -> new HashSet<String>()));
        hs.forEach(System.out::println);

        System.out.println("----------------------------");

        //�ܺ�
        Long count=employees.stream()
                            .collect(Collectors.counting());
        System.out.println(count);

        //ƽ��ֵ
        Double avg=employees.stream()
                            .collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(avg);

        //�ܺ�
        Double sum=employees.stream()
                            .collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(sum);

        //���ֵ
        Optional<Employee> max=employees.stream()
                                        .collect(Collectors.maxBy((e1,e2)->Double.compare(e1.getSalary(), e2.getSalary())));
        System.out.println(max.get());

        //��Сֵ
        Optional<Double> min=employees.stream()
                                      .map(Employee::getSalary)
                                      .collect(Collectors.minBy(Double::compare));
        System.out.println(min.get());

        System.out.println("----------------------------");

        //����
        Map<Status,List<Employee>> map=employees.stream()
                                                .collect(Collectors.groupingBy(Employee::getStatus));
        System.out.println(map);//{FREE=[Employee [name=����, age=18, salary=9999.99, Status=FREE], Employee [name=����, age=36, salary=6666.66, Status=FREE]], VOCATION=[Employee [name=����, age=26, salary=3333.33, Status=VOCATION]], BUSY=[Employee [name=����, age=58, salary=5555.55, Status=BUSY], Employee [name=����, age=12, salary=8888.88, Status=BUSY]]}

        //�༶����
        Map<Status,Map<String,List<Employee>>> map2=employees.stream()
                                                            .collect( Collectors.groupingBy( Employee::getStatus,Collectors.groupingBy((e)->{
                                                                if(((Employee) e).getAge() <= 35){
                                                                    return "����";
                                                                }else if(((Employee) e).getAge() <= 50){
                                                                    return "����";
                                                                }else{
                                                                    return "����";
                                                                }
                                                            }) ) );
        System.out.println(map2);//{FREE={����=[Employee [name=����, age=18, salary=9999.99, Status=FREE]], ����=[Employee [name=����, age=36, salary=6666.66, Status=FREE]]}, VOCATION={����=[Employee [name=����, age=26, salary=3333.33, Status=VOCATION]]}, BUSY={����=[Employee [name=����, age=12, salary=8888.88, Status=BUSY]], ����=[Employee [name=����, age=58, salary=5555.55, Status=BUSY]]}}

        //����
        Map<Boolean,List<Employee>> map3=employees.stream()
                                                 .collect(Collectors.partitioningBy((e)->e.getSalary()>8000));
        System.out.println(map3);//{false=[Employee [name=����, age=58, salary=5555.55, Status=BUSY], Employee [name=����, age=26, salary=3333.33, Status=VOCATION], Employee [name=����, age=36, salary=6666.66, Status=FREE]], true=[Employee [name=����, age=18, salary=9999.99, Status=FREE], Employee [name=����, age=12, salary=8888.88, Status=BUSY]]}

        System.out.println("--------------------------------");

        DoubleSummaryStatistics dss=employees.stream()
                                             .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(dss.getSum());
        System.out.println(dss.getAverage());
        System.out.println(dss.getMax());

        System.out.println("--------------------------------");
        String strr=employees.stream()
                             .map(Employee::getName)
                             .collect(Collectors.joining(","));
        System.out.println(strr);//����,����,����,����,����
     }
}
