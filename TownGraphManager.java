import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The TownGraphManager class is the data manager class. It has a Graph object. 
 */
public class TownGraphManager implements TownGraphManagerInterface {
	
	private Graph graph;
	
	/**
	 * Constructor
	 */
	public TownGraphManager() {
		graph = new Graph();
	}
	
	/**
	 * Adds towns and roads to the town graph from a file
	 * @param file the file to retrieve data from
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void populateTownGraph(File file) throws FileNotFoundException, IOException {
		Scanner fileReader = new Scanner(file);
		
		while (fileReader.hasNextLine()) {
			String[] roadAndTowns = fileReader.nextLine().split(";");
			String[] roadNameAndMiles = roadAndTowns[0].split(",");
			
			String roadName = roadNameAndMiles[0];
			int roadMiles = Integer.parseInt(roadNameAndMiles[1]);
			String town1 = roadAndTowns[1];
			String town2 = roadAndTowns[2];
			
			addTown(town1);
			addTown(town2);
			addRoad(town1, town2, roadMiles, roadName);
		}
		
		fileReader.close();
	}
	
	/**
	 * Gets the town with the given name from the graph
	 * @param townName the name of the town
	 * @return the Town object in the graph
	 */
	public Town getTown(String townName) {
		for (Town town : graph.vertexSet()) {
			if (town.getName().equals(townName)) {
				return town;
			}
		}
		return null;
	}
	
	@Override
	public boolean addRoad(String town1, String town2, int weight, String roadName) {
		try {
			graph.addEdge(getTown(town1), getTown(town2), weight, roadName);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	@Override
	public String getRoad(String town1, String town2) {
		Road road = graph.getEdge(new Town(town1), new Town(town2));
		if (road != null) {
			return road.getName();
		}
		return null;
	}

	@Override
	public boolean addTown(String v) {
		return graph.addVertex(new Town(v));
	}

	@Override
	public boolean containsTown(String v) {
		return graph.containsVertex(new Town(v));
	}

	@Override
	public boolean containsRoadConnection(String town1, String town2) {
		return graph.containsEdge(new Town(town1), new Town(town2));
	}

	@Override
	public ArrayList<String> allRoads() {
		ArrayList<String> list = new ArrayList<>();
		for (Road road : graph.edgeSet()) {
			list.add(road.toString());
		}
		//sort roads alphabetically
		Collections.sort(list);
		return list;
	}

	@Override
	public boolean deleteRoadConnection(String town1, String town2, String road) {
		return graph.removeEdge(new Town(town1), new Town(town2), -1, road) == null ? false : true;
	}

	@Override
	public boolean deleteTown(String v) {
		return graph.removeVertex(new Town(v));
	}

	@Override
	public ArrayList<String> allTowns() {
		ArrayList<String> list = new ArrayList<>();
		for (Town town : graph.vertexSet()) {
			list.add(town.getName());
		}
		//sort towns alphabetically
		Collections.sort(list);
		return list;
	}

	@Override
	public ArrayList<String> getPath(String town1, String town2) {
		ArrayList<String> path = graph.shortestPath(getTown(town1), getTown(town2));
		if (path == null) {
			return new ArrayList<String>();
		}
		return path;
	}

}
