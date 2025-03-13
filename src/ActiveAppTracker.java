import javax.swing.Timer;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.util.HashMap;
import java.util.Map;

public class ActiveAppTracker {
    private String lastApp = null;
    private long lastSwitchTime = 0;
    private final EfficiencyData efficiencyData;
    private final Map<String, Long> usageMap;

    public ActiveAppTracker(EfficiencyData data) {
        this.efficiencyData = data;
        // Initialize the usage map from the stored appUsage string.
        usageMap = parseAppUsage(data.getAppUsage());
    }

    // Start a Swing Timer to check the active app every second.
    public void startTracking() {
        Timer timer = new Timer(1000, e -> checkActiveApp());
        timer.start();
    }

    // Check if the active app has changed; update the usage timer accordingly.
    private void checkActiveApp() {
        String currentApp = getActiveWindowTitle().trim();
        // Do not process if the active window title is empty after trimming.
        if (currentApp.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();

        if (lastApp == null) {
            // First time initialization.
            lastApp = currentApp;
            lastSwitchTime = now;
        } else if (!currentApp.equals(lastApp)) {
            // User switched apps – update the previous app's usage if lastApp is not empty after trimming.
            long diff = now - lastSwitchTime;
            if (!lastApp.trim().isEmpty()) {
                usageMap.put(lastApp, usageMap.getOrDefault(lastApp, 0L) + diff);
            }
            lastApp = currentApp;
            lastSwitchTime = now;
        } else {
            // Same app: update its usage continuously.
            long diff = now - lastSwitchTime;
            usageMap.put(currentApp, usageMap.getOrDefault(currentApp, 0L) + diff);
            lastSwitchTime = now;
        }
        // Update the serialized mapping in EfficiencyData.
        efficiencyData.setAppUsage(serializeAppUsage(usageMap));
        String filePath = "efficiency.txt";
        EfficiencyFileUpdater.updateEfficiencyFile(filePath, efficiencyData);
    }

    // Helper: Parse a serialized mapping "AppName: hh:mm:ss; AppName2: hh:mm:ss" into a Map.
    private Map<String, Long> parseAppUsage(String usage) {
        Map<String, Long> map = new HashMap<>();
        if (usage == null || usage.trim().isEmpty()) return map;
        String[] entries = usage.split(";");
        for (String entry : entries) {
            entry = entry.trim();
            if (entry.isEmpty()) continue;
            String[] parts = entry.split(":", 2);
            if (parts.length == 2) {
                String app = parts[0].trim();
                String timeStr = parts[1].trim();
                long ms = parseTime(timeStr);
                map.put(app, ms);
            }
        }
        return map;
    }

    // Helper: Serialize the Map into a string "AppName: hh:mm:ss; AppName2: hh:mm:ss".
    private String serializeAppUsage(Map<String, Long> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(formatTime(entry.getValue()))
                    .append("; ");
        }
        return sb.toString().trim();
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

    // Use JNA to get the title of the active window (Windows-specific).
    public String getActiveWindowTitle() {
        char[] buffer = new char[512];
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        if (hwnd == null) return "";
        User32.INSTANCE.GetWindowText(hwnd, buffer, 512);
        return Native.toString(buffer);
    }
}
