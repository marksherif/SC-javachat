import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class ServerClientThreadOne extends Thread {
 Socket serverClient;
 int clientNo;
 ServerClientThreadOne(Socket inSocket,int counter){
   serverClient = inSocket;
   clientNo=counter;
 }
 public void run(){
   try{
     DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
     DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
     String clientMessage="", serverMessage="";
     while(!clientMessage.equals("quit")){
       clientMessage=inStream.readUTF();
       System.out.println("From Client-" +clientNo+ ": command is :"+clientMessage);
       if(clientMessage.startsWith("Join(") && clientMessage.endsWith(")")){
    	   String requestedUsername = clientMessage.substring(5, clientMessage.length()-1);
    	   if(Server.usersOne.contains(requestedUsername.toString()) || Server.usersTwo.contains(requestedUsername.toString())){
    		   serverMessage="From Server to Client-" + clientNo + " Username: " + requestedUsername + " is already taken";
    	       outStream.writeBytes(serverMessage +'\n');
    	       outStream.flush();
    	   }
    	   else{
    		   serverMessage="From Server to Client-" + clientNo + " Username: " + requestedUsername + " is added successfully";
    		   Server.usersOne.add(requestedUsername.toString());
    	       outStream.writeBytes(serverMessage +'\n');
    	       outStream.flush();
    	   }  
       }
       else if (clientMessage.equals("GetMemberList()")){
    	   ArrayList<String> users = Server.MemberListResponse();
    	   if(users.size()!=0){
	    	   String allUsers = "";
	    	   for (int i=0;i<users.size();i++){
	    		   allUsers= allUsers + (i+1) + ") " + users.get(i) +" ";
	    	   }
	    	   serverMessage="From Server to Client-" + clientNo + " Users registered are " + allUsers;
	    	   outStream.writeBytes(serverMessage +'\n');
    	       outStream.flush();
	    	}
	    	else{
	    		serverMessage="From Server to Client-" + clientNo + " No users signed up yet";
	    		outStream.writeBytes(serverMessage +'\n');
	    	    outStream.flush();
	    	}
       }
       else if(clientMessage.startsWith("Chat(") && clientMessage.endsWith(")")){
    	   String betweenBrackets = clientMessage.substring(5, clientMessage.length()-1);
    	   StringTokenizer st = new StringTokenizer(betweenBrackets, ",");
    	   if(st.countTokens()!=4){
    		   outStream.writeBytes("number of variables in the chat command is incorrect" +'\n');
    	       outStream.flush();
    	   }
    	   else{
    	   String source = st.nextToken();
    	   String destination = st.nextToken();
    	   String TTL = st.nextToken();
    	   String message = st.nextToken();
    	   message = "Message from " + source + ": " + message;
    	   if(Server.usersOne.contains(destination)){
    	   int indexOfSourceSocket = 0;
    	   int indexOfUser = 0;
    	   int indexOfDestinationSocket = 0;
    	   for(int i =0; i< Server.socketsOne.size(); i++){
    		   if(Server.socketsOne.get(i).equals(serverClient)){
    			   indexOfSourceSocket = i;
    			   i = Server.socketsOne.size();
    		   } 
    	   }
    	   if (Server.usersOne.contains(source)){
	    	   for(int i =0; i< Server.usersOne.size(); i++){
	    		   if(Server.usersOne.get(i).equals(source)){
	    			   indexOfUser = i;
	    			   i = Server.usersOne.size();
	    		   } 
	    	   }
    	   }
    	   else{
    		   	outStream.writeBytes("Invalid source user" +'\n');
    	   		outStream.flush();
    	   }
    	   if (Server.usersOne.contains(destination)){
	    	   for(int i =0; i< Server.usersOne.size(); i++){
	    		   if(Server.usersOne.get(i).equals(destination)){
	    			   indexOfDestinationSocket = i;
	    			   i = Server.usersOne.size();
	    		   } 
	    	   }
    	   }
    	   else{
    		   	outStream.writeBytes("Invalid destination user" +'\n');
    	   		outStream.flush();
    	   }
    	   if (indexOfSourceSocket != indexOfUser){
    	    outStream.writeBytes("You dont have permission to send from this username" +'\n');
   	   		outStream.flush();
    	   }
    	   else
    	   Server.socketsOne.get(indexOfDestinationSocket).getOutputStream().write((message + System.lineSeparator()).getBytes());
    	   Server.socketsOne.get(indexOfDestinationSocket).getOutputStream().flush();
    	   outStream.writeBytes("Message Sent" +'\n');
    	   outStream.flush();
    	   }
    	   else{
//    		   System.out.println(Integer.parseInt(TTL));
//    		   System.out.println("break");
    		   Runnable mt = new MessageThread("ServerOne", source, destination, Integer.parseInt(TTL), message, outStream);
    		   new Thread(mt).start();
    	   }
       }
       }
       else if (clientMessage.equals("quit")){
    	   int indexOfSourceSocket = 0;
    	   for(int i =0; i< Server.socketsOne.size(); i++){
    		   if(Server.socketsOne.get(i).equals(serverClient)){
    			   indexOfSourceSocket = i;
    			   i = Server.socketsOne.size();
    		   } 
    	   }
    	   Server.socketsOne.remove(indexOfSourceSocket);
    	   Server.usersOne.remove(indexOfSourceSocket);
    	   outStream.writeBytes("You have disconnected from the server" +'\n');
    	   outStream.flush();
       }
       else if (clientMessage.equals("help")){
    	   serverMessage="From Server to Client-" + clientNo + " 1) Join('your name') to join -- 2) GetMemberList() to get a list of all available users to chat with -- 3) Chat (Source, Destination, TTL, Message) -- 4) quit";
      		outStream.writeBytes(serverMessage +'\n');
      	    outStream.flush();
       }
       else{
    	serverMessage="From Server to Client-" + clientNo + " Command unknown, to view a list of all possible commands type 'help'";
   		outStream.writeBytes(serverMessage +'\n');
   	    outStream.flush();
       }
     }
     inStream.close();
     outStream.close();
     serverClient.close();
   }catch(Exception ex){
     System.out.println(ex);
   }finally{
     System.out.println("Client -" + clientNo + " exit!! ");
   }
 }
}
