/**
 * @authors Luke Agnew
 * 			Ammar Qureshi
 * 			Vincent Lat
 * 			Keith Tunstead 
 */

package cs.tcd.ie;

import java.net.*;
import tcdIO.*;

public class LinkStateServer {
	
	private DatagramSocket socket = null;
	Terminal terminal;
    boolean continueReceiving;

    public LinkStateServer(Terminal terminal, int hostPort) {
		
    	this.terminal = terminal;
		
		continueReceiving = true;
		
		int myport = hostPort;

		try 
		{
		    socket = new DatagramSocket(myport); 
		} 
		catch(Exception e) 
		{
		    System.out.println("Error creating socket: "+e);
		}	   
    }
    
    public void waitForIncomingPackets() {
	
		while (continueReceiving) 
		{
		    byte[] buffer = new byte[1040]; 
		    DatagramPacket p = new DatagramPacket(buffer, buffer.length);  
		    
		    try 
		    {
				socket.receive(p); 		
			} 
		    catch(Exception e) 
		    {
				System.out.println("Cannot receive from socket: "+e);
		    }
	
		    onReceipt(p);
		}	
		
		socket.close();
    }

    public void onReceipt(DatagramPacket p) {
		 	
    	StringContent content = new StringContent(p);
		String[] payloadElements = content.toString().split("\0");
	
		String message = payloadElements[0];
	
		terminal.print("Received message: " + message);
		
		continueReceiving = false;
    }
    
    public static void main(String[] args) {
    	
    	Terminal terminal = new Terminal("Link State Server");
    	LinkStateServer E2 = new LinkStateServer(terminal, 50002);
    	
    	try 
    	{
    		E2.waitForIncomingPackets();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
