import java.util.LinkedList;

public class glab2_2 {
    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();

        // Insert elements
        linkedList.add("Element1");
        linkedList.add("Element2");
        linkedList.add("Element3");

        // Insert at specified position
        linkedList.add(1, "InsertedElement");

        // Insert at first and last position
        linkedList.addFirst("FirstElement");
        linkedList.addLast("LastElement");

        // Iterate in reverse order
        for (int i = linkedList.size() - 1; i >= 0; i--) {
            System.out.println(linkedList.get(i));
        }

        // Remove specified element
        linkedList.remove("Element2");

        // Retrieve last element
        String lastElement = linkedList.getLast();
        System.out.println("Last element: " + lastElement);

        // Remove and return first element
        String firstElement = linkedList.poll();
        System.out.println("Removed first element: " + firstElement);
    }
}
