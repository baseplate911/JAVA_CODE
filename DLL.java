public class DLL
{
public static void main(String[] args) {
    Node first=new Node(1,null,null);
    Node head=first;
    Node prev=head;
    Node second=new Node(2,null,null);
    prev.next=second;
    second.prev=prev;
    System.out.println(head.data);
    System.out.println(head.next.data);
    System.out.println(head.next.prev.data);
    Node temp=head;
    head=head.next;
    head.prev=null;
    temp.next=null;
    System.out.println(head.data);
}
 static class Node
{
    int data;
    Node next;
    Node prev;
    Node(int data1,Node next1,Node prev1)
    {
        data=data1;
        next=next1;
        prev=prev1;
    }
}
}