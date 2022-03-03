import java.io.*;
import java.net.*;
import java.util.*;

class Chat_user_thread {
	public static void main(String[] args) {
		
		byte[] receiveBytes = new byte[256];
	    String TERMINATE = "bye";
	    int SERVER_PORT = 8080;
	    System.out.println("Client started. Type a message to Server.");
	  
		try {
			Scanner sc = new Scanner(System.in);
			DatagramSocket ds = new DatagramSocket(); 
		   
		    // Get server address
			InetAddress SERVER_ADDRESS = InetAddress.getLocalHost();
		    
			DatagramPacket dataPacket = null;
			dataPacket = new DatagramPacket(receiveBytes, receiveBytes.length);
			
			MessageReceiverThread t = new MessageReceiverThread(ds,dataPacket);
			t.start();
		
		    while (!ds.isClosed()) {
				System.out.print("You Says:");
				String input = sc.nextLine();
				
				// Terminate the client if user says "bye"
				if (input.trim().equalsIgnoreCase(TERMINATE)) {
				   break;
				}
				
				// Construct Datagram packet to send message
				dataPacket = new DatagramPacket(input.getBytes(), input.getBytes().length, SERVER_ADDRESS, SERVER_PORT);
				ds.send(dataPacket);
				
				// Construct Datagram packet to receive message
				//ds.receive(dataPacket);
				
				//String received = new String(dataPacket.getData(), 0, dataPacket.getLength());
                //System.out.println("Server Say: "+received);
				
			}
	    } catch (Exception e) {
	      e.printStackTrace();
		} 
	}
}