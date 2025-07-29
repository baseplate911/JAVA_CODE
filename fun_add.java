import java.util.*;
public class fun_add {
    public static void add(int a,int b)
    {
        System.out.println("The sum of "+a+" and "+b+" is "+(a+b));
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int num_1=sc.nextInt();
        int num_2=sc.nextInt();
        //we are returning the addition of the given two numbers by the means of a funtion named add
        add(num_1,num_2);
        sc.close();

        
    }
}
