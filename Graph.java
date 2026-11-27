import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
/**
 * The Graph class contains a set of Towns (vertices) and a set of Roads (edges)
 */
public class Graph implements GraphInterface<Town,Road> {

	private Set<Town> vertexSet;
	private Set<Road> edgeSet;
	
	/**
	 * Constructor
	 */
	public Graph () {
		vertexSet = new HashSet<>();
		edgeSet = new HashSet<>();
	}

	@Override
	public Road getEdge(Town sourceVertex, Town destinationVertex) {
		for (Road road : edgeSet) {
			if (road.contains(sourceVertex) && road.contains(destinationVertex)) {
				return road;
			}
		}
		return null;
	}

	@Override
	public Road addEdge(Town sourceVertex, Town destinationVertex, int weight, String description) {
		if (!vertexSet.contains(sourceVertex) || !vertexSet.contains(destinationVertex)) {
			throw new IllegalArgumentException();
		}
		sourceVertex.addAdjTown(destinationVertex);
		destinationVertex.addAdjTown(sourceVertex);
		
		Road edge = new Road(sourceVertex, destinationVertex, weight, description);
		edgeSet.add(edge);
		
		return edge;
	}

	@Override
	public boolean addVertex(Town v) {
		return vertexSet.add(v);
	}

	@Override
	public boolean containsEdge(Town sourceVertex, Town destinationVertex) {
		//Road's equals method checks for matching source and destination (either given order or switched will return true)
		return edgeSet.contains(new Road(sourceVertex, destinationVertex, ""));
	}

	@Override
	public boolean containsVertex(Town v) {
		return vertexSet.contains(v);
	}

	@Override
	public Set<Road> edgeSet() {
		return edgeSet;
	}

	@Override
	public Set<Road> edgesOf(Town vertex) {
		Set<Road> edgesOfSet = new HashSet<>();
		for (Road road: edgeSet) {
			if (road.contains(vertex)) {
				edgesOfSet.add(road);
			}
		}
		return edgesOfSet;
	}

	@Override
	public Road removeEdge(Town sourceVertex, Town destinationVertex, int weight, String description) {
		Road removedRoad = new Road(sourceVertex, destinationVertex, weight, description);
		boolean removed = false;
		
		if (!edgeSet.contains(removedRoad)) {
			return null;
		}
		
		//if a valid weight or description is provided, check for an exact match (since Road's equals method only checks the vertices)
		if (weight > -1 || description != null) {
			for (Road road : edgeSet) {
				if (road.equals(removedRoad)) {
					//if both are provided, both must match
					if (weight > -1 && description != null) {
						if (road.getWeight() == weight && road.getName().equals(description)) {
							removedRoad = road;
							removed = true;
							break;
						} else {
							continue; //if not a match, continue search in the next iteration
						}
					}
					//if only weight is provided
					if (weight > -1) {
						if (road.getWeight() == weight) {
							removedRoad = road;
							removed = true;
							break;
						}
					}
					//if only description is provided
					if (description != null) {
						if (road.getName().equals(description)) {
							removedRoad = road;
							removed = true;
							break;
						}
					}
				}
			}
		} else {
			//if neither weight nor description was provided, current object referenced by removedRoad will suffice for removal (any edge between the source and destination)
			removed = true; 
		}
		
		if (!removed) {
			return null;
		}
		
		//remove adjacency from source and destination vertices
		sourceVertex.getAdjTowns().remove(destinationVertex);
		destinationVertex.getAdjTowns().remove(sourceVertex);
		
		edgeSet.remove(removedRoad);
		
		return removedRoad;
	}

