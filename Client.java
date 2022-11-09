import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame implements ActionListener{
	static String messageFromServer = "";
	static boolean quitFlag= false;
	static boolean sendFlag= false;
	static boolean uselessFlag= true;
	static int useless = 2;
	
	
  public static void main(String[] args) throws Exception {
  // Frame
  try{
	Socket socket=new Socket("127.0.0.1",6789);
    DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
    
    
    JFrame frame = new JFrame();
    frame.setBounds(0, 0, 800, 500);
    frame.setVisible(true);
    
    JPanel chatbox = new JPanel();
    chatbox.setLayout(null);
    chatbox.setVisible(true);
	
    JButton send = new JButton("Send");
    send.setVisible(true);
    send.setBounds(620, 350, 150, 90);
    send.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ev) {
	    	sendFlag=true;
	    }
	  });
    
    JTextField input = new JTextField();
    input.setVisible(true);
    input.setBounds(0, 350, 600, 90);
    input.addKeyListener(new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode()==KeyEvent.VK_ENTER)
				sendFlag=true;
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	  });
    
    JTextArea screen = new JTextArea();
    screen.setVisible(true);
    screen.setBounds(10, 10, 750, 300);
    
    screen.setEditable(false);
    screen.setColumns(20);
    screen.setText("Welcome to the Chat Server. Type 'quit' to close.");
    screen.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(screen);
    scrollPane.setVisible(true);
    scrollPane.setBounds(10, 10, 750, 300);
    
    
    chatbox.add(send);
    chatbox.add(input);
    chatbox.add(scrollPane);
    frame.add(chatbox);
    frame.validate();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
   
//------------------------------------------------------------------------------------------    
        	
        	Thread sendMessage = new Thread(new Runnable() 
            {
        		String clientMessage="";
                @Override
                public synchronized void run() {
                	screen.append("Enter command :"+ "\n");
                    while (!clientMessage.equals("quit")) {
                    	System.out.print("");
                    	if (sendFlag == true){
                    	try {
							Thread.sleep(5);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    	screen.append("Enter command :");
                      try {
						clientMessage=input.getText();
						if(clientMessage.equals("quit"))
							quitFlag=true;
						sendFlag=false;
						screen.append(clientMessage + "\n");
						input.setText("");
						outStream.writeUTF(clientMessage);
						outStream.flush();
                      
					} catch (IOException e) {
						e.printStackTrace();
					}
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
                        	screen.append(messageFromServer+ "\n");
                            
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


@Override
public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
	
}
}
