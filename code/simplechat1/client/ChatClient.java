// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  String loginID;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.loginID = loginID;
    this.clientUI = clientUI;
    openConnection();
	this.sendToServer("#login " + loginID);

  }

  
  //Instance methods ************************************************
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
   
 //****Question 6a. Method updated to allow the user to input commands in the client side.
  public void handleMessageFromClientUI(String message)
  {
   /* try
    {
    	sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
    */
    	if (message.startsWith("#")) {
			String[] splitMessage = message.split(" ");
			String fstPart_Msg = splitMessage[0];
			switch (fstPart_Msg) {
			case "#quit":
				quit();
				break;
			case "#logoff":
				try {
					System.out.println("Connection closed.");
					closeConnection();
				} catch (IOException e) {
					System.out.println("Error closing connection...");
				}
				break;
			case "#sethost":
				if (this.isConnected()) {
					System.out.println("Can't do that now. Already connected.");
				} else {
					super.setHost(splitMessage[1]);
					System.out.println("Host set to: "+splitMessage[1]);
				}
				break;
			case "#setport":
				if (this.isConnected()) {
					System.out.println("Can't do that now. Already connected.");
				} else {
					this.setPort(Integer.parseInt(splitMessage[1]));
					System.out.println("Port set to: "+splitMessage[1]);
				}
				break;
			case "#login":
				if (this.isConnected()) {
					System.out.println("Can't do that now. Already connected.");
				} else {
					try {
						this.openConnection();
					} catch (IOException e) {
						System.out.println("Error opening connection to server. Perhaps the server is not running!");
					}
				}
				break;
			case "#gethost":
				System.out.println("Current host is " + this.getHost());
				break;
			case "#getport":
				System.out.println("Current port is " + this.getPort());
				break;
			default:
				System.out.println("Invalid command: '" + fstPart_Msg + "'");
				break;
			}
		} else {
			try {
				sendToServer(message);
			} catch (IOException e) {
				clientUI.display("Could not send message to server.  Terminating client.");
				quit();
			}
		}
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {  
    try
    {
	  clientUI.display("Quitting...");
      closeConnection();
    }
    catch(IOException e) {
	}
    System.exit(0);
  }

 //**** Changed for E5 part a:
  // Implementing the connectionClosed hook as well as the connectionException hook
  protected void connectionClosed() {
	if(isConnected())
		try {
		openConnection();
		}catch(IOException e) {}
	
  }
//**** Changed for E5 part a:
  protected void connectionException(Exception exception) {
	clientUI.display("The server has shut down."); 
	quit(); 
  }

}
//End of ChatClient class
