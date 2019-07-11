import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server {
	
	public static ArrayList<String> usersOne = new ArrayList<String>();
	public static ArrayList<Socket> socketsOne = new ArrayList<Socket>();
	public static ArrayList<String> usersTwo = new ArrayList<String>();
	public static ArrayList<Socket> socketsTwo = new ArrayList<Socket>();

	public static ArrayList<String> MemberListResponse(){
		ArrayList<String> AllUsers = new ArrayList<String>();
		for (int i =0; i<usersOne.size();i++){
			AllUsers.add(usersOne.get(i));
		}
		for (int i =0; i<usersTwo.size();i++){
			AllUsers.add(usersTwo.get(i));
		}
		return AllUsers;
	}
	
	
	public static class ServerOne extends Server {
		
		  @SuppressWarnings("unchecked")
		public static void main(String[] args) throws Exception {
			  Thread Server1 = new Thread(new Runnable() 
	            {
				  public void run() {
			    try{
			      ServerSocket server=new ServerSocket(6789);
			      int counter=0; 
			      System.out.println("Server Started ....");  
			      while(true){
			        counter++;
			        Socket serverClient=server.accept();
			        socketsOne.add(serverClient);
			        System.out.println(" >> " + "Client No:" + counter + " started!");
			        ServerClientThreadOne sct = new ServerClientThreadOne(serverClient,counter);
			        sct.start();
			        }
			    }catch(Exception e){
			      System.out.println(e);
			    }
	            }});
			  
			  
			  
			  Thread Server2 = new Thread(new Runnable() 
	            {
				  public void run() {
					  try{
					      ServerSocket server=new ServerSocket(6790);
					      int counter=0;
					      System.out.println("Server Started ....");
					      while(true){
					        counter++;
					        Socket serverClient=server.accept();
					        socketsTwo.add(serverClient);
					        System.out.println(" >> " + "Client No:" + counter + " started!");
					        ServerClientThreadTwo sct = new ServerClientThreadTwo(serverClient,counter);
					        sct.start();
					        }
					    }catch(Exception e){
					      System.out.println(e);
					    }
				  }});

			  Server1.start();
			  Server2.start();
		  }
	}
}
