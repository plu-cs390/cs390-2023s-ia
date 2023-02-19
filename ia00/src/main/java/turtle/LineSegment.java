package turtle;

import java.awt.geom.Point2D;

/**
 * Immutable object that represents a line segment produced by the turtle.
 */
public class LineSegment {

    private Point2D.Double start;
    private Point2D.Double end;
    private PenColor color;

    /**
     * Construct a LineSegment with the given data.
     *
     * @param color the color of the line segment
     * @param st the starting position of the line
     * @param end the end position of the line
     */
    public LineSegment( PenColor color, Point2D.Double st, Point2D.Double end ) {
        this.start = (Point2D.Double)st.clone();
        this.end = (Point2D.Double)end.clone();
        this.color = color;
    }

    public Point2D.Double getStart() { return (Point2D.Double)this.start.clone(); }
    public Point2D.Double getEnd() { return (Point2D.Double)this.end.clone(); }
    public PenColor getPenColor() { return this.color; }
}
