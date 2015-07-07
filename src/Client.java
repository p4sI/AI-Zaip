import java.util.Random;

import lenz.htw.zaip.net.*;

public class Client {

	static NetworkClient networkClient;
	static int[][] board = new int[32][32];

	
	public static void main(String[] args) {
		String serverIP = args[0];
		
		networkClient = new NetworkClient(serverIP, "HelloKitty");

		int myPlayerNo = networkClient.getMyPlayerNumber();
	    float startPos = networkClient.getX(myPlayerNo, 1);
	    int startFeld = networkClient.convertCoord2Board(startPos);
	    long time = System.nanoTime();
	    

	    updateBoard();
	    printBoard();
	    
		while (networkClient.isAlive()) {
			Random rnd = new Random();
		    for (int i = 0; i < 3; ++i) {
		    	networkClient.setMoveDirection(i, 1, 0);
		    }
		    networkClient.getBoard(networkClient.convertCoord2Board(networkClient.getX(0, 0)), networkClient.convertCoord2Board(networkClient.getY(0, 0)));
		    
		    
		    
		    
		    
		}

	}
	
	private static void updateBoard(){
		for(int i = 0; i < 32; i++){
	    	for(int j = 0; j < 32; j++){
	    		board[i][j] = networkClient.getBoard(i, j);
	    	}
	    }
	}
	
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
