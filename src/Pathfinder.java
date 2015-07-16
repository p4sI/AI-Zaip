import java.util.ArrayList;
import java.util.Collections;


public class Pathfinder {

	private static int mapWith = 32;
	private static int mapHeight = 32;
	private ArrayList<ArrayList<Node>> map;
	private ArrayList<Node> openList;			// für die bekannten Knoten
	private ArrayList<Node> closedList;			// fertig untersuchte Knoten
	private Node startNode, endNode;
	

	/*
	 * Zum Erzeugen der Karte und setzen der Kanten
	 */
	public void initNodeList() {
		createMap();
		createEdges();
	}

	/*
	 * A*-Algorithmus
	 * Liefert eine Liste mit allen Knoten vom Start bis zum Ende. 
	 * Das erste Element in der Liste ist dabei der Endknoten, 
	 * das letzte Element ist der erste Punkt nach dem Start
	 */
	public ArrayList<Node> aStar(int xStart, int yStart, int xEnd, int yEnd, int blockedFields){
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		Node currentNode;
		startNode = map.get(xStart).get(yStart);
		startNode.setfValue(0);
		endNode = map.get(xEnd).get(yEnd);
		
		openList.add(startNode);
		
		while(!openList.isEmpty()){
			currentNode = Collections.min(openList);
			openList.remove(currentNode);
			
			// Wenn der momentane Knoten das Ende ist, wird der Pfad erstellt
			// und zurück gegeben. Pfad geht vom Endknoten zum Startknoten!
			if(currentNode.equals(endNode)){
				return buildWaypoints(endNode);
			}
			
			closedList.add(currentNode);
			expandNode(currentNode, blockedFields);
		}
		
		// kein Pfad gefunden
		return null;
	}
	
	/*
	 * Durchsucht die Nachbarknoten und fügt sie der openList hinzu wenn
	 * der Knoten zum ersten mal gefunden wurde oder ein besserer Weg
	 * zu diesem Knoten gefunden wurde
	 */
	private void expandNode(Node currentNode, int blockedFields){
		for(Node successor : currentNode.getSuccessors()){
			// Wenn der Knoten bereits in der closedList steht, mache nichts
			if(closedList.contains(successor)) continue;
			
			// Wenn das Nachbarfeld ein Hinderniss ist (oder die eigene Farbe), mache nichts
			if(successor.getValue() == -1 || successor.getValue() == blockedFields) continue;
			
			// g Wert für den neuen Weg ist der Wert des Vorgängers + 1 für den neuen Weg
			float tentative_gValue = currentNode.getgValue() + 1;
			
			// Wenn der successor bereits auf der open Liste ist, sein neuer Weg
			// jedoch nicht besser ist als der alte, mache nichts
			if(openList.contains(successor) && tentative_gValue >= successor.getgValue()) continue;
			
			// Der neue Weg ist kürzer: Zeiger zum vorgänger wird gesetzt und
			// der g Wert gespeichert
			successor.setParentNode(currentNode);
			successor.setgValue(tentative_gValue);
			
			// h Wert setzen ('Luftlinie' zwischen Knoten und Ziel)
			successor.sethValue(getDistance(successor, endNode));
			
			// Neuen f Wert am Knoten ändern und ggfls in openList einfügen
			successor.setfValue(tentative_gValue + successor.gethValue());
			if(!openList.contains(successor))
				openList.add(successor);
		}
	}
	
	/*
	 * Berechnet die Distanz zwischen zwei Nodes
	 */
	private float getDistance (Node a, Node b){
		return (float) Math.sqrt(Math.pow(b.getX() - a.getX(), 2) 
				+ Math.pow(b.getY() - a.getY(), 2));
	}
	
	/*
	 * Geht über die Zeiger vom letzten Knoten solange zurück
	 * bis kein vorgänger Knoten mehr vorhanden ist und speichert
	 * diese in einer Liste
	 */
	private ArrayList<Node> buildWaypoints(Node endNode){
		ArrayList<Node> waypoints = new ArrayList<Node>();
		Node currentNode = endNode;
		while(currentNode.getParentNode() != null){
			waypoints.add(currentNode);
			currentNode = currentNode.getParentNode();
		}
		return waypoints;
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
				// Alle Nachbarknoten eintragen
				node.setSuccessors();
			}
		}
	}
	
	/*
	 * Gibt den Pfad in der Console aus
	 */
	public static void printPath(ArrayList<Node> waypoints){
		@SuppressWarnings("unchecked")
		ArrayList<Node> reverseWP = (ArrayList<Node>) waypoints.clone();
		Collections.reverse(reverseWP);
		for(Node wp : reverseWP){
			System.out.println("x: " + wp.getX() + " y: " + wp.getY());
		}
	}
}
