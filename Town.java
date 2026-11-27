import java.util.HashSet;
import java.util.Set;

/**
 * The Town class represents a vertex of the Graph class
 */
public class Town implements Comparable<Town>{
	private String name;
	private int wt;
	private Town backPath;
	private Set<Town> adjacentTowns;
	
	/**
	 * Instantiates a town given its name
	 * @param name the name of the town
	 */
	public Town(String name) {
		this.name = name;
		adjacentTowns = new HashSet<>();
	}
	
	/**
	 * Copy constructor for a town
	 * @param templateTown the town to be copied
	 */
	public Town(Town templateTown) {
		name = templateTown.name;
		wt = templateTown.wt;
		adjacentTowns = templateTown.adjacentTowns;
		backPath = templateTown.backPath;
	}
	
	/**
	 * Gets the name of the town
	 * @return the name of the town
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the current minimum weight from the source to this town during execution of dijkstra's shortest path
	 * @param wt the current minimum weight
	 */
	public void setWt(int wt) {
		this.wt = wt;
	}
	
	/**
	 * Gets the current minimum weight from the source to this town in dijkstra's shortest path
	 * @return the current minimum weight
	 */
	public int getWt() {
		return wt;
	}
	
	/**
	 * Adds a town to the adjacency set
	 * @param adjTown the adjacent town
	 */
	public void addAdjTown(Town adjTown) {
		adjacentTowns.add(adjTown);
	}
	
	/**
	 * Gets the adjacency set
	 * @return the set of adjacent towns
	 */
	public Set<Town> getAdjTowns(){
		return adjacentTowns;
	}
	
	/**
	 * Returns the predecessor to this town in the shortest path from some source town to some destination town
	 * @return
	 */
	public Town getBackPath() {
		return backPath;
	}
	
	/**
	 * Sets the predecessor of this town during construction of a shortest path from some source town to some destination town
	 * @param backPath the predecessor vertex
	 */
	public void setBackPath(Town backPath) {
		this.backPath = backPath;
	}
	
	/**
	 * Compares towns by name
	 */
	@Override
	public int compareTo(Town o) {
		return name.compareTo(o.name);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		Town otherTown = (Town) o;
		return name.equals(otherTown.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
