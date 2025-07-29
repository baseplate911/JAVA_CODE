import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MSTApplication extends JFrame {
    private JPanel graphPanel;
    private JTextField edgeInput;
    private JTextField weightInput;
    private JButton addEdgeButton;
    private JButton calculateMSTButton;
    private JComboBox<String> algorithmChoice;
    private JTextArea resultArea;
    
    private Map<String, Point> nodes = new HashMap<>();
    private List<Edge> edges = new ArrayList<>();
    private List<Edge> mstEdges = new ArrayList<>();
    private char nextNodeName = 'A';

    public MSTApplication() {
        setTitle("Kruskal's and Prim's Algorithm - MST Calculator");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g);
            }
        };
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setPreferredSize(new Dimension(600, 500));
        add(graphPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Edge (e.g., A-B):"));
        edgeInput = new JTextField();
        inputPanel.add(edgeInput);

        inputPanel.add(new JLabel("Weight:"));
        weightInput = new JTextField();
        inputPanel.add(weightInput);

        addEdgeButton = new JButton("Add Edge");
        addEdgeButton.addActionListener(e -> addEdge());
        inputPanel.add(addEdgeButton);

        calculateMSTButton = new JButton("Calculate MST");
        calculateMSTButton.addActionListener(e -> calculateMST());
        inputPanel.add(calculateMSTButton);

        add(inputPanel, BorderLayout.NORTH);

        JPanel choicePanel = new JPanel();
        algorithmChoice = new JComboBox<>(new String[]{"Kruskal's", "Prim's"});
        choicePanel.add(new JLabel("Choose Algorithm:"));
        choicePanel.add(algorithmChoice);
        add(choicePanel, BorderLayout.SOUTH);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.EAST);
    }

    private void drawGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2));

        // Draw edges
        for (Edge edge : edges) {
            Point p1 = nodes.get(edge.from);
            Point p2 = nodes.get(edge.to);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            
            // Draw weight
            int midX = (p1.x + p2.x) / 2;
            int midY = (p1.y + p2.y) / 2;
            g2d.setColor(Color.BLUE);
            g2d.fillRect(midX - 10, midY - 15, 20, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(edge.weight), midX - 5, midY);
        }
        
        // Draw MST edges
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        for (Edge edge : mstEdges) {
            Point p1 = nodes.get(edge.from);
            Point p2 = nodes.get(edge.to);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        // Draw nodes
        g2d.setStroke(new BasicStroke(2));
        for (Map.Entry<String, Point> entry : nodes.entrySet()) {
            Point point = entry.getValue();
            g2d.setColor(Color.WHITE);
            g2d.fillOval(point.x - 15, point.y - 15, 30, 30);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(point.x - 15, point.y - 15, 30, 30);
            g2d.drawString(entry.getKey(), point.x - 5, point.y + 5);
        }
    }

    private void addEdge() {
        try {
            String edgeStr = edgeInput.getText().toUpperCase();
            int weight = Integer.parseInt(weightInput.getText());

            String[] parts = edgeStr.split("-");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                // Validate node names
                if (!isValidNodeName(from) || !isValidNodeName(to)) {
                    JOptionPane.showMessageDialog(this, "Please use letters A-Z for node names");
                    return;
                }

                // Create nodes if they don't exist
                if (!nodes.containsKey(from)) {
                    nodes.put(from, calculateNodePosition(nodes.size()));
                    nextNodeName = (char) (Math.max(nextNodeName, from.charAt(0) + 1));
                }
                if (!nodes.containsKey(to)) {
                    nodes.put(to, calculateNodePosition(nodes.size()));
                    nextNodeName = (char) (Math.max(nextNodeName, to.charAt(0) + 1));
                }

                // Add the edge
                edges.add(new Edge(from, to, weight));
                edgeInput.setText("");
                weightInput.setText("");
                graphPanel.repaint();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid weight");
        }
    }

    private boolean isValidNodeName(String name) {
        return name.length() == 1 && Character.isLetter(name.charAt(0));
    }

    private Point calculateNodePosition(int nodeCount) {
        int centerX = graphPanel.getWidth() / 2;
        int centerY = graphPanel.getHeight() / 2;
        int radius = Math.min(graphPanel.getWidth(), graphPanel.getHeight()) / 3;
        
        // Calculate position on a circle to prevent overlapping
        double angle = (2 * Math.PI * nodeCount) / Math.max(1, nodes.size() + 1);
        int x = centerX + (int)(radius * Math.cos(angle));
        int y = centerY + (int)(radius * Math.sin(angle));
        
        return new Point(x, y);
    }

    private void calculateMST() {
        if (edges.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add some edges first");
            return;
        }

        String algorithm = (String) algorithmChoice.getSelectedItem();
        if (algorithm.equals("Kruskal's")) {
            mstEdges = computeKruskalMST();
        } else {
            mstEdges = computePrimMST();
        }
        displayMST();
        graphPanel.repaint();
    }

    private List<Edge> computeKruskalMST() {
        List<Edge> mst = new ArrayList<>();
        List<Edge> sortedEdges = new ArrayList<>(edges);
        Collections.sort(sortedEdges);

        UnionFind uf = new UnionFind(nodes.keySet());

        for (Edge edge : sortedEdges) {
            if (uf.union(edge.from, edge.to)) {
                mst.add(edge);
                if (mst.size() == nodes.size() - 1) break;
            }
        }
        return mst;
    }

    private List<Edge> computePrimMST() {
        List<Edge> mst = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        Set<String> visited = new HashSet<>();
        
        String start = nodes.keySet().iterator().next();
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

    private void displayMST() {
        resultArea.setText("Minimum Spanning Tree:\n");
        int totalWeight = 0;
        for (Edge edge : mstEdges) {
            resultArea.append(edge.from + " - " + edge.to + " : " + edge.weight + "\n");
            totalWeight += edge.weight;
        }
        resultArea.append("\nTotal Weight: " + totalWeight);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MSTApplication frame = new MSTApplication();
            frame.setVisible(true);
        });
    }
}