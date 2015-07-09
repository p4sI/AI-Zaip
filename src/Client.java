import java.util.Random;

import lenz.htw.zaip.net.*;

public class Client {

	static NetworkClient networkClient;
	static int[][] board = new int[32][32];
	static int myPlayerNo;
	static int currentDestinationX = 0;
	static int currentDestinationY = 0;

	
	public static void main(String[] args) {
		String serverIP = args[0];
		networkClient = new NetworkClient(serverIP, "HelloKitty");
		init();

	    float startPos = networkClient.getX(myPlayerNo, 1);
	    int startFeld = networkClient.convertCoord2Board(startPos);
	    long time = System.nanoTime();
		while (networkClient.isAlive()) {
			updateBoard();
			findDestination(0, 0, 32);
			if(myPlayerNo == 1)System.out.println("DestinationX: " + currentDestinationX + " DestinationY: " + currentDestinationY);
			//if(myPlayerNo == 1) System.out.println("Rating for Player " + myPlayerNo + ": " + rateSector(0, 0, 32));
		    for (int i = 0; i < 3; ++i) {
		    	networkClient.setMoveDirection(i, 1, 1);
		    }
		    networkClient.getBoard(networkClient.convertCoord2Board(networkClient.getX(0, 0)), networkClient.convertCoord2Board(networkClient.getY(0, 0)));
		    
		}

	}
	
	private static void findDestination(int fromX, int fromY, int size){
		if(size == 1){
			currentDestinationX = fromX;
			currentDestinationY = fromY;
			return;
		}
		float bottomLeftRating = rateSector(fromX, fromY, size/2);
		float bottomRightRating = rateSector(fromX+size/2, fromY, size/2);
		float topLeftRating = rateSector(fromX, fromY+size/2, size/2);
		float topRightRating = rateSector(fromX+size/2, fromY+size/2, size/2);
		
		if(bottomLeftRating >= bottomRightRating && bottomLeftRating >= topLeftRating && bottomLeftRating >= topRightRating){
			findDestination(fromX, fromY, size/2);
		}
		else if(bottomRightRating >= bottomLeftRating && bottomRightRating >= topLeftRating && bottomRightRating >= topRightRating){
			findDestination(fromX+size/2, fromY, size/2);
		}
		else if(topLeftRating >= bottomRightRating && topLeftRating >= bottomLeftRating && topLeftRating >= topRightRating){
			findDestination(fromX, fromY+size/2, size/2);
		}
		else if(topRightRating >= bottomRightRating && topRightRating >= bottomLeftRating && topRightRating >= topLeftRating){
			findDestination(fromX+size/2, fromY+size/2, size/2);
		}
			
	}
	
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
		//division by zero leads to NaN, so return 0
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
		System.out.println("I am Player Number " + myPlayerNo);
		initBoard();
		updateBoard();
		printBoard();
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
