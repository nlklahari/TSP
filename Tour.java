import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Lahari N.
 * 
 * Project Description: The goal of the project is – given a list of points, 
 * ordering them is such a way that the length of the path of all the points 
 * circles back to the home point in the shortest path possible.
 */

public class Tour {

    private class Node {
    	private Point p;
    	private Node next;
    	
        public Node(Point p, Node next) {
        	this.p = p;
        	this.next = next;
        }
       
        public Node (Point p) {
        	this.p = p;
        }
        
    }
    
    private Node head;
    private int size;

	/**
     *  creates an empty tour and an instantiated head for tour
     */
    public Tour() {
    	head = null;
    	size = 0;
    }

    /**
     *  creates the 4-point tour a->b->c->d->a (for debugging)
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public Tour(Point a, Point b, Point c, Point d)  
    {
    	head =  new Node (a);
    	Node NB =  new Node (b);
    	Node NC =  new Node (c);
    	Node ND = new Node (d);
    	head.next = NB;
    	NB.next = NC;
    	NC.next = ND;
    	ND.next = head;
    	
    	size = 4;
    }
    
    /**
     *  returns the number of points in this tour
     * @return
     */
    public int size()
    {return size;}
    
    /**
     *  returns the length of this tour
     * @return 
     */
    public double length()
    {
    	Node current = head;
    	double length = current.p.distanceTo(current.next.p);
    	current = current.next;
    	while (current != head) {
    		length+= current.p.distanceTo(current.next.p);
    		current = current.next;
    	}
    	return length;
    }
    
    /**
     *  returns a string representation of this tour
     */
    public String toString()
    {
    	String toReturn = "";
    	Node current = head;
    	for (int i = 0; i < size-1; i++) {

    		toReturn+= current.p.toString()+"\n";
    		current = current.next;
    	}
    	toReturn += current.p.toString();
    	return toReturn;
    }
    
    /** 
     * draws this tour to standard drawing
     */
    public void draw()
    {
    	// can set scale by reading later
    	StdDraw.setScale(0, 600);
    	Node current = head;   	
    	for (int i = 0; i < size; i++) {

    		current.p.drawTo(current.next.p);
    		current = current.next;
    	}
    }
    
    /**
     * inserts p into the tour using the nearest neighbor heuristic
     * @param p
     */
    public void insertNearest(Point q)
    {
    	if (head == null) {
    		head = new Node(q);
    		head.next = head;
    	} else {
	    	double minDistance = Double.MAX_VALUE;
	    	Node newP = new Node (q);
	    	Node current = head;
	    	Node closest = current;
	    	double CalculatedD = current.p.distanceTo(q); 
	    	if (CalculatedD < minDistance) {
    			closest = current;
    			minDistance = CalculatedD;   			
    		}
	    	current = current.next;
	    	while (current != head) {
	    		 CalculatedD = current.p.distanceTo(q); 
	    		if (CalculatedD < minDistance) {
	    			closest = current;
	    			minDistance = CalculatedD;   			
	    		}
	    		current = current.next;
	    	}
	    	// at this point we have the closest Node to p
	    	newP.next = closest.next;
	    	closest.next = newP;
    	}
    	size++;
    }
    
    /** 
     * inserts p into the tour using the smallest increase heuristic
     * @param p
     */
    public void insertSmallest(Point q)                   
    {
    	if (head == null) {
    		head = new Node(q);
    		head.next = head;
		} else {
			double minLength = Double.MAX_VALUE;
			Node newP = new Node(q);
			Node current = head;			
			Node currentClosest = current; 
			
			// insert new point after head
			current.next = newP;
			newP.next = current.next;
			
			if (this.length() < minLength) {
				currentClosest = current;
				minLength = this.length();
				currentClosest.next = currentClosest.next.next; // remove inserted point
				//System.out.println("inside first if");
			} 
				
			current = current.next; // move on from head, allowing you to set condition for while loop
			while (current != head) {
				
				current.next = newP;
				newP.next = current.next; 
				if (this.length() < minLength) {
					currentClosest = current;
					minLength = this.length();
					currentClosest.next = currentClosest.next.next;
				} 				
				current = current.next;
			}
			// insert new point after current closest
			currentClosest.next = newP;
			newP.next = currentClosest.next;
		}
    	size++; //added size 
    }
    
    public static void main(String[] args)
    {
		
/*		 Tour t = new Tour (new Point (100,100), new Point (500,100), new Point (500,
		 500), new Point (100,500)); System.out.println("Length = "+ t.length());
		 System.out.println(t.toString()); t.draw();
		*/
		  try {
		  Tour t1 = new Tour();
		  Tour t2 = new Tour();
			  
		  BufferedReader tsp10 = new BufferedReader (new FileReader (new File
		  ("./input/tsp10.txt"))); 
		  String line = tsp10.readLine();
		  line = tsp10.readLine();
		  // finish reading first line 
		  while (line != null) { // go till the end 
			  String [] arr = line.split(" "); // create string array
			  double [] coordinates = new double [arr.length]; 
			  for (int i = 0; i < arr.length; i++) { 
				  coordinates[i]= Double.parseDouble(arr[i]);
				  // add doubles to array 
			  }
			  System.out.println(coordinates[0]);
			  Point newPoint = new Point(coordinates[0], coordinates[1]);
			  //t1.insertNearest(newPoint);
			  t2.insertSmallest(newPoint);
			  line = tsp10.readLine();
		  }
		  //System.out.println(t1);
		  System.out.println(t2);
		  //System.out.println("InsertNearest = "+ t1.length());
		  System.out.println("InsertSmallest = "+ t2.length());
		  //System.out.println(t1.size());
		  System.out.println(t2.size());
		  //t1.draw();
		  t2.draw();
		  
		  tsp10.close();
		  } catch (Exception e ) {
			  System.out.println(e);
		  
		  }
    	}
}







