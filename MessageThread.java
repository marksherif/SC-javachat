import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MessageThread implements Runnable{
 String server;
 String source;
 String destination;
 int TTL;
 String message;
 DataOutputStream outstream;

 
 public MessageThread(String server, String source,String destination, int TTL, String message, DataOutputStream outstream){
   this.server = server;
   this.source = source;
   this.destination = destination;
   this.TTL = TTL;
   this.message = message;
   this.outstream = outstream;
 }
 	
 public void run(){
		   try{
			   TTL--;
			   if(TTL != 0){
			   if (server.equals("ServerOne")){
				   if(Server.usersTwo.contains(destination)){
			    	   int indexOfDestinationSocket = 0;
				    	   for(int i =0; i< Server.usersTwo.size(); i++){
				    		   if(Server.usersTwo.get(i).equals(destination)){
				    			   indexOfDestinationSocket = i;
				    			   i = Server.usersTwo.size();
				    		   } 
				    	   }
				    	   Server.socketsTwo.get(indexOfDestinationSocket).getOutputStream().write((message + System.lineSeparator()).getBytes());
				    	   Server.socketsTwo.get(indexOfDestinationSocket).getOutputStream().flush();
				    	   outstream.writeBytes("Message Sent" +'\n');
				    	   outstream.flush();
				    	   Thread.currentThread().interrupt();
			    	}
				   else{
					   Runnable mt = new MessageThread("ServerTwo", source, destination, TTL, message, outstream);
		    		   new Thread(mt).start();
				   }
				   
			   }
			   else{
				   if(Server.usersOne.contains(destination)){
			    	   int indexOfDestinationSocket = 0;
				    	   for(int i =0; i< Server.usersOne.size(); i++){
				    		   if(Server.usersOne.get(i).equals(destination)){
				    			   indexOfDestinationSocket = i;
				    			   i = Server.usersOne.size();
				    		   } 
				    	   }
				    	   Server.socketsOne.get(indexOfDestinationSocket).getOutputStream().write((message + System.lineSeparator()).getBytes());
				    	   Server.socketsOne.get(indexOfDestinationSocket).getOutputStream().flush();
				    	   outstream.writeBytes("Message Sent" +'\n');
				    	   Thread.currentThread().interrupt();
			    	}
				   else{
					   Runnable mt = new MessageThread("ServerOne", source, destination, TTL, message, outstream);
		    		   new Thread(mt).start();
				   }
			   }
				   

		   }
			   else{
				   outstream.writeBytes("Message timeout" +'\n');
			   	   outstream.flush();
			   	   
			   }
		   }catch(Exception ex){
		     System.out.println(ex);
		 }
	 }
}
