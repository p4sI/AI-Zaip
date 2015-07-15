public class Node implements Comparable<Node> {
		public int x, y;
		public Node top;
		public Node topLeft;
		public Node topRight;
		public Node right;
		public Node left;
		public Node bottom;
		public Node bottomLeft;
		public Node bottomRight;
		public float fValue;

		public Node(int x, int y) {
			super();
			this.x = x;
			this.y = y;
			this.fValue = 0;
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

		@Override
		public int compareTo(Node node) {
			if(this.fValue > node.fValue){
				return 1;
			}
			else if(this.fValue < node.fValue){
				return -1;
			}
			else
				return 0;
		}
		
		

	}
