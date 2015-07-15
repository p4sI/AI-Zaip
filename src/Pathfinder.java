import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pathfinder {

	private static int mapWith = 32;
	private static int mapHeight = 32;
	private static ArrayList<ArrayList<Node>> map;
	private ArrayList<Node> waypoints;

	/*
	 * Zum erzeugen der Karte und setzen der Kanten
	 */
	public void initNodeList() {
		createMap();
		createEdges();
	}

	public ArrayList<Node> aStar(int xStart, int yStart, int xEnd, int yEnd, int blockedFields){
		waypoints = new ArrayList<Node>();
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closedList = new ArrayList<Node>();
		//anfangs für die start node genutzt
		Node currentNode;
		Node startNode = map.get(xStart).get(yStart);
		startNode-fValue = 0;
		Node endNode = map.get(xEnd).get(yEnd);
		
		openList.add(startNode);
		
		while(!openList.isEmpty()){
			currentNode = Collections.min(openList);
			openList.remove(currentNode);
			
			if(currentNode.equals(endNode)){
				return waypoints;
			}
		}
		
		//kein Pfad gefunden
		return null;
	}
	
	/*
	 * Knoten erstellen für den Graphen
	 */
	private void createMap() {
		Node node;
		map = new ArrayList<ArrayList<Node>>();
		for (int x = 0; x < mapWith; x++) {
			map.add(new ArrayList<Node>());
			for (int y = 0; y < mapHeight; y++) {
				node = new Node(x, y);
				map.get(x).add(node);
			}
		}
	}

	/*
	 * Nachbarknoten setzen
	 * Die Bedingungen zeigen genau auf das Feld, welches die Grenzen darstellt
	 */
	private void createEdges() {
		for (int x = 0; x < mapWith; x++) {
			for (int y = 0; y < mapHeight; y++) {
				Node node = map.get(x).get(y);
				if (!(y == mapHeight - 1))
					node.setTop(map.get(x).get(y + 1));
				if (!(y == mapHeight - 1) && !(x == mapWith - 1))
					node.setTopRight(map.get(x + 1).get(y + 1));
				if (!(x == mapWith - 1))
					node.setRight(map.get(x + 1).get(y));
				if (!(x == mapWith - 1) && !(y == 0))
					node.setBottomRight(map.get(x + 1).get(y - 1));
				if (!(y == 0))
					node.setBottom(map.get(x).get(y - 1));
				if (!(x == 0) && !(y == 0))
					node.setBottomLeft(map.get(x - 1).get(y - 1));
				if (!(x == 0))
					node.setLeft(map.get(x - 1).get(y));
				if (!(x == 0) && !(y == mapHeight - 1))
					node.setTopLeft(map.get(x - 1).get(y + 1));
			}
		}
	}
}
