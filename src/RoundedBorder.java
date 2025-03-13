import java.awt.*;
import javax.swing.border.Border;

public record RoundedBorder(int radius) implements Border {

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius + 1, radius + 1, radius + 1, radius + 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}
