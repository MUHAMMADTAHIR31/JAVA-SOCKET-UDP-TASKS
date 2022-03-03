import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Server_Frame_UDP extends JFrame implements ActionListener {
	
	// Frame Setting
	JTextField port_textField  =  new JTextField("8080");
	JTextField ip_textField    =  new JTextField("localhost");
	
	//JLabel
	JLabel port_label          =  new JLabel("Port Number");
	JLabel ip_label            =  new JLabel("IP Address");
	
	//Message Send TextField & Enter Button & Receive TextArea
	JTextField textfield       =  new JTextField();
	JButton    enterButton     =  new JButton("Enter");
	JTextArea  textarea        =  new JTextArea();
	
	//Cursor & Container
	Cursor     cur             =  new Cursor(Cursor.HAND_CURSOR);
	Container con;
	
	DatagramSocket ds ;
	DatagramPacket dp ;
	
	Server_Frame_UDP(DatagramSocket ds,DatagramPacket dp)throws Exception{
		
		this.ds=ds;
		this.dp=dp;
		
		con=this.getContentPane();
		con.setLayout(null);
		
		setBounds(100,50,400,400);
		setResizable(false);
		setVisible(true);
		setTitle(" Client ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set Bounds 
		port_label.setBounds(10  , 10,  80, 30);
		port_textField.setBounds(90  , 10,  70, 30);
		
		ip_label.setBounds(170  , 10,  90, 30);
		ip_textField.setBounds(240  , 10,  90, 30);
		
		textarea.setBounds(15  , 50,  200, 250);
		textfield.setBounds(15 , 310, 200,  30);
		enterButton.setBounds(220, 310, 70, 30);
		
		// Add In Containers
		con.add(ip_textField);
		con.add(ip_label);
		con.add(port_label);
		con.add(port_textField);
		con.add(textarea);
		con.add(textfield);
		con.add(enterButton);
		
		enterButton.setCursor(cur);	
		enterButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e){
		try{
			if(e.getSource() ==  enterButton){			
				sendPackets();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}
	
	public void sendPackets(){
		try{
			
			System.out.print("You Says:");
			String input = textfield.getText();
			
			// Construct Datagram packet to send message
			DatagramPacket sendPacket = new DatagramPacket(input.getBytes(), input.getBytes().length,
			dp.getAddress(), dp.getPort());
			ds.send(sendPacket);
			
		}catch(Exception e){
		 e.printStackTrace();
		}
	}

	public static void main(String arg[])throws Exception{
		
		byte[] receiveBytes = new byte[255];
		DatagramSocket ds = new DatagramSocket(9090); 
		DatagramPacket dp = new DatagramPacket(receiveBytes, receiveBytes.length);
		
		MessageReceiverThread ob = new MessageReceiverThread(ds);
		ob.start();
		
		Server_Frame_UDP frame = new Server_Frame_UDP(ds,dp);
	}
}