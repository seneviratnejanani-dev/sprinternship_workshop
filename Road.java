/**
 * The Road class represents an edge of the Graph class
 */

public class Road implements Comparable<Road>{
	private String name;
	private Town source;
	private Town destination;
	private int weight;
	
	/**
	 * Fully parameterized constructor
	 * @param source town starting-point
	 * @param destination town end-point
	 * @param weight the distance between the towns
	 * @param name the name of the road
	 */
	public Road(Town source, Town destination, int weight, String name) {
		this.name = name;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}
	
	/**
	 * Constructor with weight preset to 1
	 * @param source town starting-point
	 * @param destination town end-point
	 * @param name the name of the road
	 */
	public Road(Town source, Town destination, String name) {
		weight = 1;
		this.name = name;
		this.source = source;
		this.destination = destination;
	}
	
	/**
	 * Returns true only if the edge contains the given town
	 * @param town the given town
	 * @return true if either the road's source or destination is equal to the town, false otherwise
	 */
	public boolean contains (Town town) {
		return source.equals(town)||destination.equals(town) ? true : false;
	}
	
	/**
	 * Returns the first town on the road
	 * @return the source
	 */
	public Town getSource() {
		return source;
	}
	
	/**
	 * Returns the second town on the road
	 * @return the destination
	 */
	public Town getDestination() {
		return destination;
	}
	
	/**
	 * Gets the distance
	 * @return the distance
	 */
	public int getWeight() {
		return weight;
	}
	
	/**
	 * Gets the name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Two roads are considered equals if they have the same source and destination (since roads are bidirectional, order doesn't matter)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		
		Road r = (Road) o;
		
		if ((r.source.equals(source) && r.destination.equals(destination)) || (r.source.equals(destination) && r.destination.equals(source))) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return source.hashCode() + destination.hashCode();
	}
	
	/**
	 * Compares roads by name 
	 */
	@Override
	public int compareTo(Road o) {
		return name.compareTo(o.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
