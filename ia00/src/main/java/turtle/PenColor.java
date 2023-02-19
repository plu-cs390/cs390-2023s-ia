package turtle;

import java.awt.*;

/**
 * Enumeration of the possible pen colors for the turtle.
 */
public enum PenColor {
    BLACK (Color.BLACK),
    WHITE (Color.white),
    RED (Color.red),
    ORANGE(Color.orange),
    YELLOW (Color.yellow),
    GREEN (Color.green),
    BLUE (Color.blue),
    PURPLE(Color.magenta);

    private final Color color;

    PenColor(Color c) {
        this.color = c;
    }

    public Color asColor() { return this.color; }
}
