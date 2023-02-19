package turtle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A GUI for displaying a drawing produced by a Turtle.
 */
public class TurtleGUI {

    private DrawPanel drawPanel;
    private BufferedImage image;
    private Turtle turtle;
    private AtomicBoolean drawing;

    private static final int CANVAS_SIZE = 800;
    private static final long ANIMATION_DELAY = 100;
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    public TurtleGUI(Turtle t) {
        this.drawPanel = new DrawPanel();
        this.drawPanel.setBackground(BACKGROUND_COLOR);
        this.turtle = t;
        this.image = new BufferedImage(CANVAS_SIZE,CANVAS_SIZE,BufferedImage.TYPE_INT_ARGB);
        this.drawing = new AtomicBoolean(false);

        JFrame frame = new JFrame("PLU Turtle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this.drawPanel, BorderLayout.CENTER);
        this.drawPanel.setPreferredSize(new Dimension(CANVAS_SIZE,CANVAS_SIZE));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public void draw() {
        if( ! drawing.compareAndSet(false, true) ) return;

        List<LineSegment> lines = turtle.getLines();
        Graphics2D g = (Graphics2D)image.getGraphics();

        AnimationRunnable runnable = new AnimationRunnable(lines, g);
        ExecutorService serv = Executors.newSingleThreadExecutor();
        serv.submit(runnable);
        serv.shutdown();
    }

    private class AnimationRunnable implements Runnable {
        private final List<LineSegment> lines;
        private final Graphics2D g;
        private final double velocity = 150.0;

        AnimationRunnable( List<LineSegment> lines, Graphics2D g ) {
            this.lines = lines;
            this.g = g;
        }

        @Override
        public void run() {
            int center = CANVAS_SIZE / 2;
            Point2D.Double animatedLocation = new Point2D.Double();
            Line2D.Double line = new Line2D.Double();
            for( LineSegment ls : lines ) {
                g.setColor(ls.getPenColor().asColor());
                Point2D.Double start = ls.getStart();
                Point2D.Double end = ls.getEnd();

                // Translate and invert y
                line.setLine(start.x + center,
                        CANVAS_SIZE - (start.y + center),
                        end.x + center,
                        CANVAS_SIZE - (end.y + center));

                g.draw(line);
                drawPanel.repaint();
                try{ Thread.sleep(ANIMATION_DELAY ); }
                catch(InterruptedException ex) { return; }
            }
        }
    }
}
