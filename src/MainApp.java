import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MainApp {
    public static void main(String[] args) {
        // Load existing efficiency data from file.
        EfficiencyData data = EfficiencyDataLoader.loadData("efficiency.txt");
        if (data.getActiveTime() == null) {
            data.setActiveTime("00:00:00");
        }
        if (data.getIdleTime() == null) {
            data.setIdleTime("00:00:00");
        }
        if (data.getOverallEfficiency() == null) {
            data.setOverallEfficiency("0%");
        }
        if (data.getAppUsage() == null) {
            data.setAppUsage("");
        }
        if (data.getBreakFrequency() == null) {
            data.setBreakFrequency("0");
        }
        if (data.getBreakDuration() == null) {
            data.setBreakDuration("00:00:00");
        }
        if (data.getKeystrokes() == null) {
            data.setKeystrokes("0");
        }
        if (data.getMouseClicks() == null) {
            data.setMouseClicks("0");
        }
        if (data.getGraphPoints() == null) {
            data.setGraphPoints(""); // Start with an empty series.
        }

        // Start local activity tracking (active/idle time).
        new ActivityTracker(data);

        // Read initial counts from the file so that they persist.
        long initialKeystrokes = 0;
        long initialMouseClicks = 0;
        try {
            initialKeystrokes = Long.parseLong(data.getKeystrokes());
        } catch (NumberFormatException ignored) { }
        try {
            initialMouseClicks = Long.parseLong(data.getMouseClicks());
        } catch (NumberFormatException ignored) { }

        // Start global activity tracking for keystrokes and mouse clicks.
        GlobalActivityTracker globalTracker = new GlobalActivityTracker();

        // Timer to update global counts into EfficiencyData every second.
        long finalInitialKeystrokes = initialKeystrokes;
        long finalInitialMouseClicks = initialMouseClicks;
        Timer globalUpdateTimer = new Timer(1000, e -> {
            long newKeystrokes = finalInitialKeystrokes + globalTracker.getKeystrokeCount();
            long newMouseClicks = finalInitialMouseClicks + globalTracker.getMouseClickCount();
            data.setKeystrokes(Long.toString(newKeystrokes));
            data.setMouseClicks(Long.toString(newMouseClicks));
            EfficiencyFileUpdater.updateEfficiencyFile("efficiency.txt", data);
        });
        globalUpdateTimer.start();

        // --- New: Timer to update the activity graph every 10 seconds ---
        final long[] previousCombinedCount = {globalTracker.getKeystrokeCount() + globalTracker.getMouseClickCount()};
        Timer graphTimer = new Timer(10000, e -> {  // 10,000 ms = 10 seconds
            long currentCombined = globalTracker.getKeystrokeCount() + globalTracker.getMouseClickCount();
            long diff = currentCombined - previousCombinedCount[0];
            previousCombinedCount[0] = currentCombined;

            // Append the new diff value to the graphPoints series (even if diff is 0).
            String currentSeries = data.getGraphPoints();
            if (currentSeries == null || currentSeries.isEmpty()) {
                data.setGraphPoints(Long.toString(diff));
            } else {
                data.setGraphPoints(currentSeries + "," + diff);
            }
            EfficiencyFileUpdater.updateEfficiencyFile("efficiency.txt", data);
        });
        graphTimer.start();


        // Start tracking the active application.
        ActiveAppTracker activeAppTracker = new ActiveAppTracker(data);
        activeAppTracker.startTracking();

        // Launch the dashboard UI with the loaded EfficiencyData.
        SwingUtilities.invokeLater(() -> new ClientDashboard(data));

        // Shutdown global hook on exit.
        Runtime.getRuntime().addShutdownHook(new Thread(globalTracker::shutdown));
    }
}
