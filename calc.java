import java.util.*;
public class calc {
    public static void main(String[] args) 
    {
    System.out.println("1.ADD 2.SUBSTRACT 3.MULTIPLY 4.DIVIDE");
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    System.out.println("ENTER FIRST NUMBER");
    int b=sc.nextInt();
    System.out.println("ENTER SECOND NUMBER");
    int c=sc.nextInt();
    switch(choice)
    {
        case 1:
        {
            System.out.println(b+c);
            break;
        }
        case 2:
        {
            System.out.println(b-c);
            break;
        }
         case 3:
        {
            System.out.println(b*c);
            break;
        }
         case 4:
        {
            System.out.println(b/c);
            break;
        }
        default :
        {
         System.out.println("ENTER A VALID INPUT");   
        }
        sc.close();
    }

    }
    
}
