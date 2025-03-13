import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EfficiencyFileUpdater {
    public static void updateEfficiencyFile(String filePath, EfficiencyData data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("activeTime=" + data.getActiveTime());
            writer.newLine();
            writer.write("idleTime=" + data.getIdleTime());
            writer.newLine();
            writer.write("overallEfficiency=" + data.getOverallEfficiency());
            writer.newLine();
            writer.write("appUsage=" + data.getAppUsage());
            writer.newLine();
            writer.write("focusTime=" + data.getFocusTime());
            writer.newLine();
            writer.write("breakFrequency=" + data.getBreakFrequency());
            writer.newLine();
            writer.write("breakDuration=" + data.getBreakDuration());
            writer.newLine();
            writer.write("keystrokes=" + data.getKeystrokes());
            writer.newLine();
            writer.write("mouseClicks=" + data.getMouseClicks());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
