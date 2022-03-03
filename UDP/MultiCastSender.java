import java.net.*;
class MultiCastSender{
	public static void main(String arg[])throws Exception{
		
		MulticastSocket multisocket=new MulticastSocket();
		
		String msg="Hello";
		
		byte data[]=msg.getBytes();
		InetAddress addr=InetAddress.getByName("224.10.10.10");
		int port=9090;
	
		DatagramPacket packet = new DatagramPacket(data,0,data.length,addr,port); 	
		multisocket.send(packet);

		System.out.println("Packet To Group Of Computer");
	}
}