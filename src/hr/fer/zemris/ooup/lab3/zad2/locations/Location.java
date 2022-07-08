package hr.fer.zemris.ooup.lab3.zad2.locations;

public class Location implements Comparable<Location> {

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}

	private int x;
	private int y;
	private int previous_y;
	private int previous_x;

	public int getPrevious_y() {
		return previous_y;
	}

	public void setPrevious_y(int previous_y) {
		this.previous_y = previous_y;
	}

	public int getPrevious_x() {
		return previous_x;
	}

	public void setPrevious_x(int previous_x) {
		this.previous_x = previous_x;
	}

	public Location(int x, int y) {
		this.previous_x = this.x;
		this.previous_y = this.y;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.previous_x = this.x;
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.previous_y = this.y;
		this.y = y;
	}

	public Location getLocation() {
		return this;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		System.out.println("X: " + x + ", Y: " + y);
	}

	@Override
	public int compareTo(Location l) {
		return compare(this, l);
	}

	private int compare(Location l1, Location l2) {
		if(l1.getY()<l2.getY())
			return -1;
		else if(l1.getY()==l2.getY()) {
			if(l1.getX()<l2.getX())
				return -1;
			else if (l1.getX()>l2.getX())
				return 1;
			else 
				return 0;
		}else
			return 1;
	}

}
