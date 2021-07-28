package finalproject;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/* This interface encompasses the Visual Vertex
 *  and Visual Edge, allowing the Visual Graph class to
 *  easily color both
 */
public interface Colorable {
    /* Static VisualVertex Color values */
    public static final Paint DEFAULT_VERTEX = new Color(0.5, 1, 0.6, 1);
    public static final Paint HIGHLIGHTED_VERTEX = new Color(1, 0.5, 0.5, 1);
    
    /* Static VisualEdge Color values */
    public static final Paint DEFAULT_EDGE = new Color(0.4, 0.9, 0.5, .5);
    public static final Paint HIGHLIGHTED_EDGE = new Color(0.9, 0.1, 0.1, 1);
    
    public void highlight();
    public void reset();
    
    public void highlightAdjacent();
    public void resetAdjacent();
}
