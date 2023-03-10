package bchart.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *  The {@code BarChartPanel} class represents a panel that displays a bar chart.
 *  It supports adding a bar (with a specified name, value, and category) and
 *  drawing all of the bars to the screen using standard draw.
 *  The bars are drawn horizontally (in the order in which they are added from
 *  top to bottom) and colored according to the category.
 *  The name and value of the bar and drawn with the bar.
 *
 *  @author Kevin Wayne with modifications by David Wolff
 */
public class BarChartPanel extends JComponent {

    // color palette for bars
    private static final Color[] COLORS = initColors();

    private String title;               // bar chart title
    private String xAxisLabel;          // x-axis label
    private String dataSource;          // data source
    private String caption;                   // caption
    private TreeMap<String, Color> colorOf;   // map category to color
    private ArrayList<String> names;          // list of bar names
    private ArrayList<Integer> values;        // list of bar values
    private ArrayList<Color> colors;          // list of bar colors
    private boolean isSetMaxValue = false;
    private int maxValue = 0;

    /**
     * Creates a new bar chart with empty title, xAxisLabel and source.
     */
    public BarChartPanel() {
        this.title = "No title";
        this.xAxisLabel = "";
        this.dataSource = "";
        this.setPreferredSize(new Dimension(800, 650));
        colorOf = new TreeMap<String, Color>();
        reset();
    }

    public void setXAxisLabel( String label ) { this.xAxisLabel = label; }
    public void setDataSource( String source ) { this.dataSource = source; }

    public void setTitle( String title ) { this.title = title; }

    // initialize the colors
    private static Color[] initColors() {

        // 12 colors from http://colorbrewer2.org/#type=qualitative&scheme=Set3&n=12
        String[] hex12 = {
                "#80b1d3", "#fdb462", "#b3de69", "#fccde5",
                "#8dd3c7", "#ffffb3", "#bebada", "#fb8072",
                "#d9d9d9", "#bc80bd", "#ccebc5", "#ffed6f"
        };

        // 20 colors from https://vega.github.io/vega/docs/schemes/
        // replaced #d62728 with #d64c4c
        String[] hex20 = {
                "#aec7e8", "#c5b0d5", "#c49c94", "#dbdb8d", "#17becf",
                "#9edae5", "#f7b6d2", "#c7c7c7", "#1f77b4", "#ff7f0e",
                "#ffbb78", "#98df8a", "#d64c4c", "#2ca02c", "#9467bd",
                "#8c564b", "#ff9896", "#e377c2", "#7f7f7f", "#bcbd22",
        };

        // use 20 colors
        Color[] colors = new Color[hex20.length];
        for (int i = 0; i < hex20.length; i++)
            colors[i] = Color.decode(hex20[i]);
        return colors;
    }

    /**
     * Sets the maximum x-value of this bar chart (instead of having it set automatially).
     * This method is useful if you know that the values stay within a given range.
     *
     * @param maxValue the maximum value
     */
    public void setMaxValue(int maxValue) {
        if (maxValue <= 0) throw new IllegalArgumentException("maximum value must be positive");
        this.isSetMaxValue = true;
        this.maxValue = maxValue;
    }

    /**
     * Sets the caption of this bar chart.
     * The caption is drawn in the lower-right part of the window.
     *
     * @param caption the caption
     */
    public void setCaption(String caption) {
        if (caption == null) throw new IllegalArgumentException("caption is null");
        this.caption = caption;
    }

    /**
     * Adds a bar to the bar chart.
     * The length of a bar is proportional to its value.
     * The bars are drawn from top to bottom in the order they are added.
     * All bars from the same category are drawn with the same color.
     *
     * @param name     the name of the bar
     * @param value    the value of the bar
     * @param category the category of bar
     */
    public void add(String name, int value, String category) {
        if (name == null) throw new IllegalArgumentException("name is null");
        if (category == null) throw new IllegalArgumentException("category is null");
        if (value <= 0) throw new IllegalArgumentException("value must be positive");

        if (!colorOf.containsKey(category)) {
            colorOf.put(category, COLORS[colorOf.size() % COLORS.length]);
        }
        Color color = colorOf.get(category);
        names.add(name);
        values.add(value);
        colors.add(color);
    }

