import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class glab2_1 {
    public static void main(String[] args) {
        // a) To insert 5 Elements of string type.
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Apple");
        arrayList.add("Banana");
        arrayList.add("Orange");
        arrayList.add("Mango");
        arrayList.add("Grapes");

        // b) To insert Element at a specific Index.
        arrayList.add(2, "Pineapple");

        // c) To retrieve an element (at a specified index) from a given array list.
        //String elementAtIndex3 = arrayList.get(3);
        //System.out.println("Element at index 3: " + elementAtIndex3);
        System.out.println("element at index 3  "+arrayList.get(3));

        // d) To update an array element by the given element.
        arrayList.set(1, "Watermelon");

        // e) To delete Element at a specific Index.
        arrayList.remove(4);

        // f) To search for an element in an array list.
        String searchElement = "Mango";
        boolean isElementFound = arrayList.contains(searchElement);
        System.out.println("Is Mango present in the list? " + isElementFound);

        // g) To sort the array list.
        Collections.sort(arrayList);

        // h) To reverse elements in an array list.
        Collections.reverse(arrayList);

        // i) To iterate through all the elements of an ArrayList.
        System.out.println("Elements of ArrayList:");
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
