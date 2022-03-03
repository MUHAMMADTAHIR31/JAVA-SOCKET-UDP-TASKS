import java.net.*;
class Sender{
	public static void main(String arg[])throws Exception{
		
		DatagramSocket socket = new DatagramSocket();
		String msg="Hello";
		
		byte data[]=msg.getBytes();
		int port=9090;
		
		InetAddress addre=InetAddress.getByName("localhost");
		DatagramPacket packet = new DatagramPacket(data,0,data.length,addre,port); 
		
		socket.send(packet);		
		
	}
}