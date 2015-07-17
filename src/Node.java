import java.util.ArrayList;

public class Node implements Comparable<Node> {
		private volatile int x, y;
		private volatile Node top;
		private volatile Node topLeft;
		private volatile Node topRight;
		private volatile Node right;
		private volatile Node left;
		private volatile Node bottom;
		private volatile Node bottomLeft;
		private volatile Node bottomRight;
		private volatile float fValue;		// Kosten Start -> Ziel (mit diesem Knoten)
		private volatile float hValue;		// Heuristik Knoten -> Ziel
		private volatile float gValue;		// Start -> Knoten
		private volatile Node parentNode;
		private volatile ArrayList<Node> successors;

		public Node(int x, int y) {
			super();
			this.x = x;
			this.y = y;
			this.fValue = 0;
			this.successors = new ArrayList<Node>();
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

		public float getfValue() {
			return fValue;
		}

		public void setfValue(float fValue) {
			this.fValue = fValue;
		}

		public float gethValue() {
			return hValue;
		}

		public void sethValue(float hValue) {
			this.hValue = hValue;
		}

		public Node getParentNode() {
			return parentNode;
		}

		public void setParentNode(Node parentNode) {
			this.parentNode = parentNode;
		}

		public ArrayList<Node> getSuccessors() {
			return successors;
		}

		public void setSuccessors() {
			if(bottomLeft != null && 
					(bottom != null && left != null && bottom.getValue() != -1 && left.getValue() != -1))
				successors.add(bottomLeft);
			if(bottom != null)
				successors.add(bottom);
			if(left != null)
				successors.add(left);
			if(right != null)
				successors.add(right);
			if(bottomRight != null && 
					(bottom != null && right != null && bottom.getValue() != -1 && right.getValue() != -1))
				successors.add(bottomRight);
			if(top != null)
				successors.add(top);
			if(topLeft != null && 
					(top != null && left != null && top.getValue() != -1 && left.getValue() != -1))
				successors.add(topLeft);
			if(topRight != null && 
					(top != null && right != null && top.getValue() != -1 && right.getValue() != -1))
				successors.add(topRight);
		}



		public float getgValue() {
			return gValue;
		}



		public void setgValue(float gValue) {
			this.gValue = gValue;
		}		

	}
