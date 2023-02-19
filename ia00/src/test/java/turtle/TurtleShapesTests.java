package turtle;

import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurtleShapesTests {

    @Test
    public void chordLengthNegativeRadius() {
        assertThrows(IllegalArgumentException.class, () -> TurtleShapes.chordLength(-1.0, 0.0) );
    }

    @Test
    public void chordLengthNegativeAngle() {
        assertThrows(IllegalArgumentException.class, () -> TurtleShapes.chordLength(1.0, -1.0) );
    }

    @Test
    public void chordLengthAngleTooLarge() {
        assertThrows( IllegalArgumentException.class, () -> TurtleShapes.chordLength(1.0, 181.0) );
    }

    @Test
    public void chordLengthSmallAngle() {
        double result = TurtleShapes.chordLength(10.0, 1.0);
        assertEquals( 0.1745307, result, 0.00001 );
    }

    @Test
    public void chordLengthLargeAngle() {
        double result = TurtleShapes.chordLength(10.0, 179.0);
        assertEquals( 19.999238, result, 0.00001 );
    }

    @Test
    public void chordLengthMidAngle() {
        double result = TurtleShapes.chordLength(10.0, 90.0);
        assertEquals( 14.142136, result, 0.00001 );
    }

    @Test
    public void chordLengthMidAngleShortRadius() {
        double result = TurtleShapes.chordLength(1.0, 90.0);
        assertEquals( 1.4142136, result, 0.00001 );
    }

    @Test
    public void chordLengthMidAngleLongRadius() {
        double result = TurtleShapes.chordLength(1000.0, 90.0);
        assertEquals( 1414.21356, result, 0.00001 );
    }

    @Test
    public void headingToPointSmallRightTurn() {
        Turtle t = new Turtle();
        t.forward(1);
        t.turn(30);
        assertEquals( 60.0,
                TurtleShapes.calculateHeadingToPoint(t,
                    new Point2D.Double(1,1)), 0.001);
    }

    @Test
    public void headingToPointLargeRightTurn() {
        Turtle t = new Turtle();
        t.forward(1);
        t.turn(30);
        assertEquals( 150.0,
                TurtleShapes.calculateHeadingToPoint(t,
                        new Point2D.Double(0,0)), 0.001);
    }

    @Test
    public void headingToPointLargeLeftTurn() {
        Turtle t = new Turtle();
        t.forward(1);
        t.turn(30);

        assertEquals( -120.0,
                TurtleShapes.calculateHeadingToPoint(t,
                        new Point2D.Double(-1,1)), 0.001);
    }

    @Test
    public void headingToPointSmallLeftTurn() {
        Turtle t = new Turtle();
        t.forward(1);
        t.turn(30);

        assertEquals( -30.0,
                TurtleShapes.calculateHeadingToPoint(t,
                        new Point2D.Double(0,2)), 0.001);
    }

    @Test
    public void distanceToPointInteger() {
        Turtle t = new Turtle();
        t.forward(1);
        Point2D.Double pt = new Point2D.Double(10, 1);

        assertEquals( 10.0, TurtleShapes.distanceToPoint(t, pt), 0.0001);
    }

    @Test
    public void distanceToPointDiagonal1() {
        Turtle t = new Turtle();
        t.forward(1);
        Point2D.Double pt = new Point2D.Double(1, 0);

        assertEquals( 1.41421356, TurtleShapes.distanceToPoint(t, pt), 0.0001);
    }

    @Test
    public void distanceToPointDiagonal2() {
        Turtle t = new Turtle();
        t.turn(90);
        t.forward(1);
        Point2D.Double pt = new Point2D.Double(0, 1);

        assertEquals( 1.41421356, TurtleShapes.distanceToPoint(t, pt), 0.0001);
    }

}
