import java.util.Scanner;

public class MergeSort {

    public static void mergeSort(int[] temp, int[] array, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(temp, array, low, mid);
            mergeSort(temp, array, mid + 1, high);
            merge(temp, array, low, mid, high);
        }
    }

    public static void merge(int[] temp, int[] array, int low, int mid, int high) {
        int h = low;
        int i = low; // For the temporary array
        int j = mid + 1;

        while (h <= mid && j <= high) {
            if (array[h] <= array[j]) {
                temp[i] = array[h];
                h++;
            } else {
                temp[i] = array[j];
                j++;
            }
            i++;
        }

        // Copy remaining elements of the left half, if any
        while (h <= mid) {
            temp[i] = array[h];
            h++;
            i++;
        }

        // Copy remaining elements of the right half, if any
        while (j <= high) {
            temp[i] = array[j];
            j++;
            i++;
        }

        // Copy the sorted subarray into the original array
        for (int k = low; k <= high; k++) {
            array[k] = temp[k];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] array = new int[50];
        int[] temp = new int[50]; // Temporary array
        int size;

        System.out.print("Enter the size of the array: ");
        size = scanner.nextInt();

        System.out.println("Enter the elements in the array: ");
        for (int i = 0; i < size; i++) {
            array[i] = scanner.nextInt();
        }

        int low = 0;
        int high = size - 1;
        mergeSort(temp, array, low, high);

        System.out.println("List after sorting is:");
        for (int k = low; k <= high; k++) {
            System.out.println(array[k]);
        }

        scanner.close();
    }
}
