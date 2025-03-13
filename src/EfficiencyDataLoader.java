import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EfficiencyDataLoader {
    public static EfficiencyData loadData(String filePath) {
        EfficiencyData data = new EfficiencyData();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "activeTime" -> data.setActiveTime(value);
                        case "idleTime" -> data.setIdleTime(value);
                        case "overallEfficiency" -> data.setOverallEfficiency(value);
                        case "appUsage" -> data.setAppUsage(value);
                        case "breakFrequency" -> data.setBreakFrequency(value);
                        case "breakDuration" -> data.setBreakDuration(value);
                        case "keystrokes" -> data.setKeystrokes(value);
                        case "mouseClicks" -> data.setMouseClicks(value);
                        case "graphPoints" -> data.setGraphPoints(value);
                        default -> System.out.println("Unknown key: " + key);
                    }
                }
            }
        } catch (IOException ignored) { }
        return data;
    }
}
