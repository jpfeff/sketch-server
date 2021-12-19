import java.awt.Color;
import java.awt.Graphics;

/**
 * A rectangle-shaped Shape
 * Defined by an upper-left corner (x1,y1) and a lower-right corner (x2,y2)
 * with x1<=x2 and y1<=y2
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, updated Fall 2016
 *
 * Code completed by:
 * @author Josh Pfefferkorn
 * CS10, Fall 2020
 */
public class Rectangle implements Shape {
	// TODO: YOUR CODE HERE

	private int x1, y1, x2, y2;		// upper left and lower right
	private Color color;			// color

	/**
	 * A rectangle defined by two corners
	 */
	public Rectangle(int x1, int y1, int x2, int y2, Color color) {
		this.x1 = x1; this.x2 = x2;
		this.y1 = y1; this.y2 = y2;
		this.color = color;
	}

	/**
	 * An "empty" rectangle, with only one point set so far
	 */
	public Rectangle(int x1, int y1, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.color = color;
	}


	@Override
	/**
	 * Moves the rectangle by a specified distance
	 */
	public void moveBy(int dx, int dy) {
		// move each corner by the specified amount
		x1 += dx; y1 += dy;
		x2 += dx; y2 += dy;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
		
	@Override
	public boolean contains(int x, int y) {
		return (x >= x1 && x <= x2 && y>=y1 && y<=y2);
	}

	/**
	 * Redefines the rectangle based on new corners
	 */
	public void setCorners(int x1, int y1, int x2, int y2) {
		// Ensure correct upper left and lower right
		this.x1 = Math.min(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.x2 = Math.max(x1, x2);
		this.y2 = Math.max(y1, y2);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}

	/**
	 * toString method, prints rectangles as "rectangle x1 y1 x2 y2 [color]"
	 */
	public String toString() {
		return "rectangle "+x1+" "+y1+" "+x2+" "+y2+" "+color.getRGB();
	}
}
