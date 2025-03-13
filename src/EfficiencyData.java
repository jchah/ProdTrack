public class EfficiencyData {
    private String activeTime;
    private String idleTime;
    private String overallEfficiency;
    private String appUsage;         // Detailed usage (e.g., "Word - Document1: 00:00:50; Word - Document2: 00:00:40; ...")
    private String breakFrequency;
    private String breakDuration;
    private String keystrokes;
    private String mouseClicks;
    private String graphPoints;      // For the activity graph (comma-separated per-minute values)

    // Getters and Setters
    public String getActiveTime() {
        return activeTime;
    }
    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getIdleTime() {
        return idleTime;
    }
    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
    }

    public String getOverallEfficiency() {
        return overallEfficiency;
    }
    public void setOverallEfficiency(String overallEfficiency) {
        this.overallEfficiency = overallEfficiency;
    }

    public String getAppUsage() {
        return appUsage;
    }
    public void setAppUsage(String appUsage) {
        this.appUsage = appUsage;
    }

    public String getBreakFrequency() {
        return breakFrequency;
    }
    public void setBreakFrequency(String breakFrequency) {
        this.breakFrequency = breakFrequency;
    }

    public String getBreakDuration() {
        return breakDuration;
    }
    public void setBreakDuration(String breakDuration) {
        this.breakDuration = breakDuration;
    }

    public String getKeystrokes() {
        return keystrokes;
    }
    public void setKeystrokes(String keystrokes) {
        this.keystrokes = keystrokes;
    }

    public String getMouseClicks() {
        return mouseClicks;
    }
    public void setMouseClicks(String mouseClicks) {
        this.mouseClicks = mouseClicks;
    }

    public String getGraphPoints() {
        return graphPoints;
    }
    public void setGraphPoints(String graphPoints) {
        this.graphPoints = graphPoints;
    }
}
