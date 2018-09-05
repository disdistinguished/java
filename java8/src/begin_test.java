


public class begin_test {
private static void test4(){
	List<String> list = Arrays.asList("Hello","jj","lambda","www","ok");
	List<String> strlist = fileerStr(list, (s)-> s.length()>3);
	for (String string : strlist){
		System.out.println(string);
	}
}
private static List<String> filterStr(List<String> list, Predicate<String> pre){
	List<String> strList = new ArrayList<>();
	for (String str : list){
		if(pre.test(str)){
			strList.add(str);
		}
	}
	return strList;
}
private static void test7(){
	
}
}    
