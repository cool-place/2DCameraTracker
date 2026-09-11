import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Camera Tracking Simulator");

        window.setSize(900, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        SimulationPanel panel = new SimulationPanel();
        window.add(panel);

        window.setVisible(true);
    }
}