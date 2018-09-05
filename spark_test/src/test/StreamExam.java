/*
 * //1.�ҳ�2011�귢�������н��ף��������׶�����(�ӵ͵���)
 * //2.����Ա������Щ��ͬ�ĳ��й�������
 * //3.�����������Խ��ŵĽ���Ա��������������
 * //4.�������н���Ա�������ַ���������ĸ˳������
 * //5.��û�н���Ա�������������ģ�
 * //6.��ӡ�����ڽ��ŵĽ���Ա�����н��׶�
 * //7.���н����У���ߵĽ��׶��Ƕ���
 * //8.�ҵ����׶���С�Ľ���
 */
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamExam {
	    static List<Transaction> transaction=null;

	    private static void init(){
	        Trader raoul=new Trader("Raoul","Cambridge");
	        Trader mario=new Trader("Mario","Milan");
	        Trader alan=new Trader("Alan","Cambridge");
	        Trader brian=new Trader("Brian","Cambridge");

	        transaction=Arrays.asList(
	                new Transaction(brian, 2011, 300),
	                new Transaction(raoul, 2012, 1000),
	                new Transaction(raoul, 2011, 400),
	                new Transaction(mario, 2012, 710),
	                new Transaction(mario, 2012, 700),
	                new Transaction(alan, 2012, 950)
	        );			
		}
		
	    public static void main(String[] args){
			init();
//			test1();
//			test2();
//			test3();
//			test4();
//			test5();
//			test6();
//			test7();
			test8();
		}
		
	    //1.�ҳ�2011�귢�������н��ף��������׶�����(�ӵ͵���)
	    private static void test1(){
	        transaction.stream()
	                   .filter((e)->e.getYear()==2012)
	                   .sorted((e1,e2)->Integer.compare(e1.getValue(), e2.getValue()))
	                   .forEach(System.out::println);
	    }

	    //2.����Ա������Щ��ͬ�ĳ��й�������
	    private static void test2(){
	        transaction.stream()
	                   .map((e)->e.getTrader().getCity())
	                   .distinct()//ȥ��
	                   .forEach(System.out::println);
	    }

	    //3.�����������Խ��ŵĽ���Ա��������������
	    private static void test3(){
	        transaction.stream()
	                   .filter((e)->e.getTrader().getCity().equals("Cambridge"))
	                   .map(Transaction::getTrader)
	                   .sorted((e1,e2)->e1.getName().compareTo(e2.getName()))
	                   .distinct()
	                   .forEach(System.out::println);
	    }

	    //4.�������н���Ա�������ַ���������ĸ˳������
	    private static void test4(){
//	        transaction.stream()
//	                   .map(Transaction::getTrader)
//	                   .map(Trader::getName)
//	                   .distinct()
//	                   .sorted()
//	                   .forEach(System.out::println);
//
//	        System.out.println("-------------------------");
//
//	        String str=transaction.stream()
//	                              .map((e)->e.getTrader().getName())
//	                              .distinct()
//	                              .sorted()
//	                              .reduce("", String::concat);
//	        System.out.println(str);//AlanBrianMarioRaoul
//
//	        System.out.println("-------------------------");

	         transaction.stream()
	                    .map((t)->t.getTrader().getName())
	                    .flatMap(StreamExam::filterCharacter)//���ص�ÿ��String�ϳ�һ����
	                    .sorted((s1,s2)->s1.compareToIgnoreCase(s2))
	                    .forEach(System.out::print);//aaaaaAaBiiilllMMnnoooorRRrruu
	    }

	    private static Stream<String> filterCharacter(String str){
	        List<String> list=new ArrayList<>();
	        for(Character ch:str.toCharArray()){
	            list.add(ch.toString());
	        }
	        return list.stream();
	    }

	    //5.��û�н���Ա�������������ģ�
	    private static void test5(){
	        boolean b1=transaction.stream()
	                              .anyMatch((t)->t.getTrader().getCity().equals("Milan"));
	        System.out.println(b1);
	    }

	    //6.��ӡ�����ڽ��ŵĽ���Ա�����н��׶�
	    private static void test6(){
	        Optional<Integer> sum=transaction.stream()
	                                         .filter((e)->e.getTrader().getCity().equals("Cambridge"))
	                                         .map(Transaction::getValue)
	                                         .reduce(Integer::sum);
	        System.out.println(sum.get());
	    }

	    //7.���н����У���ߵĽ��׶��Ƕ���
	    private static void test7(){
	        Optional<Integer> max=transaction.stream()
	                                         .map((t)->t.getValue())
	                                         .max(Integer::compare);
	        System.out.println(max.get());
	    }

	    //8.�ҵ����׶���С�Ľ���
	    private static void test8(){
	        Optional<Transaction> op=transaction.stream()
	                                            .min((t1,t2)->Integer.compare(t1.getValue(), t2.getValue()));
	        System.out.println(op.get());
	    }
	}

	
class Trader{
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Trader(String name, String city) {
		super();
		this.name = name;
		this.city = city;
	}
	String name;
	String city;
}

class Transaction{

	public Trader getTrader() {
		return trader;
	}
	public void setTrader(Trader trader) {
		this.trader = trader;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Transaction(Trader trader, int year, int value) {
		super();
		this.trader = trader;
		this.year = year;
		this.value = value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trader == null) ? 0 : trader.hashCode());
		result = prime * result + value;
		result = prime * result + year;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (trader == null) {
			if (other.trader != null)
				return false;
		} else if (!trader.equals(other.trader))
			return false;
		if (value != other.value)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	Trader trader;
	int year;
	int value;
}
