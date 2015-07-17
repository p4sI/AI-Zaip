import java.util.ArrayList;

import lenz.htw.zaip.net.*;

public class Client {

	static NetworkClient networkClient;
	static int[][] board = new int[32][32];
	static int myPlayerNo;
	static int[] currentDestinationX = {0,0,0};
	static int[] currentDestinationY = {0,0,0};
	static ArrayList<ArrayList<Node>> waypointLists;
	static Pathfinder[] pathfinders;
	static long[] searchDestinationLoopTime;
	static long stoneOneStopTime;
	
	//statistics
	static int pathsOverOwnFields;
	
	public static void main(String[] args) {
		String serverIP = args[0];
		networkClient = new NetworkClient(serverIP, "HelloTitty");
		init();
	    
		while (networkClient.isAlive()) {
		    for (int i = 0; i < 3; ++i) {
		    	ArrayList<Node> currentWaypoints;
		    	updateBoard();
		    	
		    	// Alle zwei Sekunden neues Ziel suchen
		    	if(System.currentTimeMillis() - searchDestinationLoopTime[i] > 500
		    			|| waypointLists.get(i).isEmpty()){
		    		findDestinationAndCreateWaypoints(i);
		    		searchDestinationLoopTime[i] = System.currentTimeMillis();
		    	}
		    	// Ziel vorhanden, zum nächsten Wegpunkt laufen
		    	else{
		    		currentWaypoints = waypointLists.get(i);
		    		
		    		// Gehe in Richtung des aktuelles Wegpunktes
		    		if(!currentWaypoints.isEmpty()){
		    			float x = currentWaypoints.get(currentWaypoints.size()-1).getX() - networkClient.convertCoord2Board(networkClient.getX(myPlayerNo, i));
			    		float y = currentWaypoints.get(currentWaypoints.size()-1).getY() - networkClient.convertCoord2Board(networkClient.getY(myPlayerNo, i));
			    		if(i != 0)
			    			networkClient.setMoveDirection(i, x, y);
			    		else{
			    			if(System.currentTimeMillis() - stoneOneStopTime > 150){
			    				networkClient.setMoveDirection(i, x, y);
			    			}
			    		}
			    		
	    			}
		    		
		    		// Prüfen ob der Stein bereits am Wegpunkt angelegt ist und falls ja, Wegpunkt entfernen und neue Richtung setzen
		    		if(currentWaypoints.get(currentWaypoints.size()-1).getX() == networkClient.convertCoord2Board(networkClient.getX(myPlayerNo, i)) && 
		    				currentWaypoints.get(currentWaypoints.size()-1).getY() == networkClient.convertCoord2Board(networkClient.getY(myPlayerNo, i))){
		    			
		    			currentWaypoints.remove(currentWaypoints.size()-1);
				    	networkClient.setMoveDirection(i, 0, 0);
				    	if(i == 0)
				    		stoneOneStopTime = System.currentTimeMillis();
		    		}
		    	}
		    }
		}
		System.out.println("ende");
	}
	
	/*
	 * Sucht ein neues Ziel und generiert die Wegpunkte für den Stein welcher
	 * übergeben wird. 
	 */
	private static void findDestinationAndCreateWaypoints(int i) {
		findDestination(0, 0, 32, i);

		// Neue Wegpunkte erstellen: Dabei nicht über eigene Felder gehen
		ArrayList<Node> newWaypoints = pathfinders[i].aStar(
				networkClient.convertCoord2Board(networkClient.getX(myPlayerNo, i)), 
				networkClient.convertCoord2Board(networkClient.getY(myPlayerNo, i)), 
				currentDestinationX[i], 
				currentDestinationY[i], 
				myPlayerNo+1);
		
		// Falls keine Weg gefunden wurde, erstelle neuen Weg bei dem auch eigenen Felder OK sind
		if(newWaypoints == null){
			newWaypoints = pathfinders[i].aStar(
    				networkClient.convertCoord2Board(networkClient.getX(myPlayerNo, i)), 
    				networkClient.convertCoord2Board(networkClient.getY(myPlayerNo, i)), 
    				currentDestinationX[i], 
    				currentDestinationY[i], 
    				-1);
			System.out.println(++pathsOverOwnFields);
		}
		waypointLists.set(i, newWaypoints);
		
		// Debug print
		
		if(i==0 && myPlayerNo == 0)System.out.println("P: " + myPlayerNo + " S: " + i 
		+ " From: (" + networkClient.convertCoord2Board(networkClient.getX(myPlayerNo, i))
		+ "/" + networkClient.convertCoord2Board(networkClient.getY(myPlayerNo, i)) + ") "
		+ "To: (" + currentDestinationX[i] + "/" + currentDestinationY[i] + ")" );
		//Pathfinder.printPath(newWaypoints);
		
	}

