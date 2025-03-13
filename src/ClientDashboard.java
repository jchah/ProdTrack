import javax.swing.*;
import java.awt.*;

public class ClientDashboard extends JFrame {

    private final EfficiencyData efficiencyData;
    private final JLabel activeIdleLabel;
    private final JLabel efficiencyLabel;
    private final JLabel focusTimeLabel;
    private final JLabel breakLabel;
    private final JLabel activityLabel;
    private final AppUsagePanel appUsagePanel;  // New panel for app usage

    public ClientDashboard(EfficiencyData data) {
        this.efficiencyData = data;

        // Apply UIManager customizations for a CSS-like style.
        UIManager.put("TitledBorder.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("TitledBorder.titleColor", new Color(60, 63, 65));
        UIManager.put("Label.font", new Font("Verdana", Font.PLAIN, 12));
        UIManager.put("Button.font", new Font("Verdana", Font.BOLD, 12));
        UIManager.put("Panel.background", new Color(245, 245, 245));
        UIManager.put("Button.background", new Color(66, 133, 244));
        UIManager.put("Button.foreground", Color.WHITE);

        setTitle("Productivity Monitoring Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create the metrics panel with 2 rows x 3 columns.
        JPanel metricsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        metricsPanel.setBorder(BorderFactory.createTitledBorder("Overall Metrics"));

        // 1. Active vs. Idle Time
        JPanel activeIdlePanel = new JPanel(new BorderLayout());
        activeIdlePanel.setBorder(BorderFactory.createTitledBorder("Active vs Idle Time"));
        activeIdleLabel = new JLabel("", SwingConstants.CENTER);
        activeIdlePanel.add(activeIdleLabel, BorderLayout.CENTER);
        metricsPanel.add(activeIdlePanel);

        // 2. Overall Efficiency
        JPanel efficiencyPanel = new JPanel(new BorderLayout());
        efficiencyPanel.setBorder(BorderFactory.createTitledBorder("Overall Efficiency"));
        efficiencyLabel = new JLabel("", SwingConstants.CENTER);
        efficiencyPanel.add(efficiencyLabel, BorderLayout.CENTER);
        metricsPanel.add(efficiencyPanel);

        // 3. Application Usage – replaced with AppUsagePanel.
        appUsagePanel = new AppUsagePanel(efficiencyData);
        metricsPanel.add(appUsagePanel);

        // 4. Focus Time
        JPanel focusTimePanel = new JPanel(new BorderLayout());
        focusTimePanel.setBorder(BorderFactory.createTitledBorder("Focus Time"));
        focusTimeLabel = new JLabel("", SwingConstants.CENTER);
        focusTimePanel.add(focusTimeLabel, BorderLayout.CENTER);
        metricsPanel.add(focusTimePanel);

        // 5. Break Frequency & Duration
        JPanel breakPanel = new JPanel(new BorderLayout());
        breakPanel.setBorder(BorderFactory.createTitledBorder("Break Frequency & Duration"));
        breakLabel = new JLabel("", SwingConstants.CENTER);
        breakPanel.add(breakLabel, BorderLayout.CENTER);
        metricsPanel.add(breakPanel);

        // 6. Keystroke & Mouse Activity
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Keystroke & Mouse Activity"));
        activityLabel = new JLabel("", SwingConstants.CENTER);
        activityPanel.add(activityLabel, BorderLayout.CENTER);
        metricsPanel.add(activityPanel);

        // Navigation panel with buttons.
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Navigation"));
        JButton detailsButton = new JButton("Detailed Stats");
        detailsButton.addActionListener(e ->
                JOptionPane.showMessageDialog(ClientDashboard.this, "Detailed stats page not implemented."));
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e ->
                JOptionPane.showMessageDialog(ClientDashboard.this, "Settings page not implemented."));
        buttonPanel.add(detailsButton);
        buttonPanel.add(settingsButton);

        // Add panels to the main frame.
        add(metricsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Start a timer to refresh the dashboard UI every second.
        Timer uiTimer = new Timer(1000, e -> updateLabels());
        uiTimer.start();

        setVisible(true);
    }

    // Refresh all displayed metrics.
    private void updateLabels() {
        activeIdleLabel.setText("<html>Active Time: " + efficiencyData.getActiveTime() +
                "<br>Idle Time: " + efficiencyData.getIdleTime() + "</html>");
        efficiencyLabel.setText(efficiencyData.getOverallEfficiency());
        // Refresh the app usage tile.
        appUsagePanel.updateData();
        focusTimeLabel.setText(efficiencyData.getFocusTime());
        breakLabel.setText("<html>Break Frequency: " + efficiencyData.getBreakFrequency() +
                "<br>Break Duration: " + efficiencyData.getBreakDuration() + "</html>");
        activityLabel.setText("<html>Keystrokes: " + efficiencyData.getKeystrokes() +
                "<br>Mouse Clicks: " + efficiencyData.getMouseClicks() + "</html>");
    }
}
