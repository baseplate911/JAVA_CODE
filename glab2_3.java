import java.util.*;

public class glab2_3 {
    public static void main(String[] args) {
        // Creating a TreeMap
        TreeMap<Integer, String> treeMap = new TreeMap<>();

        // Adding elements to the TreeMap
        treeMap.put(1, "One");
        treeMap.put(2, "Two");
        treeMap.put(3, "Three");
        treeMap.put(4, "Four");
        treeMap.put(5, "Five");

        // a) To get all keys from a TreeMap
        Set<Integer> keys = treeMap.keySet();
        System.out.println("Keys in the TreeMap: " + keys);

        // b) To delete all elements from a TreeMap
        treeMap.clear();
        System.out.println("TreeMap after clearing all elements: " + treeMap);

        // Re-adding elements for further demonstration
        treeMap.put(1, "One");
        treeMap.put(2, "Two");
        treeMap.put(3, "Three");
        treeMap.put(4, "Four");
        treeMap.put(5, "Five");

        // c) To get the first (lowest) key and the last (highest) key currently in a map
        if (!treeMap.isEmpty()) {
            int firstKey = treeMap.firstKey();
            int lastKey = treeMap.lastKey();
            System.out.println("First key in the TreeMap: " + firstKey);
            System.out.println("Last key in the TreeMap: " + lastKey);
        } else {
            System.out.println("TreeMap is empty.");
        }
    }
}
