import java.awt.AWTEvent;
import java.awt.Toolkit;
import javax.swing.Timer;

public class ActivityTracker implements java.awt.event.AWTEventListener {

    private long lastActivityTime;
    private long activeTime; // in milliseconds
    private long idleTime;   // in milliseconds
    private final long IDLE_THRESHOLD = 30000; // 30 seconds threshold

    private final EfficiencyData efficiencyData;

    public ActivityTracker(EfficiencyData data) {
        this.efficiencyData = data;
        // Initialize active and idle times from the file.
        this.activeTime = parseTime(efficiencyData.getActiveTime());
        this.idleTime = parseTime(efficiencyData.getIdleTime());
        lastActivityTime = System.currentTimeMillis();

        // Register for local key, mouse, and mouse motion events.
        Toolkit.getDefaultToolkit().addAWTEventListener(this,
                AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

        Timer timer = new Timer(1000, e -> updateTimes());
        timer.start();
    }

    private void updateTimes() {
        long currentTime = System.currentTimeMillis();
        long elapsedSinceLastActivity = currentTime - lastActivityTime;
        if (elapsedSinceLastActivity > IDLE_THRESHOLD) {
            idleTime += 1000;
        } else {
            activeTime += 1000;
        }
        efficiencyData.setActiveTime(formatTime(activeTime));
        efficiencyData.setIdleTime(formatTime(idleTime));

        String filePath = "efficiency.txt";
        EfficiencyFileUpdater.updateEfficiencyFile(filePath, efficiencyData);
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        lastActivityTime = System.currentTimeMillis();
    }

    public static String formatTime(long ms) {
        long seconds = ms / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    public static long parseTime(String timeStr) {
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
}
