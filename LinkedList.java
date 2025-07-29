public class LinkedList {
    static class Node{
        int data;
        Node next;
        Node(int data1,Node next1)
        {
            data=data1;
            next=next1;
        }
    }                                                  
    public static void main(String[] args) {
        Node y=new Node(2,null);
        Node z=new Node(3,null);
        y.next=z;
        int[] arr=new int[]{1,2,3,4,5};
        Node head=arrtoLL(arr);
        print(head);
        System.out.println("LENGTH IS "+length(head));
        System.out.println(search(head,8));
        head=remove(head);
        print(head);
        head=remove_tail(head);
        print(head);
        head=delete_kth_element(head,2);
        print(head);
    }
    public static Node arrtoLL(int[] arr) //return the head of the linkedlist
    {
        Node head=new Node(arr[0],null);
        Node mover=head;
        for(int i=1;i<arr.length;i++)
        {
            Node temp=new Node(arr[i],null);
            mover.next=temp;
            mover=temp;
        }
        return head;
    }
    public static int length(Node head)  //length
    {
        Node temp=head;
        int count=0;
        while(temp!=null)
        {
            count++;
            temp=temp.next;
        }
        return count;
    }
    public static boolean search(Node head,int target)  //searching
    {
        Node temp=head;
        while(temp!=null)
        {
            if(temp.data==target)
            {
                return true;
            }
            temp=temp.next;
        }
        return false;
    }
    public static void print(Node head)
    {
        Node temp=head;
        while(temp!=null)
        {
            System.out.println(temp.data);
            temp=temp.next;
        }
    }
    public static Node remove(Node head)
    {
        Node temp=head.next;
        head.next=null;
        head=temp;
        return head;
    }
    public static  Node remove_tail(Node head)
    {
        if (head==null) return head;
        Node tail=head;
        while(tail.next.next!=null)
        {
            tail=tail.next;
        }
        tail.next=null;
        return head;
    }
    public static Node delete_kth_element(Node head,int k)
    {
        if(head==null) return head;
        if(k==1)
        {
            head=head.next;
            return head;
        }
        int count=0;
        Node kth=head;
        while(count!=k-2 && kth!=null && kth.next!=null)
        {
            count++;
            kth=kth.next;
        }
        if(kth==null || kth.next==null) return head;
        kth.next=kth.next.next;
        return head;
    }
}
