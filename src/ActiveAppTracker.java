import javax.swing.Timer;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

public class ActiveAppTracker {
    private String lastApp = "";
    private final EfficiencyData efficiencyData;

    public ActiveAppTracker(EfficiencyData data) {
        this.efficiencyData = data;
        // Initialize appUsage if it's blank.
        if (efficiencyData.getAppUsage() == null || efficiencyData.getAppUsage().isEmpty()) {
            efficiencyData.setAppUsage("");
        }
    }

    // Start a Swing Timer to check the active app every second.
    public void startTracking() {
        Timer timer = new Timer(1000, e -> checkActiveApp());
        timer.start();
    }

    // Check if a new app is in the foreground.
    private void checkActiveApp() {
        String currentApp = getActiveWindowTitle();
        if (currentApp != null && !currentApp.equals(lastApp)) {
            lastApp = currentApp;
            // Append the new app name to appUsage.
            String currentUsage = efficiencyData.getAppUsage();
            if (currentUsage == null || currentUsage.isEmpty()) {
                efficiencyData.setAppUsage(currentApp);
            } else {
                efficiencyData.setAppUsage(currentUsage + "; " + currentApp);
            }
            // Update the external file.
            String filePath = "efficiency.txt";
            EfficiencyFileUpdater.updateEfficiencyFile(filePath, efficiencyData);
        }
    }

    // Use JNA to get the title of the active window (Windows-specific).
    public String getActiveWindowTitle() {
        char[] buffer = new char[512];
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        if (hwnd == null) return null;
        User32.INSTANCE.GetWindowText(hwnd, buffer, 512);
        return Native.toString(buffer);
    }
}
