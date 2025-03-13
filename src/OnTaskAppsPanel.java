import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OnTaskAppsPanel extends JPanel {

    // Constructor if you want to use it as a panel in your UI.
    public OnTaskAppsPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("On-Task Apps"));
    }

    // Static method to show a dialog containing the on-task apps list,
    // which is read from on-task-apps.txt.
    public static void showOnTaskAppsDialog(Component parent) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        try (BufferedReader br = new BufferedReader(new FileReader("on-task-apps.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    listModel.addElement(line.trim());
                }
            }
        } catch (IOException ex) {
            listModel.addElement("Error reading on-task apps.");
        }
        if (listModel.isEmpty()) {
            listModel.addElement("No apps tracked");
        }
        JList<String> appList = new JList<>(listModel);
        appList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(appList);
        scrollPane.setPreferredSize(new Dimension(250, 200));

        JOptionPane.showMessageDialog(parent, scrollPane, "On-Task Apps", JOptionPane.INFORMATION_MESSAGE);
    }
}
