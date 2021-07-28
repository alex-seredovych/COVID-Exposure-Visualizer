package finalproject;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/* The Visual Vertex class that creates a visual representation
 *  of an individual vertex in the graph
 */
public class VisualVertex implements Colorable {
    /* Static variables */
    static final int SIZE = 35;
    
    /* Create the visual elements of the VisualVertex */
    Circle c = new Circle(0, 0, SIZE);
    Text t = new Text();
    public StackPane p = new StackPane(c, t);
    
    /* Create data to hold the coordinates, the current color,
     *  and a list of all edges that this vertex connects to
     */
    public double x, y;
    int state;
    public ArrayList<VisualEdge> edges = new ArrayList<>();
    
    /* Generic contrsutctor, needs the data as a string, and coordinates */
    public VisualVertex(String s, double x, double y) {
        /* Color the circle with the default color */
        c.setFill(Colorable.DEFAULT_VERTEX);
        
        /* Create a new text field with the vertex data */
        t.setText(s);
        t.setFill(Color.BLACK);

        /* Create a StackPane to hold the circle and text */
        p.setAlignment(Pos.CENTER);
        p.setLayoutX(x - SIZE);
        p.setLayoutY(y - SIZE);
        p.setOnMouseClicked(HANDLE_VERTEX);
        
        /* Store the values for x, y, and the current color state */
        this.x = x;
        this.y = y;
        state = 0;
    }
    
    /* Adds a visual edge to the list of edges connected to this vertex */
    public void addEdge(VisualEdge e) {
        edges.add(e);
    }
    
    @Override
    /* Highlights the current vertex in the highlight color*/
    public void highlight() {
        c.setFill(Colorable.HIGHLIGHTED_VERTEX);
        state = 1;
    }
    
    @Override
    /* Resets the current vertex to the default color */
    public void reset() {
        c.setFill(Colorable.DEFAULT_VERTEX);
        state = 0;
    }

    @Override
    /* Highlights the current vertex, and all adjacent edges and verticies */
    public void highlightAdjacent() {
        highlight();
        for(VisualEdge e : edges) {
            e.highlightAdjacent();
        }
    }
    
    @Override
    /* Resets the color of the current vertex,
     *  and all adjacent edges and verticies
     */
    public void resetAdjacent() {
        reset();
        for(VisualEdge e : edges) {
            e.resetAdjacent();
        }
    }
    
    /* Swaps between highlighting/resetting the vertex and all
     *  adjacent edges and verticies
     */
    public final EventHandler<MouseEvent> HANDLE_VERTEX = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if(state == 0) {
                highlightAdjacent();
            }
            else {
                resetAdjacent();
            }
        }
    };
}
