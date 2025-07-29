import java.util.*;

public class ThreeSumWithHashMap {
    public List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> resultSet = new HashSet<>();
        Map<Integer, List<Integer>> valueToIndices = new HashMap<>();

        // Step 1: Build a map of number -> list of indices where it appears
        for (int i = 0; i < nums.length; i++) {
            valueToIndices.computeIfAbsent(nums[i], x -> new ArrayList<>()).add(i);
        }

        // Step 2: Loop through all pairs (i, j)
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                int target = -(nums[i] + nums[j]); // Step 3: Compute third number

                // Step 4: Check if target exists in the map
                if (valueToIndices.containsKey(target)) {
                    for (int k : valueToIndices.get(target)) {
                        // Step 5: Ensure index k is not the same as i or j
                        if (k != i && k != j) {
                            // Step 6: Create and sort the triplet
                            List<Integer> triplet = Arrays.asList(nums[i], nums[j], target);
                            Collections.sort(triplet);

                            // Step 7: Add to the set to avoid duplicates
                            resultSet.add(triplet);
                            break; // Only one valid index needed for this target
                        }
                    }
                }
            }
        }

        // Step 8: Convert the set to a list and return
        return new ArrayList<>(resultSet);
    }

    // Optional main method to test
    public static void main(String[] args) {
        ThreeSumWithHashMap solver = new ThreeSumWithHashMap();
        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result = solver.threeSum(nums);
        System.out.println(result);
    }
}
