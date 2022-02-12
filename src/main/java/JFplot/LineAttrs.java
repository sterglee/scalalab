

package JFplot;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class parses line style, markers, and color as used by Matlab's plot 
 * command.
 * See http://www.mathworks.com/access/helpdesk/help/techdoc/index.html?/access/helpdesk/help/techdoc/ref/plot.html&http://www.google.com/search?hl=en&rlz=1B3GGGL_enSI249SI250&sa=X&oi=spell&resnum=0&ct=result&cd=1&q=matlab+plot+command&spell=1&aq=t
 * for details.
 * 
 * The algorithm is very forgiving - only the first known strings are used, 
 * unknown characters are ignored.
 * 
 * @author Marko Klopcic, Apr. 2008
 */
class LineAttrs {

    private Color m_color;
    private Shape m_marker;
    private float[] m_style;
    
    private final static Pattern m_colorPattern = Pattern.compile("([ymcrgbwk])");
    private final static Pattern m_stylePatternChars = Pattern.compile("(\\-\\.|\\-{1,2}|:)");
    
    /*
     * There are four groups:
     * 1: (hexagram|pentagram|square|diamond)
     *    These are long names, which are later cut from the line spec to avoid
     *    confusion with color characters
     * 2: ([\\+\\*\\^v<>oxsdph])
     *    One of single character markers 
     * 3: [^-](\\.)
     *    Dot without preceeding '-', because '-.' is dash-dot line style 
     * 4: ^(\\.)
     *    Dot at the start of the line spec string.
     */
    private final static Pattern m_markerPattern = 
        Pattern.compile("(hexagram|pentagram|square|diamond)|([\\+\\*\\^v<>oxsdph])|[^-](\\.)|^(\\.)");
    
    private final static Map<String, Color> m_colorMap = new TreeMap<String, Color>();
    private final static Map<String, Shape> m_markerMap = new TreeMap<String, Shape>();
    private final static Map<String, float[]> m_styleMap = new TreeMap<String, float[]>();
    private final static double MARKER_DIM = 5;

    // These three attrs are for testing purposes only. Do not use them for 
    // anything else!
    String m_styleStr = "";
    String m_markerStr = "";
    String m_colorStr = "";
    
    static {
        initColorMap();
        initMarkerMap();
        initStyleMap();
    }
                   

