import java.net.*;
import java.io.*;

public class Client2 {
	static String messageFromServer = "";
	static boolean quitFlag= false;
	
	
  public static void main(String[] args) throws Exception {
  try{
	Socket socket=new Socket("127.0.0.1",6789);
    DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
    
    
//------------------------------------------------------------------------------------------    
        	
        	Thread sendMessage = new Thread(new Runnable() 
            {
        		String clientMessage="";
                @Override
                public void run() {
                    while (!clientMessage.equals("quit")) {
     
                    	System.out.println("Enter command :");
                      try {
						clientMessage=br.readLine();
						if(clientMessage.equals("quit"))
							quitFlag=true;
						outStream.writeUTF(clientMessage);
						outStream.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
                    }
                    }});
        	Thread readMessage = new Thread(new Runnable() 
            {
                @Override
                public void run() {

                    while (true) {
                        try {
                        	if(quitFlag){
                        		outStream.close();
        						br.close();
        						inFromServer.close();
        						socket.close();
                        		break;
                        	}
                        	else{
                        	messageFromServer = inFromServer.readLine();
                            System.out.println(messageFromServer);
                            }
                        }
                        	
                         catch (IOException e) {
     
                            e.printStackTrace();
                        }
                    }
                
                } });
        	sendMessage.start();
            readMessage.start();
  }catch(Exception e){
    System.out.println(e);
  }
  
  }
}
