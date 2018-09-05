package scala1;


public class RecursiveDemo {
    public static void main(String[] args) {
    	System.out.println(tail_func(5, 1));
	}

    int sum(int limit){
    	int res = 0;
    	for(int i = 0; i < limit; i++){
    		res += i;
    	}
    	return res;
    }
    
    //鏅�氶�掑綊
	static int func(int n) {
	    if (n <= 1) 
	    	return 1;
	    return (n * func(n-1));
	}

	//灏鹃�掑綊鏄寚閫掑綊璋冪敤鏄嚱鏁扮殑鏈�鍚庝竴涓鍙ワ紝鑰屼笖鍏剁粨鏋滆鐩存帴杩斿洖锛岃繖鏄竴绫荤壒娈婄殑閫掑綊璋冪敤銆�
	static int tail_func(int n, int res) {
	     if (n <= 1)
	    	 return res;
	     return tail_func(n - 1, n * res);
	}
/*
 * n(5)      res(1)
 * n(4)      res(5*4)
 * n(3)      res(5*4*3)
 * n(2)      res(5*4*3*2)
 * n(1)      res(5*4*3*2*1)
 */
}