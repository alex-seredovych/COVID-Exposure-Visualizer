package finalproject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/* This is the VisualGraph class. It exteds the Graph class, and
 *  is used to display that graph in a JavaFX window, along with
 *  some extra functionailities though the use of buttons, etc.
 */

public class VisualGraph<V extends Comparable<V>> extends Graph<V> {
    /* Static variables */
    private static final double PREF_SPACING = 20;
    private static final double PREF_SIZE = 900;
    private static final int RANDOM_EDGE_CREATION_WEIGHT = 4;//2
    private static final String[] DEFAULT_RAND_NAMES = new String[] {
        "Zach", "Nathan", "Samuel", "Alex", "John", "Erika", "Sarah", "Sebastian"
    };
    
    /* Create a new HashMap for storing the visual elements for both
     *  the verticies and the edges
     */
    private HashMap<V, VisualVertex> verticies = new HashMap<>();
    private HashMap<String, VisualEdge> edges = new HashMap<>();
    
    /* Create all of the JavaFX elements to hold the visuals */
    private Pane p = new Pane();
    private Group g = new Group(p);
    private VBox buttonBox = new VBox(PREF_SPACING);
    private Button[] buttons;
    public HBox display = new HBox(PREF_SPACING);
    
    /* Generic contructor */
    public VisualGraph() {
        /* Call the super class (create generic Graph) */
        super();
        
        /* Create the three side buttons and store them in a VBox */
        buttons = new Button[] {
                  new Button("Show Largest Friend Group"),
                  new Button("Reset Graph Colors"),
                  new Button("Randomize Graph")
        };
        
        buttonBox.getChildren().addAll(buttons[0], buttons[1], buttons[2]);
        buttons[0].setPrefSize(PREF_SIZE * 0.2, PREF_SIZE * 0.05);
        buttons[1].setPrefSize(PREF_SIZE * 0.2, PREF_SIZE * 0.05);
        buttons[2].setPrefSize(PREF_SIZE * 0.2, PREF_SIZE * 0.05);
        buttonBox.setAlignment(Pos.CENTER);
        
        /* Set all of the event handlers for the buttons */
        buttons[0].setOnMouseClicked(HANDLE_CLIQUE);
        buttons[1].setOnMouseClicked(HANDLE_RESET);
        buttons[2].setOnMouseClicked(HANDLE_RANDOM);
        
        /* Set up the data for the display elements */
        g.maxHeight(PREF_SIZE);
        g.maxWidth(PREF_SIZE);
        
        p.maxHeight(PREF_SIZE * 2/3);
        p.maxWidth(PREF_SIZE * 2/3);
        
        g.setTranslateX(p.getWidth() / 2.0);
        g.setTranslateY(p.getHeight() / 2.0);
        

        /* Add the elements to the display and set padding */
        display.getChildren().addAll(g, buttonBox);
        display.setAlignment(Pos.CENTER);
        display.setPadding(new Insets(20));
    }
    
    /* Initialized the visual part of the graph so that it can be viewed */
    public void init() {
        initVerticies();
        initEdges();
        drawVerticies();
    }
    
    /* Return the HBox that holds the Visual Graph (so it can be displayed)*/
    public HBox getDisplay() {
        return display;
    }
    
    /* Using the parent Graph, create all vericies as Visual Verticies */
    public void initVerticies() {
        
        /* This angle is used to calculate where to put the
         *  nodes to make the graph radial
         */
        double angle = Math.toRadians(360.0 / graph.keySet().size());
        double scale = PREF_SIZE * 0.4;
        
        int i = 0;
        for(V key : graph.keySet()) {
            verticies.put(key, new VisualVertex(key.toString(),
                    scale * (Math.cos((double)i * angle)),
                    scale * (Math.sin((double)i * angle))));
            i++;
        }
    }
    
    /* Adds the verticies to the group (so they can be seen on top
     *  of the edges)
     */
    public void drawVerticies() {
        for(V key : graph.keySet()) {
            g.getChildren().add(verticies.get(key).p);
        }
    }
    
    /* Creates the Visual Edges and adds them to the group to be displayed */
    public void initEdges() {
        for(V key : graph.keySet()) {
            for(V to : graph.get(key)) {
                if(!edges.containsKey(getEdgeKey(key, to))) {
                    VisualEdge e = new VisualEdge(verticies.get(key), verticies.get(to));
                    edges.put(getEdgeKey(key, to), e); /* Uses a unique String key */
                    g.getChildren().add(e.line);
                }
            }
        }
    }
    
    /* Used for the HashMap of edges to convert the generic data from the two
     *  verticies into a unique String key
     */
    public String getEdgeKey(V a, V b) {
        if(a.toString().compareTo(b.toString()) == 0) {
            return a.toString();
        }
        else if(a.toString().compareTo(b.toString()) < 0) {
            return a.toString() + b.toString();
        }
        else {
            return b.toString() + a.toString();
        }
    }
    
    /* Removes all the data from the graph */
    public void clearGraph() {
        verticies = new HashMap<>();
        edges = new HashMap<>();
        graph = new HashMap<>();
        g.getChildren().clear();
    }
    
    
    /* Creates a new graph using an ArrayList of verticies, randomly creating
     *  edges between the points
     */
    public void randomize(ArrayList<V> vertexList) {
        clearGraph();
        int len = vertexList.size();
        if(len == 0) { /* Return if there is nothing to add */
            return; 
        }
        else if(len == 1) { /* If size = 1, add single node and initialize */
            insertVertex(vertexList.get(0));
        }
        else { /* If size > 1, create all verticies and randomly add edges */
            vertexList.forEach(v -> insertVertex(v));
            
            Random rand = new Random();
            int range = rand.nextInt(len * RANDOM_EDGE_CREATION_WEIGHT) + len;
            for(int i = 0; i < range; i++) {
                int a = rand.nextInt(len);
                int b = rand.nextInt(len);
                while(a == b) {
                    b = rand.nextInt(len);
                }
                insertEdge(vertexList.get(a), vertexList.get(b));
            }
        }
        init();
    }
    
    @Override
    /* Create the graph from a file, and initializes it */
    public boolean createFromFile(String filePath) {
        if(!super.createFromFile(filePath)) {
            return false;
        }
        init();
        return true;
    }
    
    /* This event handler finds and highlights the largest complete subgraph */
    private final EventHandler<MouseEvent> HANDLE_CLIQUE = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            ArrayList<V> arr = getLargestCompleteSubgraph();
            
            /* Don't color if "arr" is empty or doesn't have any friend group */
            if(arr.size() < 2) {
                return;
            }
            
            for(int i = 0; i < arr.size()-1; i++) {
                for(int j = i+1; j < arr.size(); j++) {
                    /* Only color if the edge exists (to avoid potential errors) */
                    if(edges.keySet().contains(getEdgeKey(arr.get(i), arr.get(j)))) {
                        edges.get(getEdgeKey(arr.get(i), arr.get(j))).highlightAdjacent();
                    }
                }
            }
        }
    };
    
    /* This event handler resets all of the colors on the graph */
    private final EventHandler<MouseEvent> HANDLE_RESET = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            for(V key : verticies.keySet()) {
                verticies.get(key).reset();
            }
            for(String key : edges.keySet()) {
                edges.get(key).reset();
            }
        }
    };
    
    /* This event handler randomized the graph */
    private final EventHandler<MouseEvent> HANDLE_RANDOM = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            ArrayList<V> arr = new ArrayList<>();
            for(String s : DEFAULT_RAND_NAMES) {
                arr.add((V)s);
            }
            randomize(arr);
        }
    };

    
}
