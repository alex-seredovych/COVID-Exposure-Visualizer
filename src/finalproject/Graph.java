package finalproject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/* This is the Graph class, that holds information
 *  about a graph of type "V". It is an undirected,
 *  and unweigted graph. In our case it is used to
 *  show freindships among a group of people.
 */
public class Graph<V extends Comparable<V>> {
    HashMap<V, List<V>> graph;
    
    /* Generic constructor */
    public Graph() {
        graph = new HashMap<>();
    }
    
    /* Insert a vertex with data "v" only if
     *  that vertex doesn't exist, returns true
     *  if the insertion was successful, else false
     */
    public boolean insertVertex(V v) {
        if(containsVertex(v)) {
            return false;
        }

        graph.put(v, new ArrayList<>());
        return true;
    }
    
    /* Returns if the graph contains a vertex with data "v" */
    public boolean containsVertex(V v) {        
        return graph.containsKey(v);
    }
    
    /* Insert an edge into the graph (bi-directional),
     *  and return true if successful. Returns false if
     *  the edge exists already, or one (or both) of the
     *   verticies is not in the graph
     */
    public boolean insertEdge(V from, V to) {
        if(!containsVertex(from) || !containsVertex(to)) {
            return false;
        }
        if(graph.get(from).contains(to)) {
            return false;
        }
        
        graph.get(from).add(to);
        graph.get(to).add(from);
        return true;
    }
    
    /* Inserts an edge into the graph,
     *  creating new verticies if they
     *  are not present in the graph.
     *  Returns true if the edge is inserted successfully
     */
    public boolean forceInsertEdge(V from, V to) {
        if(!containsVertex(from)) {
            insertVertex(from);
        }
        if(!containsVertex(to)) {
            insertVertex(to);
        }
        return insertEdge(from, to);
    }
    
    /* Returns if the graph contains the desired edge combination */
    public boolean containsEdge(V from, V to) {
        return graph.containsKey(from) && graph.containsKey(to) && graph.get(from).contains(to);
    }
    
    /* Creates the Graph from a given file String, returning
     *  if the creation was successful or not
     */
    public boolean createFromFile(String filePath) {
        try {
            Scanner file = new Scanner(new File(filePath));
            
            while(file.hasNextLine()) {
                String[] line = file.nextLine().split(" ");
                if(!containsVertex((V)line[0])) {
                    insertVertex((V)line[0]);
                }
                for(int i = 1; i < line.length; i++) {
                    forceInsertEdge((V)line[0], (V)line[i]);
                }
            }
        }
        catch(Exception e) {
            System.out.println("File couldn't be opened");
            return false;
        }
        return true;
    }
    
    /* Returns an ArrayList containing the data from all verticies
     *  that are part of the largest fully connected subgraph. Arbitrarily
     *  returns the first found complete subgraph in the case of a tie.
     */
    public ArrayList<V> getLargestCompleteSubgraph() {
        ArrayList<V> max = new ArrayList<>();
        /* Create a list of verticies for convinience */
        ArrayList<V> vl = new ArrayList<>();
        graph.keySet().forEach(e -> vl.add(e));
        
        /* Loop through each node as a starting node,
         *  finding the largest graph that includes that node
         *  (and only nodes coming after that node), if this
         *  is larger than the current max, replace the current max
         */
        for(int i = 0; i < vl.size(); i++) {
            if(graph.get(vl.get(i)).size() < max.size()) { continue; }

            ArrayList<V> temp = increase(new ArrayList<>(), i, vl);
            if(temp.size() > max.size()) {
                max = temp;
            }
        }
        return max;
    }
    
    /* Takes a current list of all connected points
     *  and attempts to increase the size of that graph by adding
     *  some vertex later in the list (as long as it is valid to
     *  be added). If any combination of verticies is greater than
     *  the max, update the max
     */
    public ArrayList<V> increase(ArrayList<V> current, int index, ArrayList<V> vl) {
        current.add(vl.get(index));
        ArrayList<V> max = copyList(current);
        
        for(int i = index+1; i < vl.size(); i++) {
            /* Ignore verticies with less connections than the current graph */
            if(graph.get(vl.get(i)).size() < current.size()) { continue; }
            /* Ignore if vertex doesn't connect to all current verticies */
            boolean valid = true;
            for(V v : current) {
                if(!graph.get(vl.get(i)).contains(v)) { valid = false; break; }
            }
            if(!valid) { continue; }
            
            /* Try to increase from this new index */
            ArrayList t = increase(copyList(current), i, vl);
            /* If "t" is larger, update the max */
            if(t.size() > max.size()) {
                max = t;
            }
        }
        return max;
    }
    
    /* Returns a hard copy of a given ArrayList */
    public ArrayList<V> copyList(ArrayList<V> arr) {
        ArrayList<V> result = new ArrayList<>();
        arr.forEach(e -> result.add(e));
        return result;
    }
    
    /* Prints a representation of the graph, showing the key vertex
     *  followed by all of the verticies that that vertex is connected to
     */
    public void print() {
        for(V key : graph.keySet()) {
            System.out.printf("%s : ", key.toString());
            for(V to : graph.get(key)) {
                System.out.printf("%s ", to.toString());
            }
            System.out.println();
        }
    }
}
