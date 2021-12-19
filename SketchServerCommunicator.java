import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 *
 * Code completed by:
 * @author Josh Pfefferkorn
 * CS10, Fall 2020
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			// loop over sketches
			for (int k=0; k<server.getSketch().size(); k++) {
				// make sure the shape isn't null
				if (server.getSketch().getShape(k) != null) {
					// tell the client to add it
					send("add " + server.getSketch().getShape(k));
				}
			}
			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			String line;
			// continuously read lines
			while ((line = in.readLine()) != null) {
				// split the commands by " "
				String[] pieces = line.split(" ");
				// the first word of the string should be the keyword (either add, recolor, delete, or move)
				String command = pieces[0];
				// if recolor
				if (command.equals("recolor")) {
					// ID should be second part of string (change to int)
					int id = Integer.parseInt(pieces[1]);
					// RGB should be third (change to int)
					int rgb = Integer.parseInt(pieces[2]);
					// update the server's sketch with the new color for the correct shape
					server.getSketch().getShape(id).setColor(new Color(rgb));
				}
				// if delete
				else if (command.equals("delete")) {
					// ID should be second part of string (change to int)
					int id = Integer.parseInt(pieces[1]);
					// remove correct shape from the server's sketch
					server.getSketch().removeID(id);
				}
				// if move
				else if (command.equals("move")) {
					// ID should be second part of string (change to int)
					int id = Integer.parseInt(pieces[1]);
					// x displacement should be third part of string (change to int)
					int dx = Integer.parseInt(pieces[2]);
					// y displacement should be fourth part of string (change to int)
					int dy = Integer.parseInt(pieces[3]);
					// move the correct shape on the server's sketch by the specified values
					server.getSketch().getShape(id).moveBy(dx,dy);
				}
				// if add
				else if (command.equals("add")) {
					// shape type should be second part of string
					String shapeType = pieces[1];
					// rgb should be the last part of the string
					String rgb = pieces[pieces.length-1];

					// if freehand
					if (shapeType.equals("freehand")) {
						// create an arraylist to hold the points
						ArrayList<Point> points = new ArrayList<Point>();
						// loop over all the points
						for (int i = 2; i<pieces.length-1; i++){
							// split x and y values
							String[] coords = pieces[i].split(",");
							// cut out (
							String x = coords[0].substring(1,coords[0].length());
							// cut out )
							String y = coords[1].substring(0,coords[1].length()-1);
							points.add(new Point(Integer.parseInt(x),Integer.parseInt(y)));
						}
						// create the polyline
						Polyline freehand = new Polyline(points,new Color(Integer.parseInt(rgb)));
						// add it to the server sketch
						server.getSketch().addShape(freehand);
					}
					// if the shape is an ellipse, rectangle, or segment
					else {
						// x1 should be third part of string (change to int)
						int x1 = Integer.parseInt(pieces[2]);
						// x2 should be fourth part of string (change to int)
						int y1 = Integer.parseInt(pieces[3]);
						// y1 should be fifth part of string (change to int)
						int x2 = Integer.parseInt(pieces[4]);
						// y2 should be sixth part of string (change to int)
						int y2 = Integer.parseInt(pieces[5]);
						// RGB should be seventh part of string (change to int)
						int rgb2 = Integer.parseInt(pieces[6]);

						// if ellipse
						if (shapeType.equals("ellipse")) {
							// add new ellipse to server's sketch with specified parameters
							server.getSketch().addShape(new Ellipse(x1, y1, x2, y2, new Color(rgb2)));
						}
						else if (shapeType.equals("rectangle")) {
							// add new rectangle to server's sketch with specified parameters
							server.getSketch().addShape(new Rectangle(x1, y1, x2, y2, new Color(rgb2)));
						}
						else if (shapeType.equals("segment")) {
							// add new segment to server's sketch with specified parameters
							server.getSketch().addShape(new Segment(x1, y1, x2, y2, new Color(rgb2)));
						}
					}
				}
				// broadcast command to editors
				server.broadcast(line);
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
