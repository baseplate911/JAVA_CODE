import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class knapsackSolver {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(knapsackSolver::new);
    }

    public knapsackSolver() {
        // Set up the main frame
        JFrame frame = new JFrame("0/1 Knapsack Problem Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Inputs"));

        JLabel weightsLabel = new JLabel("Weights (comma-separated):");
        JTextField weightsField = new JTextField("3,4,5,6");
        JLabel profitsLabel = new JLabel("Profits (comma-separated):");
        JTextField profitsField = new JTextField("2,3,4,1");
        JLabel capacityLabel = new JLabel("Knapsack Capacity:");
        JTextField capacityField = new JTextField("8");
        JButton solveButton = new JButton("Solve");

        inputPanel.add(weightsLabel);
        inputPanel.add(weightsField);
        inputPanel.add(profitsLabel);
        inputPanel.add(profitsField);
        inputPanel.add(capacityLabel);
        inputPanel.add(capacityField);
        inputPanel.add(new JLabel());
        inputPanel.add(solveButton);

        // Output area
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Solve button action
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Parse input values
                    String[] weightsInput = weightsField.getText().split(",");
                    String[] profitsInput = profitsField.getText().split(",");
                    int capacity = Integer.parseInt(capacityField.getText());

                    int n = weightsInput.length;
                    int[] weights = new int[n];
                    int[] profits = new int[n];

                    for (int i = 0; i < n; i++) {
                        weights[i] = Integer.parseInt(weightsInput[i].trim());
                        profits[i] = Integer.parseInt(profitsInput[i].trim());
                    }

                    // Solve the knapsack problem
                    int[][] dp = solveKnapsackDP(weights, profits, capacity);

                    // Generate the formatted output
                    String solutionMatrix = formatSolutionMatrix(dp, weights, profits);
                    String selectedItems = getSelectedItems(dp, weights, profits, capacity);
                    int maxProfit = dp[n][capacity];

                    // Display the output
                    outputArea.setText("Solution Matrix (Dynamic Programming):\n\n");
                    outputArea.append(solutionMatrix + "\n");
                    outputArea.append("Selected Items: " + selectedItems + "\n");
                    outputArea.append("Maximum Profit: " + maxProfit + "\n");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input! Please check the format and try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private int[][] solveKnapsackDP(int[] weights, int[] profits, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - weights[i - 1]] + profits[i - 1]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        return dp;
    }

    private String formatSolutionMatrix(int[][] dp, int[] weights, int[] profits) {
        StringBuilder sb = new StringBuilder();
        int n = weights.length;
        int capacity = dp[0].length - 1;

        // Header
        sb.append(String.format("%-10s %-10s %-10s", "Profit", "Weight", "Item"));
        for (int w = 0; w <= capacity; w++) {
            sb.append(String.format("%4d", w));
        }
        sb.append("\n");

        // Matrix rows
        sb.append(String.format("%-10s %-10s %-10s", "0", "0", "0"));
        for (int w = 0; w <= capacity; w++) {
            sb.append(String.format("%4d", dp[0][w]));
        }
        sb.append("\n");

        for (int i = 1; i <= n; i++) {
            sb.append(String.format("%-10d %-10d %-10d", profits[i - 1], weights[i - 1], i));
            for (int w = 0; w <= capacity; w++) {
                sb.append(String.format("%4d", dp[i][w]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getSelectedItems(int[][] dp, int[] weights, int[] profits, int capacity) {
        StringBuilder sb = new StringBuilder();
        int w = capacity;
        for (int i = weights.length; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                sb.append(String.format("Item %d (weight: %d, profit: %d) ", i, weights[i - 1], profits[i - 1]));
                w -= weights[i - 1];
            }
        }
        return sb.toString();
    }
}
