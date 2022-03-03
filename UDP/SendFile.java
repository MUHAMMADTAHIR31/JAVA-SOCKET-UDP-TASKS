import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class SendFile extends JFrame {
	public static int SEND=0;
	int mode;
	File f;
	SendFile pt;
	InetAddress hostip;
	long filesize;
	DatagramSocket sock;
	JLabel img=new JLabel("",SwingConstants.CENTER);
	JProgressBar jpb=new JProgressBar();
	byte[] b;
	SocketAddress sa;
	
	public SendFile(int mod,File fi,String ip){
			mode=mod;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pt=this;
		setSize(600,480);
		setLayout(null);
		add(jpb);
		jpb.setBounds(5,5,585,30);
		add(img);
		setResizable(false);
		img.setVerticalTextPosition(SwingConstants.CENTER);
		img.setHorizontalTextPosition(SwingConstants.CENTER);
		img.setBounds(5,40,585,410);
		img.setBorder(new EtchedBorder());
		jpb.setValue(0);
		jpb.setStringPainted(true);
		try{
		if(mod==0)
			{
			sock=new DatagramSocket(33780);
			f=fi;
			filesize=f.length();
			img.setText("<HTML><b>Waiting for a connection.<br/>IP: "+InetAddress.getLocalHost().getHostAddress()+"</b></HTML>");
			}
		else
			{
			hostip=InetAddress.getByName(ip);
			sock=new DatagramSocket();
			img.setText("<HTML><b>Trying to connect to the given IP.</b></HTML>");
			}
		MyThread.start();
		}catch(Exception ex){JOptionPane.showMessageDialog(null,ex);System.exit(0);}
	}

public static void main(String args[])
{
    File ftemp;
int i=JOptionPane.showConfirmDialog(null,"Want ro send a picture?\nYes - Send an Image\nNo - Receive an Image","Send a File?", JOptionPane.YES_NO_OPTION);
if(i==0)
    {
    JFileChooser jfc=new JFileChooser();
    jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
    FileFilter ff=new FileNameExtensionFilter("Image file","jpg","png","bmp","jpeg","gif");
    jfc.addChoosableFileFilter(ff);
    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    jfc.setMultiSelectionEnabled(false);
    jfc.setDialogTitle("Select a file to send");
    if(jfc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
        {
        ftemp=jfc.getSelectedFile();
        new SendFile(0,ftemp,"").setVisible(true);
        }
    else
        {JOptionPane.showMessageDialog(null, "File not Selected");System.exit(0);}
    }
else
    {
    String ip=JOptionPane.showInputDialog("Enter the IP of server");
    new SendFile(1,null,ip).setVisible(true);
    }

}

Thread MyThread=new Thread()
{
public void run()
{
DatagramPacket p;
String s="";
try{
if(mode==SEND)
    {
    while(s.startsWith("GETPIC")==false)
    {
    b=new byte[300];
    p=new DatagramPacket(b,300);
    sock.setSoTimeout(0);
    sock.receive(p);
    sa=p.getSocketAddress();
    s=new String(b);
    }
    img.setText("connected to:"+sa);
    s=f.getName()+":"+Long.toString(filesize)+"$$";
    p=new DatagramPacket(s.getBytes(),s.length(),sa);
    sock.send(p);
    int l,sendCount;
    boolean failed;
    sock.setSoTimeout(1000);
    FileInputStream fi=new FileInputStream(f);
    l=1;
    img.setText("Sending image");
    for(int i=0;i<filesize;)
        {
        failed=false;
        b=new byte[300];
        l=fi.read(b);
        sendCount=0;
        do
            {
            p=new DatagramPacket(b,l,sa);
            sock.send(p);
            sendCount++;
            Thread.sleep(80);
            try{
            sock.receive(p);
            s=new String(b);
            if(s.contains("ACK")==false)
                throw new Exception();
            }catch(Exception ex){failed=true;}
            }while(failed&&sendCount<5);
        if(sendCount<5)
            {
            i+=l;
            jpb.setValue(i*100/(int)filesize);
            jpb.setString(Integer.toString(jpb.getValue())+" %");
            }
        else
            {
            JOptionPane.showMessageDialog(null, "Client is not receiving");System.exit(0);
            }
        }
    fi.close();
    img.setText("Sending complete");
    }
else
    {
    s="GETPIC";
    p=new DatagramPacket(s.getBytes(),s.length(),hostip,33780);
    sock.send(p);
    b=new byte[300];
    p=new DatagramPacket(b,300);
    sock.receive(p);
    s=new String(p.getData());
    f=new File(System.getProperty("java.io.tmpdir")+"\\"+s.substring(0,s.indexOf(":")));
    filesize=Long.parseLong(s.substring(s.indexOf(":")+1,s.indexOf("$$")));
    int i,l;
    FileOutputStream fo=new FileOutputStream(f);
    b=new byte[300];
    sock.setSoTimeout(5000);
    img.setText("Receiving image");
    for(i=0;i<filesize;)
        {
        b=new byte[300];
        p=new DatagramPacket(b,300);
        sock.receive(p);
        if(p.getLength()>0)
            {
            i+=p.getLength();
            jpb.setValue(i*100/(int)filesize);
            jpb.setString(Integer.toString(jpb.getValue())+" %");
            fo.write(p.getData());
            s="ACK";
            p=new DatagramPacket(s.getBytes(),s.length(),hostip,33780);
            sock.send(p);
            }
        }
    fo.close();
    img.setIcon(new ImageIcon(new ImageIcon(f.getPath()).getImage().getScaledInstance(img.getWidth(),img.getHeight(),Image.SCALE_SMOOTH)));
    img.setText("This is the received image");
    }
}catch(Exception ex){}
}
};
}