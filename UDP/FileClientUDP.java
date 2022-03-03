import java.io.*;
import java.net.*;
class FileClientUDP{
	
	DatagramSocket ds;
	DatagramPacket dp;
	
	public  FileClientUDP( String host, int port, String file){
		try{
			
			this.ds = new DatagramSocket();
			sendFile(host,port,file);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void sendFile(String host,int port,String filePath)throws Exception {
		
		BufferedInputStream file = new BufferedInputStream(new FileInputStream(filePath));
	
		int fileSize=file.available();
		int packetSize=1000;
		double totalPacket=Math.ceil(((int)fileSize)/packetSize);
		
		//int lastPacketSize=fileSize%packetSize;
		//System.out.println(lastPacketSize);
		
		System.out.println(totalPacket);
		InetAddress SERVER_ADDRESS = InetAddress.getLocalHost();
		
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
		new FileClientUDP("localhost",8080,"cat.jpg");
	}
}