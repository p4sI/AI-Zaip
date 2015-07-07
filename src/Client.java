import java.util.Random;

import lenz.htw.zaip.net.*;

public class Client {

	static NetworkClient networkClient;
	static int[][] board = new int[32][32];
	static int myPlayerNo;

	
	public static void main(String[] args) {
		String serverIP = args[0];
		networkClient = new NetworkClient(serverIP, "HelloKitty");
		init();

	    float startPos = networkClient.getX(myPlayerNo, 1);
	    int startFeld = networkClient.convertCoord2Board(startPos);
	    long time = System.nanoTime();

		while (networkClient.isAlive()) {
			updateBoard();
			Random rnd = new Random();
		    for (int i = 0; i < 3; ++i) {
		    	networkClient.setMoveDirection(i, 1, 0);
		    }
		    networkClient.getBoard(networkClient.convertCoord2Board(networkClient.getX(0, 0)), networkClient.convertCoord2Board(networkClient.getY(0, 0)));
		}

	}
	
	/*
	 * Initalisierung:
	 * - Ruft Spielername ab
	 * - Initialisiert und printed das Board
	 */
	private static void init(){
		myPlayerNo = networkClient.getMyPlayerNumber();
		initBoard();
		printBoard();
	}
	
	/*
	 * Initialisiert das Spielfeld und läd einmal alle Felder
	 * auch die außerhalb des Rings und den Ring selber
	 */
	private static void initBoard(){
		for(int i = 0; i < 32; i++){
	    	for(int j = 0; j < 32; j++){
	    		board[i][j] = networkClient.getBoard(i, j);
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
				System.out.print(board[j][i] + " ");
			}
			System.out.println();
		}
		System.out.println();		
	}

}
