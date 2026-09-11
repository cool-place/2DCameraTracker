import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("Camera Tracking Simulator");

        window.setSize(900, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        SimulationPanel panel = new SimulationPanel();

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        startButton.addActionListener(e -> {
            panel.startSimulation();
        });

        stopButton.addActionListener(e -> {
            panel.stopSimulation();
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        window.setLayout(new BorderLayout());

        window.add(panel, BorderLayout.CENTER);
        window.add(buttonPanel, BorderLayout.SOUTH);

        window.setVisible(true);
    }
}