import java.net.*;
class MultiCastReceiver{
	public static void main(String arg[])throws Exception{
		
		MulticastSocket multisocket=new MulticastSocket(9090);
		InetAddress addr=InetAddress.getByName("224.10.10.10");
		multisocket.joinGroup(addr);
		
		byte data[] = new byte[255];
		DatagramPacket packet = new DatagramPacket(data,0,data.length); 
		multisocket.receive(packet);		
		
		data=packet.getData();
		int len=packet.getLength();
		
		String msg = new String(data,0,len);
		System.out.println(msg);
	}
}