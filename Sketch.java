import java.awt.*;
import java.util.HashMap;
import java.util.TreeMap;

public class Sketch {
    // maps shapes to their IDs
    private TreeMap<Integer, Shape> idToShape;
    // tracks the last assigned ID so that shapes aren't overwritten
    private int curIndex;

    public Sketch() {
        // initialize map
        idToShape = new TreeMap<>();
        // set current ID to 0
        curIndex = 0;
    }

    public synchronized void draw(Graphics g) {
        // loop over IDs
        for (int id: idToShape.keySet()) {
            // if a shape exists for that ID draw it
            if (idToShape.get(id) != null) {
                idToShape.get(id).draw(g);
            }
        }
    }

    /**
     * finds the ID of the shape that contains the parameter x and y values
     */
    public synchronized int getID (int x, int y) {
        // loop over IDs from oldest to newest
        for (int id: idToShape.navigableKeySet()) {
            // check if the associated shape contains the point
            if (idToShape.get(id).contains(x,y)) {
                // if it does, return it
                return id;
            }
        }
        // otherwise if no shape matches, return -1
        return -1;
    }

    /**
     * return the shape given an ID
     */
    public synchronized Shape getShape (int id) {
        return idToShape.get(id);
    }

    /**
     * adds a shape at the current index
     */
    public synchronized void addShape (Shape shape) {
        idToShape.put(curIndex, shape);
        // increment the current next open ID
        curIndex++;
    }

    /**
     * removes the shape at a given ID
     */
    public synchronized void removeID(int id) {
        // if the ID exists
        if (idToShape.containsKey(id)) {
            // remove it (and its associated shape)
            idToShape.remove(id);
        }
    }

    /**
     * returns the number of shapes in the sketch
     */
    public synchronized int size() {
        return idToShape.size();
    }
}
