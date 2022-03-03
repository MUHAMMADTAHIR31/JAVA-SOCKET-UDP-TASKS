import java.net.*;
class Receiver{
	public static void main(String arg[])throws Exception{
		
		DatagramSocket socket = new DatagramSocket(9090);
		byte data[] = new byte[255];
	
		DatagramPacket packet = new DatagramPacket(data,0,data.length); 
		socket.receive(packet);		
		
		data=packet.getData();
		int len=packet.getLength();
		
		String msg = new String(data,0,len);
		System.out.println(msg);
		
		
	}
}