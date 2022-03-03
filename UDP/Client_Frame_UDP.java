import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Client_Frame_UDP extends JFrame implements ActionListener {
	
	// Frame Setting
	JTextField port_textField  =  new JTextField("9090");
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
	
	Client_Frame_UDP(DatagramSocket ds,DatagramPacket dp)throws Exception{
		
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
	
	public void  receive()
	
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
			
			InetAddress SERVER_ADDRESS = InetAddress.getLocalHost();
			int SERVER_PORT = 8080;
			
			System.out.print("You Says:");
			String input = textfield.getText();
			
			// Construct Datagram packet to send message
			dp= new DatagramPacket(input.getBytes(), input.getBytes().length,
			SERVER_ADDRESS, SERVER_PORT);
			ds.send(dp);
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	public static void main(String arg[])throws Exception{
		
		byte[] receiveBytes = new byte[255];
		DatagramSocket ds = new DatagramSocket(); 
		DatagramPacket dp = new DatagramPacket(receiveBytes, receiveBytes.length);
		
		//MessageReceiverThread ob = new MessageReceiverThread(ds);
		//ob.start();
		
		Client_Frame_UDP frame = new Client_Frame_UDP(ds,dp);
	}
}