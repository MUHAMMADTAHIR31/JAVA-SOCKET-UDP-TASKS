import java.net.*;
import java.util.Scanner;

class Chat_Server_thread {
	public static void main(String[] args) {
	   
	    byte[] receiveBytes = new byte[256];
	    int SERVER_PORT = 8080;
	    System.out.println("Server started. Waiting for client message...");
		
	  try{
		    DatagramSocket ds = new DatagramSocket(SERVER_PORT);
		    Scanner sc = new Scanner(System.in);
			DatagramPacket dp = new DatagramPacket(receiveBytes, receiveBytes.length);
			
			MessageReceiverThread t = new MessageReceiverThread(ds,dp);
			t.start();
			
		    while (!ds.isClosed()) {
				
				// Construct Datagram packet to receive message
				//ds.receive(dp);
				
				//String received = new String(dp.getData(), 0, dp.getLength());
				//System.out.println("Client Says:" + received);
				
				
				System.out.print("You Says:");
				String input = sc.nextLine();
				// Construct Datagram packet to send message
				DatagramPacket sendPacket = new DatagramPacket(input.getBytes(), input.getBytes().length,
				dp.getAddress(), dp.getPort());
				ds.send(sendPacket);
		    }
			ds.close();
		} catch (Exception e) {
		   e.printStackTrace();
		}
	}
}