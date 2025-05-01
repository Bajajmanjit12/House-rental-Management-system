import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard {
    JFrame jf;
    JTable table;
    DefaultTableModel model;
    JLabel totalRentals, activeRentals, cancelledRentals;

    public AdminDashboard() {
        jf = new JFrame("Admin Dashboard");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(900, 650);
        jf.setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel("/mnt/data/704a80a9-2b94-4a0d-902a-60aa90825559.png");
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("HOUSE RENTAL - ADMIN DASHBOARD", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.DARK_GRAY);
        backgroundPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // Metrics Panel
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        metricsPanel.setOpaque(false);

        totalRentals = createMetricLabel("Total Rentals: 0");
        activeRentals = createMetricLabel("Active: 0");
        cancelledRentals = createMetricLabel("Cancelled: 0");

        metricsPanel.add(totalRentals);
        metricsPanel.add(activeRentals);
        metricsPanel.add(cancelledRentals);

        centerPanel.add(metricsPanel, BorderLayout.NORTH);

        // Search bar
        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setToolTipText("Search by tenant, house or status...");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);

        centerPanel.add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        String[] columns = {"Tenant Name", "House", "Status", "From Date", "To Date / Visited Date"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(100, 149, 237));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);

        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");

        styleButton(refreshButton, new Color(0, 123, 255));
        styleButton(logoutButton, new Color(220, 53, 69));

        refreshButton.addActionListener(e -> fetchData());
        logoutButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
        jf.setContentPane(backgroundPanel);
        jf.setVisible(true);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }

            private void filterTable() {
                String keyword = searchField.getText().trim().toLowerCase();
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                table.setRowSorter(sorter);
                sorter.setRowFilter(keyword.isEmpty() ? null : RowFilter.regexFilter("(?i)" + keyword));
            }
        });

        fetchData();
        new Timer(15000, e -> fetchData()).start();
    }

    private void fetchData() {
        try {
            model.setRowCount(0);
            int total = 0, active = 0, cancelled = 0;

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/house_rental", "root", "your_password");
            Statement stmt = conn.createStatement();

            String query = "SELECT u.name AS tenant_name, h.title AS house_title, " +
                    "r.status, r.rent_date AS from_date, r.visit_date AS to_date " +
                    "FROM rentals r JOIN users u ON r.user_id = u.id " +
                    "JOIN houses h ON r.house_id = h.id";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String status = rs.getString("status");
                total++;
                if ("active".equalsIgnoreCase(status)) active++;
                if ("cancelled".equalsIgnoreCase(status)) cancelled++;

                model.addRow(new Object[]{
                        rs.getString("tenant_name"),
                        rs.getString("house_title"),
                        status,
                        rs.getDate("from_date"),
                        rs.getDate("to_date")
                });
            }
            conn.close();

            totalRentals.setText("Total Rentals: " + total);
            activeRentals.setText("Active: " + active);
            cancelledRentals.setText("Cancelled: " + cancelled);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(jf, "Connection Error: " + e.getMessage());
        }
    }

    private JLabel createMetricLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.DARK_GRAY);
        label.setOpaque(true);
        label.setBackground(new Color(240, 240, 240));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return label;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(110, 35));
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found.");
        }
        SwingUtilities.invokeLater(AdminDashboard::new);
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String filePath) {
        try {
            backgroundImage = new ImageIcon(filePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}