    /**
     * Removes all of the bars from this bar chart (but keep the color scheme).
     * This method is convenient when drawing an animated bar chart.
     */
    public void reset() {
        names = new ArrayList<String>();
        values = new ArrayList<Integer>();
        colors = new ArrayList<Color>();
        caption = "";
    }

    // compute units (multiple of 1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, ...)
    // so that between 4 and 8 axes labels
    private static int getUnits(double xmax) {
        int units = 1;
        while (Math.floor(xmax / units) >= 8) {
            // hack to identify 20, 200, 2000, ...
            if (units % 9 == 2)  units = units * 5 / 2;
            else                 units = units * 2;
        }
        return units;
    }

    /**
     * Draws this bar chart.
     */
    public void paintComponent( Graphics g ) {
        // nothing to draw
        if (names.isEmpty()) return;

        final int LEFT_MARGIN = 10;
        final int RIGHT_MARGIN = 80;
        final int VERT_MARGIN = 10;
        int w = getWidth();
        int h = getHeight();
        int chartWidth = w - LEFT_MARGIN - RIGHT_MARGIN;

        Graphics2D g2d = (Graphics2D)g;

        // leave room for at least 8 bars
        int numberOfBars = Math.max(8, names.size());

        // set the scale of the coordinate axes
        double xmax = Double.NEGATIVE_INFINITY;
        for (int value : values) {
            if (value > xmax) xmax = value;
        }
        if (isSetMaxValue) xmax = maxValue;

        // draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2d.drawString(title, 45, 35);

        // draw x-axis label
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2d.drawString(xAxisLabel, 10, 75);

        // draw axes
        int units = getUnits(xmax);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (int unit = 0; unit <= xmax; unit += units) {
            int x = (int)( ((double)unit / xmax) * chartWidth + LEFT_MARGIN );
            g2d.setColor(Color.GRAY);
            centerText(g2d, x, 100, String.format("%,d", unit));
            g2d.setColor(new Color(230, 230, 230));
            g2d.drawLine(x, 105, x, h - VERT_MARGIN );
        }

        // draw caption
        g2d.setColor(Color.LIGHT_GRAY);
        if      (caption.length() <= 4) g2d.setFont(new Font("SansSerif", Font.BOLD, 100));
        else if (caption.length() <= 8) g2d.setFont(new Font("SansSerif", Font.BOLD, 60));
        else                            g2d.setFont(new Font("SansSerif", Font.BOLD, 40));
        textRight(g2d, w - RIGHT_MARGIN, 500, caption);

        // draw data source acknowledgment
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textRight(g2d, w - RIGHT_MARGIN, 550, dataSource);

        final int BAR_HEIGHT = 40;
        final int BAR_MARGIN = 10;
        // draw bars
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            int value = values.get(i);
            Color color = colors.get(i);
            g2d.setColor(color);
            int barWidth = (int)( (value / xmax) * chartWidth );
            int barY = 115 + i * (BAR_HEIGHT + BAR_MARGIN);
            g2d.fillRect(LEFT_MARGIN, barY, barWidth, BAR_HEIGHT);
            g2d.setColor(Color.BLACK);
            int fontSize = (int) Math.ceil(14 * 10.0 / numberOfBars);
            g2d.setFont(new Font("SansSerif", Font.BOLD, fontSize));
            textRight(g2d, barWidth + LEFT_MARGIN - 5, barY + BAR_HEIGHT / 2, name);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
            g2d.setColor(Color.DARK_GRAY);
            textLeft(g2d, barWidth + LEFT_MARGIN + 5, barY + BAR_HEIGHT / 2, String.format("%,d", value));
        }
    }

    private void centerText( Graphics2D g2d, int x, int y, String text ) {
        FontMetrics metrics = g2d.getFontMetrics();
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        g2d.drawString(text, x - ws/2, y + hs);
    }

    private void textRight( Graphics2D g2d, int x, int y, String text ) {
        FontMetrics metrics = g2d.getFontMetrics();
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        g2d.drawString(text, x - ws, y + hs);
    }

    private void textLeft( Graphics2D g2d, int x, int y, String text ) {
        FontMetrics metrics = g2d.getFontMetrics();
        int hs = metrics.getDescent();
        g2d.drawString(text, x, y + hs);
    }

}