    /**
     * Constructs line style according to the given lineSpec, with default 
     * width 1.
     * 
     * @param lineSpec defines line color, style, and markers. Any combination 
     * of color, marker and style is allowed. The following properties can 
     * be specified: 
     * 
     * <ul>
     * <li>Colors:</li>
     * <ul>
     *   <li>r - red</li>
     *   <li>g - green</li>
     *   <li>b - blue</li>
     *   <li>c - cym</li>
     *   <li>y - yellow</li>
     *   <li>m - magenta</li>
     *   <li>k - black</li>
     * </ul>

     * <li>Markers:</li>
     * <ul>
     *   <li>+ - Plus sign</li>
     *   <li>o - Circle</li>
     *   <li>* - Asterisk</li>
     *   <li>. - Point</li>
     *   <li>x - Cross</li>
     *   <li>^ - Upward-pointing triangle</li>
     *   <li>v - Downward-pointing triangle</li>
     *   <li>> - Right-pointing triangle</li>
     *   <li>< - Left-pointing triangle</li>
     *   <li>'square' or s - Square</li>
     *   <li>'diamond' or d - Diamond</li>
     *   <li>'pentagram' or p - Five-pointed star (pentagram)</li>
     *   <li>'hexagram' or h - Six-pointed star (hexagram)</li>
     * </ul>
     * <li>Styles</li>
     * <ul>
     *  <li><b>-</b> solid line</li> 
     *  <li><b>--</b> dashed line</li>
     *  <li><b>:</b> dotted line</li> 
     *  <li><b>-.</b> dash-dot line</li> 
     * </ul>
     * </ul>
     * 
     * <b>Examples:</b><p>
     * "yx--" - yellow dashed line with crosses at points<br>
     * ":c" - dotted cyan line without markers<br>
     * "w" - white solid line (the same as "w--")<br>
     */
    LineAttrs(String lineSpec) {
        this(lineSpec, 1);
    }
    
    
    /**
     * Constructs line style according to the given lineSpec
     * 
     * @param lineWidth line width
     * @param lineSpec defines line color, style, and markers. Any combination 
     * of color, marker and style is allowed. The following properties can 
     * be specified: 
     * 
     * <ul>
     * <li>Colors:</li>
     * <ul>
     *   <li>r - red</li>
     *   <li>g - green</li>
     *   <li>b - blue</li>
     *   <li>c - cym</li>
     *   <li>y - yellow</li>
     *   <li>m - magenta</li>
     *   <li>k - black</li>
     * </ul>

     * <li>Markers:</li>
     * <ul>
     *   <li>+ - Plus sign</li>
     *   <li>o - Circle</li>
     *   <li>* - Asterisk</li>
     *   <li>. - Point</li>
     *   <li>x - Cross</li>
     *   <li>'square' or s - Square</li>
     *   <li>'diamond' or d - Diamond</li>
     *   <li>^ - Upward-pointing triangle</li>
     *   <li>v - Downward-pointing triangle</li>
     *   <li>> - Right-pointing triangle</li>
     *   <li>< - Left-pointing triangle</li>
     *   <li>'pentagram' or p - Five-pointed star (pentagram)</li>
     *   <li>'hexagram' or h - Six-pointed star (hexagram)</li>
     * </ul>
     * <li>Styles</li>
     * <ul>
     *  <li><b>-</b> solid line</li> 
     *  <li><b>--</b> dashed line</li>
     *  <li><b>:</b> dotted line</li> 
     *  <li><b>-.</b> dash-dot line</li> 
     * </ul>
     * </ul>
     * 
     * <b>Examples:</b><p>
     * "yx--" - yellow dashed line with crosses at points<br>
     * ":c" - dotted cyan line without markers<br>
     * "w" - white solid line (the same as "w--")<br>
     */
    LineAttrs(String lineSpec, int lineWidth) {

        // System.out.println(">>> " + lineSpec);
        StringBuilder specStr = new StringBuilder(lineSpec); 
        m_marker = getMarker(specStr);
        m_style = getLineStyle(specStr.toString(), lineWidth);
        m_color = getColor(specStr.toString());
    }

    
    private static void initStyleMap() {
        m_styleMap.put("-", new float[]{1});
        m_styleMap.put("--", new float[]{5, 4});
        m_styleMap.put(":", new float[]{1, 3});
        m_styleMap.put("-.", new float[]{5, 4, 1, 4});
    }

    
    private static void initMarkerMap() {
        GeneralPath path = new GeneralPath();
        path.append(new Line2D.Double(0, -MARKER_DIM, 0, MARKER_DIM), false);
        path.append(new Line2D.Double(-MARKER_DIM, 0, MARKER_DIM, 0), false);
        m_markerMap.put("+", path);
        m_markerMap.put("o", new Ellipse2D.Double(-MARKER_DIM/2, -MARKER_DIM/2, MARKER_DIM, MARKER_DIM));
        path = new GeneralPath();
        path.append(new Line2D.Double(0, -MARKER_DIM, 0, MARKER_DIM), false);
        path.append(new Line2D.Double(-MARKER_DIM, 0, MARKER_DIM, 0), false);
        double diag = MARKER_DIM - 1;
        path.append(new Line2D.Double(-diag, -diag, diag, diag), false);
        path.append(new Line2D.Double(-diag, diag, diag, -diag), false);
        m_markerMap.put("*", path);
        m_markerMap.put(".", new Ellipse2D.Double(-1, -1, 2, 2)); 
        path = new GeneralPath();
        path.append(new Line2D.Double(-MARKER_DIM, -MARKER_DIM, MARKER_DIM, MARKER_DIM), false);
        path.append(new Line2D.Double(-MARKER_DIM, MARKER_DIM, MARKER_DIM, -MARKER_DIM), false);
        m_markerMap.put("x", path); 
        m_markerMap.put("square", new Rectangle2D.Double(-MARKER_DIM/2, -MARKER_DIM/2, MARKER_DIM, MARKER_DIM)); 
        m_markerMap.put("s", m_markerMap.get("square")); 

        path = new GeneralPath();
        path.append(new Line2D.Double(0, -MARKER_DIM, MARKER_DIM, 0), false);
        path.append(new Line2D.Double(MARKER_DIM, 0, 0, MARKER_DIM), false);
        path.append(new Line2D.Double(0, MARKER_DIM, -MARKER_DIM, 0), false);
        path.append(new Line2D.Double(-MARKER_DIM, 0, 0, -MARKER_DIM), false);
        m_markerMap.put("diamond", path); 
        m_markerMap.put("d", m_markerMap.get("diamond"));
        
        path = new GeneralPath();
        path.append(new Line2D.Double(-MARKER_DIM, MARKER_DIM, 0, 0), false);
        path.append(new Line2D.Double(0, 0, MARKER_DIM, MARKER_DIM), false);
        m_markerMap.put("^", path); 

        path = new GeneralPath();
        path.append(new Line2D.Double(-MARKER_DIM, -MARKER_DIM, 0, 0), false);
        path.append(new Line2D.Double(0, 0, MARKER_DIM, -MARKER_DIM), false);
        m_markerMap.put("v", path);
        
        path = new GeneralPath();
        path.append(new Line2D.Double(MARKER_DIM, MARKER_DIM, 0, 0), false);
        path.append(new Line2D.Double(0, 0, MARKER_DIM, -MARKER_DIM), false);
        m_markerMap.put("<", path); 

        path = new GeneralPath();
        path.append(new Line2D.Double(-MARKER_DIM, MARKER_DIM, 0, 0), false);
        path.append(new Line2D.Double(0, 0, -MARKER_DIM, -MARKER_DIM), false);
        m_markerMap.put(">", path); 
        
        // pentagram and hexagram are currently not supported - they are
        // shown as circles.
        m_markerMap.put("pentagram", new Ellipse2D.Double(0, 0, MARKER_DIM, MARKER_DIM)); 
        m_markerMap.put("p", m_markerMap.get("pentagram")); 
        m_markerMap.put("hexagram", new Ellipse2D.Double(0, 0, MARKER_DIM, MARKER_DIM)); 
        m_markerMap.put("h", m_markerMap.get("hexagram"));
    }

    
    private static void initColorMap() {
        m_colorMap.put("r", Color.red);
        m_colorMap.put("g", Color.green);
        m_colorMap.put("b", Color.blue);
        m_colorMap.put("c", Color.cyan);
        m_colorMap.put("y", Color.yellow);
        m_colorMap.put("m", Color.magenta);
        m_colorMap.put("k", Color.black);
        m_colorMap.put("w", Color.white);
    }

