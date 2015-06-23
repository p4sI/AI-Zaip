import java.util.Random;

import lenz.htw.zaip.net.*;

public class Client {

	static NetworkClient networkClient;
	
	public static void main(String[] args) {
		String serverIP = args[0];
		
		networkClient = new NetworkClient(serverIP, "HelloKitty");

		while (networkClient.isAlive()) {
			Random rnd = new Random();
		    for (int i = 0; i < 3; ++i) {
		    	networkClient.setMoveDirection(i, rnd.nextFloat() - 0.5f, rnd.nextFloat() - 0.5f);
		    }
		    networkClient.getBoard(networkClient.convertCoord2Board(networkClient.getX(0, 0)), networkClient.convertCoord2Board(networkClient.getY(0, 0)));
		}

	}

}
