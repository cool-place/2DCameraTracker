import javax.swing.JPanel;
import java.awt.Graphics;
import javax.swing.Timer;
// extends JPanel makes class inherit abilities from JPanel to SimulationPanel
public class SimulationPanel extends JPanel {

    private int targetX = 400;
    private int targetY = 150;
    private int targetVelocityX = 1;

    private double cameraAngle = -90; // -90 is y pointed up in swing 90 is vice versa
    private double fieldOfView = 50;
    private double cameraVelocity = 0.5;

    public SimulationPanel() {

        Timer timer = new Timer(16, e -> {

            targetX += targetVelocityX;

            if (targetX <= 0 || targetX + 20 >= getWidth()) {
                targetVelocityX *= -1;
            }

            cameraAngle += cameraVelocity;

            if (cameraAngle >= -30 || cameraAngle <= -150) {
                cameraVelocity *= -1;
            }

            repaint();
        });

        timer.start();
    }

    @Override //override so that we can use my version of paintcomponent
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillOval(targetX,targetY,20,20);

        int cameraX = getWidth() / 2;
        int cameraY = getHeight() - 50;
        // x and y start in the top left corner of x, hence minus half of width to x
        g.fillRect(cameraX - 20, cameraY, 40, 20);
        g.fillOval(cameraX - 6, cameraY - 10, 12, 12);

        int lineLength = 200;

        double angleRadians = Math.toRadians(cameraAngle);

        int lineEndX = cameraX + (int) (Math.cos(angleRadians) * lineLength);
        int lineEndY = cameraY + (int) (Math.sin(angleRadians) * lineLength);

        double halfFOV = fieldOfView / 2;
        double leftAngleRadians = Math.toRadians(cameraAngle - halfFOV);
        double rightAngleRadians = Math.toRadians(cameraAngle +halfFOV);
        int leftEndX = cameraX + (int) (Math.cos(leftAngleRadians) * lineLength);
        int leftEndY = cameraY + (int) (Math.sin(leftAngleRadians) * lineLength);
        int rightEndX = cameraX + (int) (Math.cos(rightAngleRadians) * lineLength);
        int rightEndY = cameraY + (int) (Math.sin(rightAngleRadians) * lineLength);

        g.drawLine(cameraX, cameraY, leftEndX, leftEndY);
        g.drawLine(cameraX, cameraY, rightEndX, rightEndY);
        g.drawLine(cameraX, cameraY, lineEndX, lineEndY);
    }
}
