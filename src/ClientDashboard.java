import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ClientDashboard extends JFrame {

    private final EfficiencyData efficiencyData;
    private final JLabel activeIdleLabel;
    private final JLabel efficiencyLabel;
    private final JLabel breakLabel;
    private final JLabel activityLabel;
    private final AppUsagePanel appUsagePanel;
    private final ActivityGraphPanel activityGraphPanel;  // Graph panel for activity data

    public ClientDashboard(EfficiencyData data) {
        this.efficiencyData = data;

        // Setup modern UI styles.
        UIManager.put("TitledBorder.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TitledBorder.titleColor", new Color(45, 45, 45));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Panel.background", new Color(250, 250, 250));
        UIManager.put("Button.background", new Color(66, 133, 244));
        UIManager.put("Button.foreground", Color.WHITE);

        setTitle("Productivity Monitoring Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel (North) with icon, title, and welcome message.
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(66, 133, 244));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("icon.png")));
            iconLabel.setIcon(icon);
        } catch (Exception e) {
            iconLabel.setText("");
        }
        headerPanel.add(iconLabel, BorderLayout.WEST);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Productivity Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        JLabel welcomeLabel = new JLabel("Welcome, User!", SwingConstants.RIGHT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);
        titlePanel.add(welcomeLabel, BorderLayout.SOUTH);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Navigation Panel (West) with a button to show On-Task Apps and additional nav buttons.
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(250, 250, 250));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton showOnTaskAppsButton = new JButton("On-Task Apps");
        showOnTaskAppsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showOnTaskAppsButton.setMaximumSize(new Dimension(150, 40));
        showOnTaskAppsButton.addActionListener(e -> OnTaskAppsPanel.showOnTaskAppsDialog(this));
        navPanel.add(showOnTaskAppsButton);
        navPanel.add(Box.createVerticalStrut(20));

        navPanel.add(createNavButton("Detailed Stats"));
        navPanel.add(Box.createVerticalStrut(20));
        navPanel.add(createNavButton("Settings"));
        navPanel.add(Box.createVerticalGlue());
        add(navPanel, BorderLayout.WEST);

        // Main Metrics Panel (Center) with 2 rows x 3 columns.
        JPanel metricsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        metricsPanel.setOpaque(false);
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel activeIdlePanel = createStyledPanel("Active vs Idle Time");
        activeIdleLabel = new JLabel("", SwingConstants.CENTER);
        activeIdleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        activeIdlePanel.add(activeIdleLabel, BorderLayout.CENTER);
        metricsPanel.add(activeIdlePanel);

        JPanel efficiencyPanel = createStyledPanel("Overall Efficiency");
        efficiencyLabel = new JLabel("", SwingConstants.CENTER);
        efficiencyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        efficiencyPanel.add(efficiencyLabel, BorderLayout.CENTER);
        metricsPanel.add(efficiencyPanel);

        appUsagePanel = new AppUsagePanel(efficiencyData) {
            protected void showExpandedDialog() {
                showAppUsageDialog();
            }
        };
        appUsagePanel.setBorder(BorderFactory.createTitledBorder("Application Usage"));
        metricsPanel.add(appUsagePanel);

        activityGraphPanel = new ActivityGraphPanel(efficiencyData);
        JScrollPane graphScrollPane = new JScrollPane(activityGraphPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        graphScrollPane.setBorder(BorderFactory.createTitledBorder("Activity Graph"));
        metricsPanel.add(graphScrollPane);

        JPanel breakPanel = createStyledPanel("Break Frequency & Duration");
        breakLabel = new JLabel("", SwingConstants.CENTER);
        breakLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        breakPanel.add(breakLabel, BorderLayout.CENTER);
        metricsPanel.add(breakPanel);

        JPanel activityPanel = createStyledPanel("Keystroke & Mouse Activity");
        activityLabel = new JLabel("", SwingConstants.CENTER);
        activityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        activityPanel.add(activityLabel, BorderLayout.CENTER);
        metricsPanel.add(activityPanel);

        add(metricsPanel, BorderLayout.CENTER);

        // Timer to refresh the dashboard UI every second.
        Timer uiTimer = new Timer(1000, e -> updateLabels());
        uiTimer.start();

        setVisible(true);
    }

    // Helper method to create a styled rounded panel.
    private JPanel createStyledPanel(String title) {
        RoundedPanel panel = new RoundedPanel(10);
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(new RoundedBorder(10), title));
        return panel;
    }

    // Helper method to create a navigation button.
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(150, 40));
        button.addActionListener(e ->
                JOptionPane.showMessageDialog(ClientDashboard.this, text + " page not implemented."));
        return button;
    }

    // This method shows an expanded Application Usage dialog.
    // It reads detailed usage from efficiencyData.getAppUsage() and displays it in a list.
    private void showAppUsageDialog() {
        JDialog dialog = new JDialog(this, "Application Usage", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> usageList = new JList<>(listModel);
        usageList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(usageList);
        scrollPane.setPreferredSize(new Dimension(350, 250));

        // Populate the list from the detailed app usage stored in efficiencyData.
        String usageStr = efficiencyData.getAppUsage();
        if (usageStr == null || usageStr.trim().isEmpty()) {
            listModel.addElement("No apps tracked");
        } else {
            String[] entries = usageStr.split(";");
            for (String entry : entries) {
                entry = entry.trim();
                if (!entry.isEmpty()) {
                    listModel.addElement(entry);
                }
            }
        }

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // Refresh all displayed metrics.
    void updateLabels() {
        activeIdleLabel.setText("<html>Active Time: " + efficiencyData.getActiveTime() +
                "<br>Idle Time: " + efficiencyData.getIdleTime() + "</html>");
        efficiencyLabel.setText(efficiencyData.getOverallEfficiency());
        appUsagePanel.updateData();
        activityGraphPanel.updateData();
        breakLabel.setText("<html>Break Frequency: " + efficiencyData.getBreakFrequency() +
                "<br>Break Duration: " + efficiencyData.getBreakDuration() + "</html>");
        activityLabel.setText("<html>Keystrokes: " + efficiencyData.getKeystrokes() +
                "<br>Mouse Clicks: " + efficiencyData.getMouseClicks() + "</html>");
    }
}
