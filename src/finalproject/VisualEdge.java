package finalproject;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;

/* This Visual Edge class creates a curve that connects
 *  two different verticies in the graph, using a QuadCurve
 */
public class VisualEdge implements Colorable{
    /* Static variables */
    static final int SIZE = 10;
    static final int STROKE_WIDTH = 2;
    
    /* Store the verticies that this edge connects, and its color state*/
    VisualVertex a, b;
    int state;
    
    /* The visual componenet of the edge */
    QuadCurve line;
    
    
    public VisualEdge(VisualVertex a, VisualVertex b) {
        /* Store the data for the verticies,
         *  and add this edge to the verticies
         */
        this.a = a;
        this.b = b;
        a.addEdge(this);
        b.addEdge(this);
        
        /* Create a curve based on the coordiates of the two points */
        line = new QuadCurve(a.x, a.y, (a.x+b.x)/4.0, (a.y+b.y)/4.0, b.x, b.y);
        
        /* Set the properties of the line */
        line.setStrokeWidth(STROKE_WIDTH);
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Colorable.DEFAULT_EDGE);
        state = 0;
    }
    
    /* Returns if this edge contains the vertex "v" */
    public boolean contains(VisualVertex v) {
        return a.equals(v) || b.equals(v);
    }
    
    /* Knowing one edge, return the opposite edge */
    public VisualVertex getAdjacent(VisualVertex v) {
        if(!contains(v)) {
            return null;
        }
        return a.equals(v) ? b : a;
    }
    
    
    @Override
    /* Set this edge to its highlighted color */
    public void highlight() {
        line.setStroke(Colorable.HIGHLIGHTED_EDGE);
        state = 1;
    }
    
    @Override
    /* Set this edge to its default color */
    public void reset() {
        line.setStroke(Colorable.DEFAULT_EDGE);
        state = 0;
    }
    
    @Override
    /* Highlight this edge, and the two adjacent verticies 
     *  (if they are not already highlighted)
     */
    public void highlightAdjacent() {
        highlight();
        if(a.state != 1) {
            a.highlight();
        }
        if(b.state != 1) {
            b.highlight();
        }
    }
    
    @Override
    /* Set this edge and all adjacent verticies to the defaul color */
    public void resetAdjacent() {
        reset();
        if(a.state != 0) {
            a.reset();
        }
        if(b.state != 0) {
            b.reset();
        }
    }
}
