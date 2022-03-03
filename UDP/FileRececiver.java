import java.net.*;
import java.io.*;
class FileRececiver extends Thread{
	
	DatagramSocket ds;
	DatagramPacket dp;
	
	public FileRececiver(int port){
		
		try{
			ds=new DatagramSocket(port);
			saveFile();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void saveFile()throws Exception{
		
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
		
		new FileRececiver(8080);
	}
}