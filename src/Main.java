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

        JButton randomizeButton = new JButton("Randomize Target");

        randomizeButton.addActionListener(e -> {
            panel.randomizeTarget();
        });

        JButton obstacleButton = new JButton("Randomize Obstacle");

        obstacleButton.addActionListener(e -> {
            panel.randomizeObstacle();
        });

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(randomizeButton);
        buttonPanel.add(obstacleButton);

        window.setLayout(new BorderLayout());

        window.add(panel, BorderLayout.CENTER);
        window.add(buttonPanel, BorderLayout.SOUTH);

        window.setVisible(true);
    }
}