import java.net.*;
import java.io.*;
class MulticastFileRececiver extends Thread{
	
	MulticastSocket ds;
	DatagramPacket dp;
	
	public MulticastFileRececiver(int port){
		
		try{
			ds=new MulticastSocket(port);
			saveFile();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void saveFile()throws Exception{
		
		InetAddress addr=InetAddress.getByName("224.10.10.10");
		ds.joinGroup(addr);
		
		byte[] dataPacket=new byte[1000];
		dp=new DatagramPacket(dataPacket,dataPacket.length);
		ds.receive(dp);
		
		String header = new String(dp.getData(), 0, dp.getLength());
		java.util.StringTokenizer tokens=new java.util.StringTokenizer(header,":");
		
		String fileName = tokens.nextToken();
		int packetSize = Integer.parseInt(tokens.nextToken());						
		double totalPackets = Double.parseDouble(tokens.nextToken());
		
		BufferedOutputStream fis = new BufferedOutputStream(new FileOutputStream("ali.jpg"));	
		for(double i=0; i<=totalPackets; i++){
	
			dataPacket=new byte[packetSize];
			dp=new DatagramPacket(dataPacket,0,dataPacket.length);
			
			ds.receive(dp);
			fis.write(dataPacket,0,dataPacket.length);
			System.out.println("Packet: "+(i+1));
		}
	}
	
	public static void main(String arg[]) {
		
		new MulticastFileRececiver(8080);
	}
}