	@Override
	public boolean removeVertex(Town v) {
		if (vertexSet.remove(v) == false) {
			return false;
		}
		
		Set<Road> removeEdgeSet = new HashSet<>();
		
		//remove all edges touching this vertex
		for (Road road : edgeSet) {
			if (road.contains(v)) {
				//if v is the source of the edge, remove it from the destination vertex's adjacency set
				if (road.getSource().equals(v)) {
					road.getDestination().getAdjTowns().remove(v);
				} 
				//otherwise v is the destination, remove it from the source vertex's adjacency set
				road.getSource().getAdjTowns().remove(v);
				
				removeEdgeSet.add(road);
			}
		}
		
		for (Road road : removeEdgeSet) {
			edgeSet.remove(road);
		}
		
		return true;
	}

	@Override
	public Set<Town> vertexSet() {
		return vertexSet;
	}

	@Override
	public ArrayList<String> shortestPath(Town sourceVertex, Town destinationVertex) {
		dijkstraShortestPath(sourceVertex);
	
		
		Stack<String> backPath = new Stack<>();
		Town topVertex = destinationVertex;
		
		//initial check for disjoint graph
		if (topVertex.getBackPath() == null) {
			return null;
		}
		
		while (topVertex != sourceVertex && topVertex != null) {
			Road edge = getEdge(topVertex, topVertex.getBackPath());
			backPath.push(topVertex.getBackPath() + " via " + edge.getName() + " to " + topVertex + " " + edge.getWeight() + " mi");
			topVertex = topVertex.getBackPath();
		}
		
		//there exists no path from source to destination (graph is disjoint)
		if (topVertex == null) {
			return null;
		}
		
		ArrayList<String> forwardPath = new ArrayList<>();
		while (!backPath.isEmpty()) {
			forwardPath.add(backPath.pop());
		}		
		
		return forwardPath;
	}

	@Override
	public void dijkstraShortestPath(Town sourceVertex) {
		//set initial weight values to "infinity" and backpath to null
		for (Town town : vertexSet) {
			town.setWt(Integer.MAX_VALUE);
			town.setBackPath(null);
		}
		//distance from source to source is 0
		sourceVertex.setWt(0);
		
		Set<Town> visitedTowns = new HashSet<>(); 		
		Set<Town> unvisitedNeighbors = new HashSet<>();
		
		//add source to visited set
		Town originVertex = sourceVertex;
		visitedTowns.add(originVertex);

		//algorithm continues until all towns have been visited
		while (visitedTowns.size() != vertexSet.size()) {
			
			//if vertex does not have any neighbors, skip
			if (originVertex.getAdjTowns() != null && !originVertex.getAdjTowns().isEmpty()) {
				
				//add all unvisited neighbors to a set, and update cost if new origin produces a cheaper path
				for (Town neighbor : originVertex.getAdjTowns()) {
					//if neighbor is already part of dijkstra subgraph (is visited), skip
					if (visitedTowns.contains(neighbor)) {
						continue;
					}
					
					int edgeWeight = getEdge(originVertex, neighbor).getWeight();
					int newDistanceFromSource = originVertex.getWt() + edgeWeight;
					
					//if the new path from the source to the neighbor through the current origin is less than the distance of the current path to the neighbor, update
					if (newDistanceFromSource < neighbor.getWt()) { 
						//update cost
						neighbor.setWt(newDistanceFromSource);
						//update predecessor
						neighbor.setBackPath(originVertex);
					}
					//adds to set if not already in it, otherwise does nothing
					unvisitedNeighbors.add(neighbor);
				}
			}
		
			int minimumCost = Integer.MAX_VALUE;
			Town minimumVertex = null;
			
			//find next cheapest vertex to travel to
			for (Town neighbor: unvisitedNeighbors) {
				if (neighbor.getWt() < minimumCost) {
					minimumVertex = neighbor;
					minimumCost = neighbor.getWt();
				}
			}						
			//add minimum vertex to visited set
			visitedTowns.add(minimumVertex);
			//remove minimum vertex from unvisited neighbors set
			unvisitedNeighbors.remove(minimumVertex);
			//set newly added end vertex to be origin
			originVertex = minimumVertex;

		}
	}
	
}
