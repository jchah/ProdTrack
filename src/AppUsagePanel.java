import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AppUsagePanel extends JPanel {
    private final JLabel topAppLabel;
    private final EfficiencyData efficiencyData;

    public AppUsagePanel(EfficiencyData data) {
        this.efficiencyData = data;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Application Usage"));

        topAppLabel = new JLabel("", SwingConstants.CENTER);
        topAppLabel.setFont(new Font("Verdana", Font.BOLD, 12));

        JButton expandButton = new JButton("Expand All");
        expandButton.addActionListener(e -> showAllApps());

        add(topAppLabel, BorderLayout.CENTER);
        add(expandButton, BorderLayout.SOUTH);

        updateData();
    }

    // Call this method to refresh the tile with the latest usage data.
    public void updateData() {
        // Parse the current appUsage string into a map.
        Map<String, Long> usageMap = parseAppUsage(efficiencyData.getAppUsage());
        if (usageMap.isEmpty()) {
            topAppLabel.setText("No app usage data");
            return;
        }

        // Sort entries descending by accumulated time.
        List<Map.Entry<String, Long>> sortedList = new ArrayList<>(usageMap.entrySet());
        sortedList.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        Map.Entry<String, Long> topEntry = sortedList.get(0);
        String topApp = topEntry.getKey();
        String topTime = formatTime(topEntry.getValue());
        topAppLabel.setText("<html>" + topApp + "<br>" + topTime + "</html>");
    }

    // Displays a dialog with a sorted list of all app usage.
    private void showAllApps() {
        Map<String, Long> usageMap = parseAppUsage(efficiencyData.getAppUsage());
        if (usageMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No app usage data available.");
            return;
        }

        List<Map.Entry<String, Long>> sortedList = new ArrayList<>(usageMap.entrySet());
        sortedList.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> entry : sortedList) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(formatTime(entry.getValue()))
                    .append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Verdana", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        JOptionPane.showMessageDialog(this, scrollPane, "All App Usage", JOptionPane.INFORMATION_MESSAGE);
    }

    // Parses a string in the format "AppName: hh:mm:ss; AppName2: hh:mm:ss" into a map.
    private Map<String, Long> parseAppUsage(String usage) {
        Map<String, Long> map = new HashMap<>();
        if (usage == null || usage.trim().isEmpty()) return map;
        String[] entries = usage.split(";");
        for (String entry : entries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;
            String[] parts = entry.split("\\s*:\\s*", 2);
            if (parts.length == 2) {
                String appName = parts[0].trim();
                long timeMs = parseTime(parts[1].trim());
                map.put(appName, timeMs);
            }
        }
        return map;
    }

    // Converts a time string hh:mm:ss into milliseconds.
    private long parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) return 0;
        String[] parts = timeStr.split(":");
        if (parts.length != 3) return 0;
        try {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);
            return hours * 3600000L + minutes * 60000L + seconds * 1000L;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Formats milliseconds as hh:mm:ss.
    private String formatTime(long ms) {
        long seconds = ms / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
