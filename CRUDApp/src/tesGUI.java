
import javax.swing.*;

public class testGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test GUI");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Hello, this is a test GUI!"));

        frame.add(panel);
        frame.setVisible(true);
    }
}
