import javax.swing.JPanel;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.geom.Line2D;
// extends JPanel makes class inherit abilities from JPanel to SimulationPanel
public class SimulationPanel extends JPanel {

    private int targetX = 400;
    private int targetY = 150;
    private int targetVelocityX = 1;

    private int wallY = 300;
    private int wallWidth = 300;
    private int wallHeight = 20;

    private boolean targetBlocked = false;

    private double cameraAngle = -90; // -90 is y pointed up in swing 90 is vice versa
    private double fieldOfView = 50;
    private double cameraVelocity = 0.5;

    private boolean targetDetected = false;
    private double targetAngle;
    private double angleDifference;

    public SimulationPanel() {

        Timer timer = new Timer(16, e -> {

            targetX += targetVelocityX;

            if (targetX <= 0 || targetX + 20 >= getWidth()) {
                targetVelocityX *= -1;
            }

            if (!targetDetected) {
                cameraAngle += cameraVelocity;

                if (cameraAngle >= -30 || cameraAngle <= -150) {
                    cameraVelocity *= -1;
                }
            }

            int cameraX = getWidth() / 2;
            int cameraY = getHeight() - 50;

            int targetCenterX = targetX + 10;
            int targetCenterY = targetY + 10;

            int wallX = (getWidth() - wallWidth) / 2;

            Line2D sightLine = new Line2D.Double(
                    cameraX,
                    cameraY,
                    targetCenterX,
                    targetCenterY
            );

            targetBlocked = sightLine.intersects(
                    wallX,
                    wallY,
                    wallWidth,
                    wallHeight
            );

            int deltaX = targetCenterX - cameraX;
            int deltaY = targetCenterY - cameraY;

            targetAngle = Math.toDegrees(Math.atan2(deltaY, deltaX));

            angleDifference = Math.abs(targetAngle - cameraAngle);

            targetDetected = angleDifference <= fieldOfView / 2 && !targetBlocked;

            if (targetDetected) {
                cameraAngle = targetAngle;
            }

            repaint();
        });

        timer.start();
    }

    @Override //override so that we can use my version of paintcomponent
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillOval(targetX,targetY,20,20);

        int wallX = (getWidth() - wallWidth) / 2;
        g.fillRect(wallX, wallY, wallWidth, wallHeight);

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

        if (targetDetected) {
            g.drawString("Status: TARGET DETECTED", 20, 30);
        } else {
            g.drawString("Status: SCANNING", 20, 30);
        }
        g.drawString("Camera angle: " + cameraAngle, 20, 50);
        g.drawString("Target angle: " + targetAngle, 20, 70);
        g.drawString("Difference: " + angleDifference, 20, 90);

        if (targetBlocked) {
            g.drawString("Line of sight: BLOCKED", 20, 110);
        } else {
            g.drawString("Line of sight: CLEAR", 20, 110);
        }
    }
}
