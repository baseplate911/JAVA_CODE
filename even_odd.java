
import java.util.*;
public class even_odd {
    public static void odd_even(int num)
    {
         if(num%2==0)
        {
            System.out.println(num+" IS EVEN");
        }
        else{
            System.out.println(num+" IS ODD");
            }  
    }
    public static void main(String[] args) {
        System.out.println("ENTER A NUMBER");
        Scanner sc=new Scanner(System.in);
        int num=sc.nextInt();
        odd_even(num);
        sc.close();
    }
}