	/*
	 * Sucht ein Ziel, je nachdem wie die momentante Feldsituation aussieht:
	 * Es wird das Feld in einzelne Teile zerlegt und die kleineren Teile
	 * Untersucht, solange bis nurnoch ein Kästchen untersucht wird.
	 */
	private static void findDestination(int fromX, int fromY, int size, int stoneNumber){
		if(size == 1){
			currentDestinationX[stoneNumber] = fromX;
			currentDestinationY[stoneNumber] = fromY;
			return;
		}
		float bottomLeftRating = rateSector(fromX, fromY, size/2);
		float bottomRightRating = rateSector(fromX+size/2, fromY, size/2);
		float topLeftRating = rateSector(fromX, fromY+size/2, size/2);
		float topRightRating = rateSector(fromX+size/2, fromY+size/2, size/2);
		
		if(bottomLeftRating >= bottomRightRating && bottomLeftRating >= topLeftRating && bottomLeftRating >= topRightRating){
			findDestination(fromX, fromY, size/2, stoneNumber);
		}
		else if(bottomRightRating >= bottomLeftRating && bottomRightRating >= topLeftRating && bottomRightRating >= topRightRating){
			findDestination(fromX+size/2, fromY, size/2, stoneNumber);
		}
		else if(topLeftRating >= bottomRightRating && topLeftRating >= bottomLeftRating && topLeftRating >= topRightRating){
			findDestination(fromX, fromY+size/2, size/2, stoneNumber);
		}
		else if(topRightRating >= bottomRightRating && topRightRating >= bottomLeftRating && topRightRating >= topLeftRating){
			findDestination(fromX+size/2, fromY+size/2, size/2, stoneNumber);
		}
			
	}
	
	/*
	 * Bewertet einen Feldausschnitt:
	 * Gibt eine prozentuale Angabe über die neu einfärbbaren Felder zurück
	 */
	private static float rateSector(int fromX, int fromY, int size){
		int fieldCount = size*size;
		int fieldsNotAccessible = 0;
		int fieldsFree = 0;
		int fieldsPlayerOne = 0, fieldsPlayerTwo = 0, fieldsPlayerThree = 0, fieldsPlayerFour = 0;
		int fieldsTakeable = 0;
		for(int i = fromX; i < fromX+size; i++){
			for(int j = fromY; j < fromY+size; j++){
				switch (board[i][j]){
					case -1:
						fieldsNotAccessible++;
						break;
					case 0:
						fieldsFree++;
						break;
					case 1:
						fieldsPlayerOne++;
						break;
					case 2:
						fieldsPlayerTwo++;
						break;
					case 3:
						fieldsPlayerThree++;
						break;
					case 4:
						fieldsPlayerFour++;
						break;
				}
			}
		}
		//System.out.println("-1: " + fieldsNotAccessible + " 0: " + fieldsFree + " 1: " + fieldsPlayerOne + " 2: " + fieldsPlayerTwo + " 3: " + fieldsPlayerThree + " 4: " + fieldsPlayerFour);
		if(myPlayerNo == 0){
			fieldsTakeable = fieldsFree + fieldsPlayerTwo + fieldsPlayerThree + fieldsPlayerFour;
		} 
		else if(myPlayerNo == 1){
			fieldsTakeable = fieldsFree + fieldsPlayerOne + fieldsPlayerThree + fieldsPlayerFour;
		}
		else if(myPlayerNo == 2){
			fieldsTakeable = fieldsFree + fieldsPlayerOne + fieldsPlayerTwo + fieldsPlayerFour;
		}
		else if(myPlayerNo == 3){
			fieldsTakeable = fieldsFree + fieldsPlayerOne + fieldsPlayerTwo + fieldsPlayerThree;
		}
		// Division durch null ergibt NaN, also return 0
		if(fieldCount - fieldsNotAccessible == 0)
			return 0;
		else
			return (float)fieldsTakeable / (float)(fieldCount - fieldsNotAccessible);
	}
	
