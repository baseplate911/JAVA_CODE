public class glab3_1 {
    public static void main(String[] args) {
        try {
            // This code may throw various exceptions
            int[] numbers = {1, 2, 3};
            int index = 4;
            int result = numbers[index] / 0; // Division by zero will throw ArithmeticException
            System.out.println("Result: " + result); // This line won't be executed
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}