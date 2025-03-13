import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityGraphPanel extends JPanel {
    private final EfficiencyData efficiencyData;
    private List<Integer> dataPoints;
    private final int pointWidth = 20;    // Horizontal spacing per data point
    private final int leftMargin = 40;    // Space for y-axis labels
    private final int bottomMargin = 30;  // Space for x-axis labels

    public ActivityGraphPanel(EfficiencyData data) {
        this.efficiencyData = data;
        dataPoints = parseGraphPoints(efficiencyData.getGraphPoints());
        setPreferredSize(new Dimension(getGraphWidth(), 150));
    }

    public void updateData() {
        dataPoints = parseGraphPoints(efficiencyData.getGraphPoints());
        setPreferredSize(new Dimension(getGraphWidth(), 150));
        revalidate();
        repaint();
    }

    private List<Integer> parseGraphPoints(String graphStr) {
        List<Integer> list = new ArrayList<>();
        if (graphStr == null || graphStr.isEmpty()) return list;
        String[] parts = graphStr.split(",");
        for (String part : parts) {
            try {
                list.add(Integer.parseInt(part.trim()));
            } catch (NumberFormatException e) {
                list.add(0);
            }
        }
        return list;
    }

    private int getGraphWidth() {
        return Math.max(600, leftMargin + dataPoints.size() * pointWidth);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fill background.
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int graphWidth = getWidth() - leftMargin;
        int graphHeight = getHeight() - bottomMargin;
        int maxVal = dataPoints.stream().max(Integer::compareTo).orElse(1);

        // Draw axes.
        g.setColor(Color.BLACK);
        g.drawLine(leftMargin, 10, leftMargin, 10 + graphHeight); // y-axis
        g.drawLine(leftMargin, 10 + graphHeight, leftMargin + graphWidth, 10 + graphHeight); // x-axis

        // Draw y-axis tick marks and labels.
        int numYTicks = 5;
        for (int i = 0; i <= numYTicks; i++) {
            int y = 10 + graphHeight - (i * graphHeight / numYTicks);
            g.drawLine(leftMargin - 5, y, leftMargin, y);
            int labelVal = maxVal * i / numYTicks;
            g.drawString(Integer.toString(labelVal), 5, y + 5);
        }

        // Draw x-axis tick marks every 5 points.
        for (int i = 0; i < dataPoints.size(); i += 5) {
            int x = leftMargin + i * pointWidth;
            g.drawLine(x, 10 + graphHeight, x, 10 + graphHeight + 5);
            g.drawString(Integer.toString(i * 10), x - 5, 10 + graphHeight + 20);
        }

        // Draw axis labels.
        g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.drawString("Time (seconds)", leftMargin + graphWidth / 2 - 40, getHeight() - 5);
        Graphics2D g2d = (Graphics2D) g;
        Font origFont = g.getFont();
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2d.translate(15, 10 + graphHeight / 2 + 40);
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Event Count", 0, 0);
        g2d.rotate(Math.PI / 2);
        g2d.translate(-15, -(10 + graphHeight / 2 + 40));
        g2d.setFont(origFont);

        // Draw the line graph.
        if (dataPoints.isEmpty()) {
            g.drawString("No data", leftMargin + graphWidth / 2 - 20, 10 + graphHeight / 2);
            return;
        }
        g.setColor(Color.BLUE);
        int prevX = leftMargin;
        int prevY = 10 + graphHeight - (int)(((double) dataPoints.get(0) / maxVal) * graphHeight);
        for (int i = 1; i < dataPoints.size(); i++) {
            int x = leftMargin + i * pointWidth;
            int y = 10 + graphHeight - (int)(((double) dataPoints.get(i) / maxVal) * graphHeight);
            g.drawLine(prevX, prevY, x, y);
            prevX = x;
            prevY = y;
        }
    }
}
