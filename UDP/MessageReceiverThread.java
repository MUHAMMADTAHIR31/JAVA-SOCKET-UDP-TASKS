import java.io.*;
import java.net.*;

class MessageReceiverThread extends Thread{
	
	DatagramSocket ds;
	DatagramPacket dp;
	
	
	MessageReceiverThread(DatagramSocket ds){
		
		this.ds=ds;
	}
	
	public void run(){
		byte[] receiveBytes = new byte[256];
		do{     
		   try{ 
				dp= new DatagramPacket(receiveBytes, receiveBytes.length);
				ds.receive(dp);
				String received = new String(dp.getData(), 0, dp.getLength());
				System.out.println("Client Says:" + received);

		    }catch(Exception e){
			 e.printStackTrace();
            }
		}while(true);
	}//end run
}//end thread