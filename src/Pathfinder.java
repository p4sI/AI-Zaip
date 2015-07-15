import java.util.ArrayList;

public class Pathfinder {

	private static int mapWith = 32;
	private static int mapHeight = 32;
	private static ArrayList<ArrayList<Node>> map;

	/*
	 * Zum erzeugen der Karte und setzen der Kanten
	 */
	public void initNodeList() {
		createMap();
		createEdges();
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

	public class Node {
		public int x, y;
		public Node top;
		public Node topLeft;
		public Node topRight;
		public Node right;
		public Node left;
		public Node bottom;
		public Node bottomLeft;
		public Node bottomRight;

		public Node(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public int getValue() {
			return Client.board[x][y];
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public Node getTop() {
			return top;
		}

		public void setTop(Node top) {
			this.top = top;
		}

		public Node getTopLeft() {
			return topLeft;
		}

		public void setTopLeft(Node topLeft) {
			this.topLeft = topLeft;
		}

		public Node getTopRight() {
			return topRight;
		}

		public void setTopRight(Node topRight) {
			this.topRight = topRight;
		}

		public Node getRight() {
			return right;
		}

		public void setRight(Node right) {
			this.right = right;
		}

		public Node getLeft() {
			return left;
		}

		public void setLeft(Node left) {
			this.left = left;
		}

		public Node getBottom() {
			return bottom;
		}

		public void setBottom(Node bottom) {
			this.bottom = bottom;
		}

		public Node getBottomLeft() {
			return bottomLeft;
		}

		public void setBottomLeft(Node bottomLeft) {
			this.bottomLeft = bottomLeft;
		}

		public Node getBottomRight() {
			return bottomRight;
		}

		public void setBottomRight(Node bottomRight) {
			this.bottomRight = bottomRight;
		}

	}

}
