/**
 * @authors Luke Agnew
 * 			Ammar Qureshi
 * 			Vincent Lat
 * 			Keith Tunstead 
 */

package cs.tcd.ie;

import java.util.*;
import java.net.*;
import tcdIO.*;

public class LinkStateClient {
	    private DatagramSocket socket = null;
	    private Node srcNode = null;
	    private Node dstNode = null;
	    private int dstPort;
	    boolean continueReceiving;
	    Terminal terminal;
	    
	    public LinkStateClient(Terminal terminal, int hostPort, int dstPort, Node dstNode) {
			
	    	this.terminal = terminal;
	    	int myport = hostPort;
	    	
	    	this.dstPort = dstPort;
	    	this.dstNode = dstNode;
	    	
			srcNode = Node.valueOf('A');
			continueReceiving = true;	
	
			try 
			{
			    socket = new DatagramSocket(myport); 
			}
			catch(Exception e) 
			{
			    System.out.println("Error creating socket: "+e);
			}	   
	    }

	    public void run() {
	    	
	    	String message = (terminal.readString("String to send: "));
			Random gen = new Random();
			
			String payloadElements = message + "\0" + dstNode.getName() + "\0" + dstPort 
										+ "\0" + srcNode.getName() + "\0" + gen.nextInt(1000);
			
			// Note that we are sending the packet to the router
			// that this Client is attached to, not the one that the
			// Server is attached to.
			
			sendToNode(srcNode, payloadElements);
	    }

	    private void sendToNode(Node n, String payloadElements) {
			InetAddress ip = n.getIPAddress();
			int port = n.getPort();
			byte[] payload = payloadElements.getBytes();
	
			try 
			{
			    DatagramPacket p = new DatagramPacket(payload, payload.length, ip, port);
			    socket.send(p);		    
			} 
			catch(Exception e) 
			{
			    System.out.println("Error sending packet: "+e);
			}
			
			socket.close();
	    }
	    
	    public void runEventLoop() {
			try 
			{
			    int myport = 5007;
			    socket = new DatagramSocket(myport); 
			} 
			catch(Exception e) 
			{
			    System.out.println("Cannot create socket: " + e);
			}
		
			while (continueReceiving) 
			{
			    byte[] buffer = new byte[1040]; 
			    DatagramPacket p = 
				new DatagramPacket(buffer, buffer.length);  
			    
			    try {
					socket.receive(p); 
					
				    } catch(Exception e) {
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
		
			System.out.println("Received message: " + message);
			
			continueReceiving = false;
	    }
	    
	    public static void main(String[] args) {
	    	
	    	Terminal terminal = new Terminal("Link State Client");
	    	int srcPort = 50001;
	    	int dstPort = 50002;
	    	Node dstNode = Node.valueOf('G');
	    	
	    	LinkStateClient E1 = new LinkStateClient(terminal, srcPort, dstPort, dstNode);   	
		
			E1.run();
	    }
}
