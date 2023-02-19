package turtle;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A turtle's state includes a 2D position, a heading, and a pen color.  The turtle
 * can be moved forward and turned.  When the turtle moves, it traces a line from the starting
 * position to the ending position using the pen color.  Nothing is actually drawn
 * to the screen however, until draw is called.
 */
public class Turtle {

    /** The turtle's heading is in radians and is measured clockwise from the +y axis. */
    private double heading;

    private Point2D.Double location; // turtle's position
    private List<LineSegment> lines; // list of line segments produced by this turtle
    private PenColor penColor;       // the turtle's current pen color

    // The GUI to display the turtle's drawing
    private TurtleGUI gui = null;

    /**
     * Constructs a turtle with default configuration.  The initial position is (0,0)
     * and initial heading is 0.0.
     */
    public Turtle() {
        lines = new ArrayList<>();
        location = new Point2D.Double();
        reset();
    }

    /**
     * Resets the turtle to (0,0) with heading 0.0.  The history of lines drawn is also cleared.
     */
    public void reset() {
        heading = 0.0;
        location.setLocation(0,0);
        lines.clear();
        penColor = PenColor.BLACK;
    }

    public void setPen( PenColor penColor ) {
        this.penColor = penColor;
    }

    public Point2D.Double getLocation() {
        return new Point2D.Double( location.x, location.y );
    }

    public double getHeading() {
        return Math.toDegrees(heading);
    }

    /**
     * Turns the turtle by the given degrees.
     *
     * @param degrees the change in degrees.  A positive value is clockwise.
     */
    public void turn( double degrees ) {
        // Convert to radians
        double angleRad = Math.toRadians(degrees);
        this.heading += angleRad;
    }

    /**
     * Move the turtle and draw a line using the current pen color.  Lines are stored
     * in memory and don't appear on the screen until draw is called.
     * @param distance the distance to move.
     */
    public void forward(double distance ) {
        double newX = location.x + Math.sin(heading) * distance;
        double newY = location.y + Math.cos(heading) * distance;

        Point2D.Double dest = new Point2D.Double(newX, newY);
        lines.add( new LineSegment(penColor, location, dest) );
        location.setLocation(dest);
    }

    /**
     * Displays a GUI and draws all of the turtle's stored line segments.
     * This method should only be called once after all of the turtle's movements
     * are complete.  Any changes to the turtle after this is called, will not appear
     * in the UI.
     */
    public void draw() {
        // Not thread safe
        if( gui == null ) {
            EventQueue.invokeLater(() -> {
                gui = new TurtleGUI(this);
                gui.draw();
            });
        }
    }

    /**
     * @return the list of line segments drawn by this turtle.
     */
    public List<LineSegment> getLines() {
        return lines;
    }
}
