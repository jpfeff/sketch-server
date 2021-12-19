import javax.sound.sampled.Line;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 *
 * Code completed by:
 * @author Josh Pfefferkorn
 * CS10, Fall 2020
 */
public class Polyline implements Shape {
	// TODO: YOUR CODE HERE

	// arraylist to hold connecting segments
	private ArrayList<Segment> segments;
	// arraylist to hold points
	private ArrayList<Point> points;
	// the line's color
	private Color color;

	/**
	 * A polyline defined by its points
	 */
	public Polyline(ArrayList<Point> points, Color color) {
		// set color
		this.color = color;
		// set points
		this.points = points;
		// initialize arraylist for connecting segments
		segments = new ArrayList<>();
		// loop over the points, adding connecting segments between each pair
		for (int k = 0; k<points.size()-1; k++) {
			Segment connector = new Segment((int)points.get(k).getX(),(int)points.get(k).getY(),(int)points.get(k+1).getX(),(int)points.get(k+1).getY(),color);
			segments.add(connector);
		}
	}

	/**
	 * An "empty" polyline, with only one point so far
	 */
	public Polyline(int x1, int y1, Color color){
		// set color
		this.color = color;
		// initialize arraylist for points
		points = new ArrayList<Point>();
		// add initial point
		points.add(new Point(x1,y1));
		// initialize segments arraylist
		segments = new ArrayList<Segment>();
		// add "empty" segment to first point
		segments.add(new Segment(x1,y1,color));
	}

	/**
	 * Moves the polyline by a specified distance
	 */
	@Override
	public void moveBy(int dx, int dy) {
		// loop over points
		for (int k = 0; k <points.size(); k++) {
			// displace each point by specified amount
			Point newPoint = new Point((int)points.get(k).getX()+dx, (int)points.get(k).getY()+dy);
			// replace it in points arraylist
			points.set(k, newPoint);
		}
		// loop over segments
		for (int k = 0; k <segments.size(); k++) {
			// move each segment by specified amount
			segments.get(k).moveBy(dx,dy);
		}
	}

	/**
	 * Color accessor method
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * Color setter method
	 */
	@Override
	public void setColor(Color color) {
		// set color
		this.color = color;
		// loop over segments and change their color
		for (int k = 0; k <segments.size(); k++) {
			segments.get(k).setColor(color);
		}
	}

	/**
	 * Elongate the polyline by adding a point
	 */
	public void addPoint (Point point) {
		// create a segment that connects the last point to the new point
		Segment connector = new Segment((int)points.get(points.size()-1).getX(),(int)points.get(points.size()-1).getY(),(int)point.getX(),(int)point.getY(), color);
		// add the new segment to segments arraylist
		segments.add(connector);
		// add the new point to points arraylist
		points.add(point);
	}

	/**
	 * Check if point is contained along polyline
	 */
	@Override
	public boolean contains(int x, int y) {
		// loop over segments and see if any contain the point
		for (int k = 0; k <segments.size(); k++) {
			if (segments.get(k).contains(x,y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Draw the polyline
	 */
	@Override
	public void draw(Graphics g) {
		// set graphics color to color
		g.setColor(color);
		// loop over segments and draw them
		for (int k = 0; k <segments.size(); k++) {
			segments.get(k).draw(g);
		}
	}

	/**
	 * toString method, prints polylines as "freehand (x1,y1) (x2,y2) (x3,y3) ... [color]"
	 */
	@Override
	public String toString() {
		// start with "freehand"
		String result = "freehand ";
		// add each coordinate pair
		for (int k = 0; k <points.size(); k++) {
			result += "(" + (int)points.get(k).getX() + "," + (int)points.get(k).getY() + ") ";
		}
		// add color
		result += color.getRGB();
		return result;
	}
}
