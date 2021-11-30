import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame{
	Socket socket;
	BufferedReader br;
	 PrintWriter out;

	 private JLabel heading=new JLabel("Client Area");
	 private JTextArea messageArea=new JTextArea();
	 private JTextField messageInput=new JTextField();
	 private Font font=new Font("Roboto",Font.BOLD,20);
	 
	 public Client()
	{
		try {
			
			System.out.println("Sending request to Server");
			socket=new Socket("127.0.0.1",1111);
			System.out.println("Connection Done.");
			 br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 out=new PrintWriter(socket.getOutputStream());
			createGUI();
		    handleEvents();
			 startReading();
			 //startWriting();
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	 private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {

			@Override 
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("key released "+e.getKeyCode());
				if(e.getKeyCode()==10)
				{
					//System.out.println("You have pressed Enter Button");
					String contentToSend=messageInput.getText();
					messageArea.append("Me :"+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
		});	
		
	}
	private void createGUI()
	 {
		this.setTitle("Client Messenger[END]");
		this.setSize(700,500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//component ki coading
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setIcon(new ImageIcon("chaticon.png"));
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		//frame ka layout set kiya
		this.setLayout(new BorderLayout());
		
		//component ko frame se jodne ki coading
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane=new JScrollPane(messageArea);
		this.add(jScrollPane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		
		this.setVisible(true);
		
	 }
	 
	 public void startReading()
	 {
		 Runnable r1=()->{
			 System.out.println("Reader Started");
			 try {
				 while(true) {
				 
				 String msg=br.readLine();
				 if(msg.equals("quite")&&msg.equals("exit")&&msg.equals("close"))
				 {
					 System.out.println("Server terminated chat...."); 
					 JOptionPane.showMessageDialog(this, "Server Terminated the chat");
					 messageInput.setEnabled(false);
					 socket.close();
					 break;
				 }
				// System.out.println("Server :"+msg);
				 messageArea.append("Server :"+msg+"\n");
				 
			 }
			 }catch(Exception e) {
				 //e.printStackTrace();
				 System.out.println("Connection is closed");
			 }
			 
		 };
		 new Thread(r1).start();
	 }
	 public void startWriting()
	 {
		 Runnable r2=()->{
			 System.out.println("Writer Started...");
			 try {
				 while(!socket.isClosed())
			 {
				 
					 BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
					 String content=br1.readLine();
					 out.println(content);
					 out.flush();
					 if(content.equals("exit"))
					 {
						socket.close();
						break; 
					 }
				 
			 }
				 System.out.println("Connection is closed");	 
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
			 
		 };
		 new Thread(r2).start();
	 }

	public static void main(String[] args) {
		System.out.println("This is Client");
		new Client();

	}

}
