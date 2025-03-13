import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class AppUsagePanel extends JPanel {
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

    // Refresh the display: show the app with the most time and its usage.
    public void updateData() {
        Map<String, Long> usageMap = parseAppUsage(efficiencyData.getAppUsage());
        if (usageMap.isEmpty()) {
            topAppLabel.setText("No app usage data");
            return;
        }
        // Find the app with the maximum usage.
        Map.Entry<String, Long> topEntry = Collections.max(usageMap.entrySet(),
                Comparator.comparingLong(Map.Entry::getValue));
        String topApp = topEntry.getKey();
        String topTime = formatTime(topEntry.getValue());
        topAppLabel.setText("<html>" + topApp + "<br>" + topTime + "</html>");
    }

    // Displays a dialog with a sorted list of all apps and their usage times.
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

    // Helper: Parse the app usage string into a Map. The expected format is
    // "AppName: hh:mm:ss; AppName2: hh:mm:ss".
    private Map<String, Long> parseAppUsage(String usage) {
        Map<String, Long> map = new HashMap<>();
        if (usage == null || usage.trim().isEmpty()) return map;
        String[] entries = usage.split(";");
        for (String entry : entries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;
            String[] parts = entry.split(":", 2);
            if (parts.length == 2) {
                String appName = parts[0].trim();
                String timeStr = parts[1].trim();
                long timeMs = parseTime(timeStr);
                map.put(appName, timeMs);
            }
        }
        return map;
    }

    // Helper: Convert a time string hh:mm:ss into milliseconds.
    private long parseTime(String timeStr) {
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

    // Helper: Format milliseconds as hh:mm:ss.
    private String formatTime(long ms) {
        long totalSeconds = ms / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    protected abstract void showExpandedDialog();
}
