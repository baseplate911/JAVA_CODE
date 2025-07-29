import java.util.*;
public class acc {
    public static void deposit(int balance,int deposit)
    {
        int new_balance=balance+deposit;
        System.out.println("NEW BALANCE AFTER DEPOSIT IS"+new_balance);
    }
    public static void withdraw(int balance,int withdraw)
    {
        int new_balance=balance-withdraw;
        System.out.println("NEW BALANCE AFTER WITHDRAW IS "+new_balance);
    }
    public static void main(String[] args) 
    {
    System.out.println("ENTER YOUR BALANCE");
    Scanner sc=new Scanner(System.in);
    int balance=sc.nextInt();
    System.out.println("ENTER YOUR CHOICE\n1.DEPOSIT\n2.WITHDRAW");
    int choice=sc.nextInt();
    switch(choice)
    {
        case 1:
        {
            System.out.println("ENTER THE AMOUNT TO BE DEPOSIT");
            int depsoit=sc.nextInt();
            deposit(balance,depsoit);
            break;
        }
        case 2:
        {
            System.out.println("ENTER THE AMOUNT TO BE WITHDRAW");
            int withdraw=sc.nextInt();
            withdraw(balance,withdraw);
            break;
        }
        default:
        {
            System.out.print("ENTER VALID INPUT");
        }
    }
    sc.close();
    }
    
   
    
}
