import java.io.*;
import java.net.*;
class MulticastFileClientUDP{
	
	MulticastSocket ds;
	DatagramPacket dp;
	//MulticastSocket multisocket=new MulticastSocket();
	public MulticastFileClientUDP( int port, String file){
		try{
			
			this.ds = new MulticastSocket();
			sendFile(port,file);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void sendFile(int port,String filePath)throws Exception {
		
		BufferedInputStream file = new BufferedInputStream(new FileInputStream(filePath));
	
		int fileSize=file.available();
		int packetSize=1000;
		double totalPacket=Math.ceil(((int)fileSize)/packetSize);
		
		//int lastPacketSize=fileSize%packetSize;
		//System.out.println(lastPacketSize);
		
		System.out.println(totalPacket);
		InetAddress SERVER_ADDRESS=InetAddress.getByName("224.10.10.10");
		
		String header = filePath+":"+packetSize+":"+totalPacket;//+":"+lastPacketSize;
		System.out.println(header);
		
		byte[] data=header.getBytes();
		dp=new DatagramPacket(data,data.length,SERVER_ADDRESS,port);
		ds.send(dp);
		
		data=new byte[packetSize];
		for(double i=0; i<=totalPacket; i++){
			
			file.read(data,0,data.length);
			System.out.println("Packet: "+(i+1));
			dp=new DatagramPacket(data,0,data.length,SERVER_ADDRESS,port);
			ds.send(dp);
		}
	}
	
	public static void main(String arg[]){
		new MulticastFileClientUDP(8080,"cat.jpg");
	}
}