    /*
     * This method modifies input string, if words are used for marker selection.
     * Words are removed, because they contain characters, which are also used
     * for colors and other markers. For example, 'hexagon' contains 'x'.
     * 
     * @param lineWidth multiplies dash lengths for wider lines
     */
    private float[] getLineStyle(String lineSpec, int lineWidth) {

        Matcher matcher = m_stylePatternChars.matcher(lineSpec);
        if (matcher.find()) {
            m_styleStr = matcher.group(1);
            return m_styleMap.get(m_styleStr);
        }
        
        return null;
    }

    
    private Shape getMarker(StringBuilder lineSpec) {

        Matcher matcher = m_markerPattern.matcher(lineSpec);
        if (matcher.find()) {
            m_markerStr = matcher.group(1);
            if (m_markerStr != null) {
                // it is one of the first group: hexagram, ... 
                lineSpec.delete(matcher.start(), matcher.end());
            } else if (matcher.group(2) != null) {
                // it is one of single character markers
                m_markerStr = matcher.group(2); 
            } else { 
                // it is dot marker, third or fourth group matched 
                m_markerStr = ".";
            }
        }        
        return m_markerMap.get(m_markerStr);
    }

    
    private Color getColor(String lineSpec) {
        Matcher matcher = m_colorPattern.matcher(lineSpec);
        if (matcher.find()) {
            // System.out.println("Color: " + matcher.group(1));
            m_colorStr = matcher.group(1);
            return m_colorMap.get(m_colorStr);
        }

        return null;
    }

    
    /** 
     * Returns color according to the specification given in constructor, or
     * null if the color was not specified. 
     */
    Color getColor() {
        return m_color;
    }

    
    /** 
     * Returns style according to the specification given in constructor, or
     * null if the style was not specified. 
     */
    float[] getStyle() {
        return m_style;
    }

    
    /** 
     * Returns marker according to the specification given in constructor, or
     * null if the marker was not specified. 
     */
    Shape getMarker() {
        return m_marker;
    }
    
    
    
     // For testing purposes only. 
    public static void main(String [] args) {
        new LineAttrs("squarey");
        new LineAttrs("square");
        new LineAttrs("ysquarey");
        new LineAttrs("-.ysquarey");
        new LineAttrs("-ysquarey.");
        new LineAttrs("yo");
        new LineAttrs("k-.");
        new LineAttrs("c.-");
        new LineAttrs("y...");
        new LineAttrs("z");
        new LineAttrs("rgb");
        new LineAttrs("oy.-");
        new LineAttrs("hexagramy-.");
    }
     
}
