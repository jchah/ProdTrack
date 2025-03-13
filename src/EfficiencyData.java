public class EfficiencyData {
    private String activeTime;
    private String idleTime;
    private String overallEfficiency;
    private String appUsage;         // e.g., "Chrome: 03:00:00; IntelliJ: 02:00:00; Slack: 01:00:00"
    private String focusTime;
    private String breakFrequency;   // Here treated as a time duration (hh:mm:ss) for consistency
    private String breakDuration;
    private String keystrokes;       // Also expressed as a time duration (hh:mm:ss)
    private String mouseClicks;      // Expressed as a time duration (hh:mm:ss)

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
    public String getFocusTime() {
        return focusTime;
    }
    public void setFocusTime(String focusTime) {
        this.focusTime = focusTime;
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
}