	/*
	 * Initalisierung:
	 * - Ruft Spielername ab
	 * - Initialisiert und printed das Board
	 */
	private static void init(){
		myPlayerNo = networkClient.getMyPlayerNumber();
		waypointLists = new ArrayList<ArrayList<Node>>();
		pathfinders = new Pathfinder[3];
		searchDestinationLoopTime = new long[3];
		searchDestinationLoopTime[0] = System.currentTimeMillis();
		searchDestinationLoopTime[1] = System.currentTimeMillis();
		searchDestinationLoopTime[2] = System.currentTimeMillis();
		
		// Board Setup
		initBoard();
		updateBoard();
		printBoard();
		
		// Pathfinder Setup
		for(int i = 0; i < 3; i++){
			pathfinders[i] = new Pathfinder();
			pathfinders[i].initNodeList();
			waypointLists.add(new ArrayList<Node>());
		}
		System.out.println("I am Player Number " + myPlayerNo);
		
		
	}
	
	/*
	 * Initialisiert das Spielfeld so, dass alle Felder zuerst 
	 * mit -1 belegt sind und somit bei der späteren Berechnung
	 * von Felder welche angelaufen werden können rausfallen
	 */
	private static void initBoard(){
		for(int i = 0; i < 32; i++){
	    	for(int j = 0; j < 32; j++){
	    		board[i][j] = -1;
	    	}
	    }
	}
	
	/*
	 * Updated nur die Felder innerhalb des Spielfeldes, 
	 * also innerhalb des Rings
	 */
	private static void updateBoard(){
    	for(int j = 11; j <= 20; j++){
	    	board[1][j] = networkClient.getBoard(1, j);
	    	board[30][j] = networkClient.getBoard(30, j);
	    }
    	for(int j = 9; j <= 22; j++){
	    	board[2][j] = networkClient.getBoard(2, j);
	    	board[29][j] = networkClient.getBoard(29, j);
	    }
    	for(int i = 0; i <= 4; i++){
	    	for(int j = 7-i; j <= 24+i; j++){
		    	board[3 + i][j] = networkClient.getBoard(3 + i,j);
		    	board[28 - i][j] = networkClient.getBoard(28 - i,j);
		    }
    	}
    	for(int j = 3; j <= 28; j++){
	    	board[8][j] = networkClient.getBoard(8, j);
	    	board[23][j] = networkClient.getBoard(23, j);
	    }
    	for(int j = 2; j <= 29; j++){
	    	board[9][j] = networkClient.getBoard(9, j);
	    	board[10][j] = networkClient.getBoard(10, j);
	    	board[22][j] = networkClient.getBoard(22, j);
	    	board[21][j] = networkClient.getBoard(21, j);
	    }
    	for(int i = 0; i < 10; i++){
	    	for(int j = 1; j <= 30; j++){
		    	board[11+i][j] = networkClient.getBoard(11+i, j);
		    }
    	}
	}
	
	/*
	 * Gibt das Board in der Konsole aus
	 */
	private static void printBoard(){
		for(int i = 31; i >= 0; i--){
			for(int j = 0; j < 32; j++){
				System.out.format("%3d", board[j][i]);
			}
			System.out.println();
		}
		System.out.println();		
	}

}
