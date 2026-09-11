import javax.swing.JPanel;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.geom.Line2D;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;

// extends JPanel makes class inherit abilities from JPanel to SimulationPanel
public class SimulationPanel extends JPanel {

    private int targetX = 400;
    private int targetY = 150;
    private int targetVelocityX = 1;

    private int movementCounter = 0;
    private int nextMovementChange = 120;

    private ArrayList<Rectangle> obstacles = new ArrayList<>();

    private boolean targetBlocked = false;

    private double cameraAngle = -90; // -90 is y pointed up in swing 90 is vice versa
    private double fieldOfView = 50;
    private double cameraVelocity = 2;
    private double trackingSpeed = 1.5;

    private boolean targetDetected = false;
    private double targetAngle;
    private double angleDifference;

    private double targetDistance;
    private double targetSpeed;

    private Random random = new Random();

    private boolean targetInFOV;

    private boolean simulationRunning = false;

    private int acquisitions = 0;
    private int targetsLost = 0;

    private double trackingTime = 0;

    private boolean wasTracking = false;

    public SimulationPanel() {

        obstacles.add(new Rectangle(100, 280, 180, 20));
        obstacles.add(new Rectangle(350, 330, 220, 20));
        obstacles.add(new Rectangle(650, 270, 140, 20));

        Timer timer = new Timer(16, e -> {

            if (!simulationRunning) {
                return;
            }

            targetX += targetVelocityX;

            movementCounter++;

            if (movementCounter >= nextMovementChange) {

                int newVelocity;

                do {
                    newVelocity = random.nextInt(7) - 3;
                } while (newVelocity == 0);

                targetVelocityX = newVelocity;

                movementCounter = 0;
                nextMovementChange = random.nextInt(126) + 63;
            }

            targetSpeed = Math.abs(targetVelocityX) / 0.016;

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

            Line2D sightLine = new Line2D.Double(
                    cameraX,
                    cameraY,
                    targetCenterX,
                    targetCenterY
            );

            targetBlocked = false;

            for (Rectangle obstacle : obstacles) {
                if (sightLine.intersects(obstacle)) {
                    targetBlocked = true;
                    break;
                }
            }

            int deltaX = targetCenterX - cameraX;
            int deltaY = targetCenterY - cameraY;

            targetDistance = Math.hypot(deltaX, deltaY);

            targetAngle = Math.toDegrees(Math.atan2(deltaY, deltaX));

            angleDifference = Math.abs(targetAngle - cameraAngle);

            targetInFOV = angleDifference <= fieldOfView / 2;

            targetDetected = targetInFOV && !targetBlocked;

            if (targetDetected && !wasTracking) {
                acquisitions++;
            }

            if (!targetDetected && wasTracking) {
                targetsLost++;
            }

            if (targetDetected) {
                trackingTime += 0.016;
            }

            wasTracking = targetDetected;

            if (targetDetected) {

                double trackingDifference = targetAngle - cameraAngle;

                if (Math.abs(trackingDifference) <= trackingSpeed) {
                    cameraAngle = targetAngle;
                } else {
                    cameraAngle += Math.signum(trackingDifference) * trackingSpeed;
                }
            }

            repaint();
        });

        timer.start();
    }

    @Override //override so that we can use my version of paintcomponent
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g.fillOval(targetX, targetY, 20, 20);

        for (Rectangle obstacle : obstacles) {
            g.fillRect(
                    obstacle.x,
                    obstacle.y,
                    obstacle.width,
                    obstacle.height
            );
        }

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
        double rightAngleRadians = Math.toRadians(cameraAngle + halfFOV);
        int leftEndX = cameraX + (int) (Math.cos(leftAngleRadians) * lineLength);
        int leftEndY = cameraY + (int) (Math.sin(leftAngleRadians) * lineLength);
        int rightEndX = cameraX + (int) (Math.cos(rightAngleRadians) * lineLength);
        int rightEndY = cameraY + (int) (Math.sin(rightAngleRadians) * lineLength);

        Polygon cone = new Polygon();
        cone.addPoint(cameraX, cameraY);
        cone.addPoint(leftEndX, leftEndY);
        cone.addPoint(rightEndX, rightEndY);

        if (targetDetected) {
            g2.setColor(new Color(0, 255, 0, 60));
        } else {
            g2.setColor(new Color(255, 0, 0, 60));
        }

        g2.fillPolygon(cone);

        if (targetDetected) {
            g2.setColor(Color.GREEN);
        } else {
            g2.setColor(Color.RED);
        }

        g2.drawLine(cameraX, cameraY, leftEndX, leftEndY);
        g2.drawLine(cameraX, cameraY, rightEndX, rightEndY);
        g2.drawLine(cameraX, cameraY, lineEndX, lineEndY);

        g2.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        if (!simulationRunning) {
            g.drawString("Status: STOPPED", 20, 30);
        } else if (targetDetected) {
            g.drawString("Status: TRACKING", 20, 30);
        } else {
            g.drawString("Status: SCANNING", 20, 30);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 14));

        g.drawString(
                "Camera Angle: " + String.format("%.1f", cameraAngle) + "°",
                20,
                95
        );

        g.drawString(
                "Target Angle: " + String.format("%.1f", targetAngle) + "°",
                20,
                115
        );

        g.drawString("Acquisitions: " + acquisitions, 20, 175);
        g.drawString("Targets Lost: " + targetsLost, 20, 195);

        g.drawString(
                "Tracking Time: " + String.format("%.2f", trackingTime) + " s",
                20,
                215
        );

        if (targetDetected) {
            g.drawString("Distance: " + targetDistance + " px", 20, 135);
        }

        if (targetDetected) {
            g.drawString("Speed: " + targetSpeed + " px/s", 20, 155);
        }

        int targetCenterX = targetX + 10;
        int targetCenterY = targetY + 10;

        if (targetInFOV) {

            if (targetBlocked) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }

            g.drawLine(
                    cameraX,
                    cameraY,
                    targetCenterX,
                    targetCenterY
            );

            g.setColor(Color.BLACK);
        }

    }

    public void randomizeTarget() {

        targetY = random.nextInt(151) + 80; // random.nextInt(x) gives ints from 0 up to x - 1

        targetVelocityX = random.nextInt(3) + 1;

        targetX = 0;
    }

    public void randomizeObstacle() {

        obstacles.clear();

        for (int i = 0; i < 3; i++) {

            int width = random.nextInt(121) + 120;
            int x = random.nextInt(getWidth() - width - 100) + 50;
            int y = random.nextInt(101) + 250;

            obstacles.add(new Rectangle(x, y, width, 20));
        }
    }

    public void startSimulation() {

        acquisitions = 0;
        targetsLost = 0;
        trackingTime = 0;

        targetDetected = false;
        targetBlocked = false;
        wasTracking = false;

        randomizeTarget();
        randomizeObstacle();

        simulationRunning = true;
    }

    public void stopSimulation() {
        simulationRunning = false;
        repaint();
    }
}