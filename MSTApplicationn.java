import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MSTApplicationn extends JFrame {
    // Inner class definitions
    private static class Node {
        double x, y;
        double vx, vy;
        
        public Node(double x, double y) {
            this.x = x;
            this.y = y;
            this.vx = 0;
            this.vy = 0;
        }
    }

    private static class Edge implements Comparable<Edge> {
        String from, to;
        int weight;

        public Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    private static class UnionFind {
        private Map<String, String> parent;

        public UnionFind(Set<String> nodes) {
            parent = new HashMap<>();
            for (String node : nodes) {
                parent.put(node, node);
            }
        }

        public String find(String node) {
            if (!parent.get(node).equals(node)) {
                parent.put(node, find(parent.get(node)));
            }
            return parent.get(node);
        }

        public boolean union(String node1, String node2) {
            String root1 = find(node1);
            String root2 = find(node2);

            if (!root1.equals(root2)) {
                parent.put(root1, root2);
                return true;
            }
            return false;
        }
    }

    // Main class fields
    private JPanel graphPanel;
    private JTextField edgeInput;
    private JTextField weightInput;
    private JButton addEdgeButton;
    private JButton calculateMSTButton;
    private JButton clearButton;
    private JComboBox<String> algorithmChoice;
    private JTextArea resultArea;
    
    private Map<String, Node> nodes = new HashMap<>();
    private List<Edge> edges = new ArrayList<>();
    private List<Edge> mstEdges = new ArrayList<>();
    
    // Force-directed layout parameters
    private static final double REPULSION = 8000;
    private static final double SPRING = 0.5;
    private static final double EDGE_LENGTH = 150;
    private javax.swing.Timer layoutTimer;  // Explicitly using javax.swing.Timer
    private boolean isLayoutStabilized = false;

    public MSTApplicationn() {
        setTitle("Minimum Spanning Tree Calculator");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeGraphPanel();
        initializeInputPanel();
        initializeResultPanel();
        
        layoutTimer = new javax.swing.Timer(50, e -> {
            try {
                if (!isLayoutStabilized) {
                    updateLayout();
                    graphPanel.repaint();
                }
            } catch (Exception ex) {
                handleError("Layout update error: " + ex.getMessage());
            }
        });
    }

    private void initializeGraphPanel() {
        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                try {
                    drawGraph(g2d);
                } catch (Exception e) {
                    handleError("Error drawing graph: " + e.getMessage());
                }
            }
        };
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setPreferredSize(new Dimension(600, 500));
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(graphPanel, BorderLayout.CENTER);
    }

    private void drawGraph(Graphics2D g2d) {
        // Draw edges
        for (Edge edge : edges) {
            Node n1 = nodes.get(edge.from);
            Node n2 = nodes.get(edge.to);
            
            boolean isMST = mstEdges.contains(edge);
            
            if (isMST) {
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(2.0f));
            } else {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1.0f));
            }
            
            g2d.drawLine((int)n1.x, (int)n1.y, (int)n2.x, (int)n2.y);
            
            // Draw weight
            g2d.setColor(Color.BLUE);
            g2d.drawString(String.valueOf(edge.weight), 
                         (int)((n1.x + n2.x) / 2), 
                         (int)((n1.y + n2.y) / 2));
        }
        
        // Draw nodes
        g2d.setColor(Color.BLACK);
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            String label = entry.getKey();
            Node node = entry.getValue();
            
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int)node.x - 15, (int)node.y - 15, 30, 30);
            g2d.setColor(Color.BLACK);
            g2d.drawOval((int)node.x - 15, (int)node.y - 15, 30, 30);
            g2d.drawString(label, (int)node.x - 5, (int)node.y + 5);
        }
    }

    private void updateLayout() {
        Map<Node, double[]> forces = new HashMap<>();
        
        // Initialize forces
        for (Node node : nodes.values()) {
            forces.put(node, new double[]{0, 0});
        }
        
        // Calculate repulsive forces
        List<Node> nodeList = new ArrayList<>(nodes.values());
        for (int i = 0; i < nodeList.size(); i++) {
            for (int j = i + 1; j < nodeList.size(); j++) {
                Node n1 = nodeList.get(i);
                Node n2 = nodeList.get(j);
                
                double dx = n2.x - n1.x;
                double dy = n2.y - n1.y;
                double dist = Math.sqrt(dx * dx + dy * dy);
                
                if (dist < 1) dist = 1;
                
                double force = REPULSION / (dist * dist);
                double fx = (dx / dist) * force;
                double fy = (dy / dist) * force;
                
                forces.get(n1)[0] -= fx;
                forces.get(n1)[1] -= fy;
                forces.get(n2)[0] += fx;
                forces.get(n2)[1] += fy;
            }
        }
        
        // Calculate spring forces
        for (Edge edge : edges) {
            Node n1 = nodes.get(edge.from);
            Node n2 = nodes.get(edge.to);
            
            double dx = n2.x - n1.x;
            double dy = n2.y - n1.y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if (dist < 1) dist = 1;
            
            double force = (dist - EDGE_LENGTH) * SPRING;
            double fx = (dx / dist) * force;
            double fy = (dy / dist) * force;
            
            forces.get(n1)[0] += fx;
            forces.get(n1)[1] += fy;
            forces.get(n2)[0] -= fx;
            forces.get(n2)[1] -= fy;
        }
        
        // Update positions
        double maxMove = 0;
        for (Node node : nodes.values()) {
            double[] force = forces.get(node);
            node.vx = (node.vx + force[0]) * 0.5;
            node.vy = (node.vy + force[1]) * 0.5;
            
            node.x += node.vx;
            node.y += node.vy;
            
            node.x = Math.max(50, Math.min(graphPanel.getWidth() - 50, node.x));
            node.y = Math.max(50, Math.min(graphPanel.getHeight() - 50, node.y));
            
            maxMove = Math.max(maxMove, Math.abs(node.vx) + Math.abs(node.vy));
        }
        
        isLayoutStabilized = maxMove < 0.1;
    }

    private void initializeInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Edge input
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Edge (e.g., A-B):"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        edgeInput = new JTextField(10);
        inputPanel.add(edgeInput, gbc);

        // Weight input
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Weight:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        weightInput = new JTextField(10);
        inputPanel.add(weightInput, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        addEdgeButton = new JButton("Add Edge");
        addEdgeButton.addActionListener(e -> safeAddEdge());
        buttonPanel.add(addEdgeButton);

        calculateMSTButton = new JButton("Calculate MST");
        calculateMSTButton.addActionListener(e -> safeCalculateMST());
        buttonPanel.add(calculateMSTButton);

        clearButton = new JButton("Clear Graph");
        clearButton.addActionListener(e -> clearGraph());
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        // Algorithm choice
        gbc.gridy = 3;
        JPanel algoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmChoice = new JComboBox<>(new String[]{"Kruskal's", "Prim's"});
        algoPanel.add(new JLabel("Algorithm:"));
        algoPanel.add(algorithmChoice);
        inputPanel.add(algoPanel, gbc);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void initializeResultPanel() {
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));
        add(scrollPane, BorderLayout.EAST);
    }

    private void calculateMST() {
        String algorithm = (String) algorithmChoice.getSelectedItem();
        if (algorithm.equals("Kruskal's")) {
            mstEdges = computeKruskalMST();
        } else {
            mstEdges = computePrimMST();
        }
        displayMST();
    }

    private List<Edge> computeKruskalMST() {
        List<Edge> mst = new ArrayList<>();
        Collections.sort(edges);

        UnionFind uf = new UnionFind(nodes.keySet());

        for (Edge edge : edges) {
            if (uf.union(edge.from, edge.to)) {
                mst.add(edge);
                if (mst.size() == nodes.size() - 1) break;
            }
        }
        return mst;
    }

    private List<Edge> computePrimMST() {
        List<Edge> mst = new ArrayList<>();
        if (edges.isEmpty()) return mst;

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();
        
        String start = edges.get(0).from;
        visited.add(start);
        
        for (Edge edge : edges) {
            if (edge.from.equals(start) || edge.to.equals(start)) {
                pq.add(edge);
            }
        }

        while (!pq.isEmpty() && mst.size() < nodes.size() - 1) {
            Edge edge = pq.poll();
            String nextNode = visited.contains(edge.from) ? edge.to : edge.from;
            
            if (!visited.contains(nextNode)) {
                mst.add(edge);
                visited.add(nextNode);

                for (Edge e : edges) {
                    if ((e.from.equals(nextNode) && !visited.contains(e.to)) ||
                        (e.to.equals(nextNode) && !visited.contains(e.from))) {
                        pq.add(e);
                    }
                }
            }
        }
        return mst;
    }

    private void safeAddEdge() {
        try {
            String edgeStr = edgeInput.getText().toUpperCase().trim();
            if (!validateEdgeFormat(edgeStr)) {
                throw new IllegalArgumentException("Invalid edge format. Use format 'A-B'");
            }

            String weightStr = weightInput.getText().trim();
            if (!validateWeight(weightStr)) {
                throw new IllegalArgumentException("Weight must be a positive number");
            }

            int weight = Integer.parseInt(weightStr);
            addEdge(edgeStr, weight);
            
            edgeInput.setText("");
            weightInput.setText("");
            
        } catch (Exception e) {
            handleError("Error adding edge: " + e.getMessage());
        }
    }

    private boolean validateEdgeFormat(String edgeStr) {
        return edgeStr.matches("[A-Z]-[A-Z]");
    }

    private boolean validateWeight(String weightStr) {
        try {
            int weight = Integer.parseInt(weightStr);
            return weight > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    } 
    private void addEdge(String edgeStr, int weight) {
        String[] parts = edgeStr.split("-");
        String from = parts[0].trim();
        String to = parts[1].trim();

        // Check for duplicate edges
        for (Edge edge : edges) {
            if ((edge.from.equals(from) && edge.to.equals(to)) ||
                (edge.from.equals(to) && edge.to.equals(from))) {
                throw new IllegalArgumentException("Edge already exists");
            }
        }

        // Add nodes if they don't exist
        if (!nodes.containsKey(from)) {
            nodes.put(from, new Node(
                Math.random() * (graphPanel.getWidth() - 100) + 50,
                Math.random() * (graphPanel.getHeight() - 100) + 50
            ));
        }
        if (!nodes.containsKey(to)) {
            nodes.put(to, new Node(
                Math.random() * (graphPanel.getWidth() - 100) + 50,
                Math.random() * (graphPanel.getHeight() - 100) + 50
            ));
        }

        // Add the edge
        edges.add(new Edge(from, to, weight));
        mstEdges.clear(); // Clear previous MST
        isLayoutStabilized = false;
        layoutTimer.start();
        graphPanel.repaint();
    }

    private void safeCalculateMST() {
        try {
            calculateMST();
            graphPanel.repaint();
        } catch (Exception e) {
            handleError("Error calculating MST: " + e.getMessage());
        }
    }

    private void displayMST() {
        StringBuilder result = new StringBuilder();
        result.append("Minimum Spanning Tree Results:\n\n");
        
        int totalWeight = 0;
        for (Edge edge : mstEdges) {
            result.append(String.format("Edge: %s-%s, Weight: %d\n",
                edge.from, edge.to, edge.weight));
            totalWeight += edge.weight;
        }
        
        result.append("\nTotal MST Weight: ").append(totalWeight);
        resultArea.setText(result.toString());
    }

    private void clearGraph() {
        nodes.clear();
        edges.clear();
        mstEdges.clear();
        resultArea.setText("");
        graphPanel.repaint();
        layoutTimer.stop();
        isLayoutStabilized = false;
    }

    private void handleError(String message) {
        JOptionPane.showMessageDialog(this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MSTApplicationn app = new MSTApplicationn();
                app.setLocationRelativeTo(null);
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}