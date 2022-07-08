package hr.fer.zemris.ooup.lab3.zad2.locations;

public class LocationRange {

	private Location location_start;
	private Location location_end;

	public LocationRange() {
		this.location_start = new Location(0, 0);
		this.location_end = new Location(0, 0);
	}

	public LocationRange(Location location_start, Location location_end) {
		this.location_start = location_start;
		this.location_end = location_end;
	}

	public Location getLocation_start() {
		return location_start;
	}

	public void setLocation_start(Location location_start) {
		this.location_start = location_start;
	}

	public Location getLocation_end() {
		return location_end;
	}

	public void setLocation_end(Location location_end) {
		this.location_end = location_end;
	}

	@Override
	public String toString() {
		return "[location_start=" + location_start + ", location_end=" + location_end + "]";
	}

}
