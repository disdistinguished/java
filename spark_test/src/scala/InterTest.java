package scala;

interface MyInter {
    default void df(){    //澹版槑涓�涓帴鍙ｇ殑榛樿鏂规硶
        System.out.println("MyInter df");
        //sf();        //璋冪敤鏈帴鍙ｇ殑绫绘柟娉�
    }
//    static void sf(){//澹版槑涓�涓帴鍙ｇ殑绫绘柟娉�
//        System.out.println("i'am static f");
//    }
    
    static int A = 8;
}

class Man implements MyInter{    //Man绫诲疄鐜癕yInter鎺ュ彛
//	@Override
//	public void df(){//澹版槑涓�涓帴鍙ｇ殑绫绘柟娉�
//        System.out.println("Man df");
//    }
}

public class InterTest extends Man{
    public static void main(String[] args) {
    	MyInter man = new Man();
        //man.df();            //閫氳繃man瀵硅薄璋冪敤MyInter鎺ュ彛鐨勯粯璁ゆ柟娉昫f()
        man.df();
    }
}