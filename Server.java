import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
class Server {
	 
	 ServerSocket server;
	 Socket socket;
	 BufferedReader br;
	 PrintWriter out;
	 
	 public Server()
	 {
		 try {
			 server=new ServerSocket(1111);
			 System.out.println("Server is ready to accept connection");
			 System.out.println("Waiting....");
			 socket=server.accept();
			 
			 br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 out=new PrintWriter(socket.getOutputStream());
			 startReading();
			 startWriting();
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
			
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
					 System.out.println("Client terminated chat...."); 
					 socket.close();
					 break;
				 }
				 System.out.println("Client :"+msg);
				 
			 }
			 }catch(Exception e) {
				// e.printStackTrace();
				 System.out.println("Connection is closed");
			 }
			 
		 };
		 new Thread(r1).start();
	 }
	 public void startWriting()
	 {
		 Runnable r2=()->{
			 System.out.println("Writer Started");
			 
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
				//e.printStackTrace();
			}
			 System.out.println("Connection is closed");
			 
		 };
		 new Thread(r2).start();
	 }
	 
	public static void main(String[] args) {
		System.out.println("This is server.. going to start Server");
	    new Server();
	   	}
	 
}
