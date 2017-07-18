/**
 * @authors Luke Agnew
 * 			Ammar Qureshi
 * 			Vincent Lat
 * 			Keith Tunstead 
 */

package cs.tcd.ie;

import java.util.*;
import java.net.*;

public class LinkStateRouters {

	private DijkstraAlgorithm dij = null;
	
	// list of nodes in this network
	private ArrayList<Node> networkNodes = null; 
    RoutingTable rTable = null;
    
    private Node sourceNode = null; 
    private DatagramSocket socket = null;
    boolean continueReceiving;

    public LinkStateRouters(Node self, ArrayList<Node> nodelist, NetworkTopology networkTopology) {
    	sourceNode = self;
    	networkNodes = nodelist;
    	
    	dij = new DijkstraAlgorithm(networkTopology);
    	rTable = new RoutingTable(networkNodes, dij);
    	
    	continueReceiving = true;
	
    	// Run Dijkstra's Algorithm and determine the routing table for each router 	
    	rTable.populateRoutingTable(sourceNode);  	
    }
    
    public void waitForIncomingPackets() {	
		try 
		{
		    socket = new DatagramSocket(sourceNode.getPort()); 
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		    System.out.println("Cannot create socket: " + e);
		}
	
		while(continueReceiving) {
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
    	// retrieve the packet received
	
		StringContent content = new StringContent(p);
		String[] payloadElements = content.toString().split("\0");
		
		char[] destinationChar = payloadElements[1].toCharArray();
		Node destinationNode = Node.valueOf(destinationChar[0]);
		
		// Decide which node to forward to 
		// If we are the final destination, deliver to the Server	
		Node n = rTable.lookupRoutingTable(sourceNode.getName(), destinationChar[0]);
		
		System.out.println("The message string: " + content.toString());
		System.out.println("The destination node is: " + destinationNode.getName());
		
		if (n.equals(destinationNode)) 
		{
		    // deliver packet
		    System.out.println("Reached the destination router for the packet " + n.getName());
		    deliverToHost(p);
		}
		else 
		{
		    // send the packet to the next node as indicated by our table
		    forwardToNode(n, p);
		    System.out.println("Passed packet to router " + n);
		}
    }

    private void deliverToHost(DatagramPacket packet) {
		try 
		{	
			StringContent packetContent = new StringContent(packet);
			String[] packetElements = packetContent.toString().split("\0");
			
		    InetAddress ip = InetAddress.getByName("localhost");
		    int port = 	Integer.parseInt(packetElements[2]);
		    send(ip, port, packet);
		    
		    continueReceiving = false;
		}
		catch(Exception e) {}
    }

    private void forwardToNode(Node n, DatagramPacket packet) {
    	
    	sourceNode = n;
    	socket.close();
    	
    	try 
	    {
	    	socket = new DatagramSocket(sourceNode.getPort());
	    }
	    catch(Exception e) 
	    {
			e.printStackTrace();
		    System.out.println("Cannot create socket: " + e);
		}
    	
		InetAddress ip = n.getIPAddress();
		int port = n.getPort();
		send(ip, port, packet);
    }

    private void send(InetAddress ip, int port, DatagramPacket packet) {
 
    	StringContent content = new StringContent(packet);
    	byte[] payload = content.toString().getBytes();
    	
		try 
		{
		    DatagramPacket p = 
			new DatagramPacket(payload, payload.length, ip, port);
		    socket.send(p);
		} 
		catch(Exception e) 
		{
		    System.out.println("Error sending packet: "+e);
		}	   
    }    

    public static void main(String args[]) throws Exception {
    	int nwsize = 7;
		NetworkTopology testNetwork = new NetworkTopology(nwsize);
		
		// Create the desired topology for our network of routers
		String topology = "A-B-5, A-C-4, B-D-2, C-G-1, D-E-4, E-F-1, E-G-4";
		String[] links = topology.split(", ");
		
		for(int i=0; i<links.length; i++) 
		{	
		    String[] items = links[i].split("-");
		    
		    char srcChar = items[0].charAt(0);
		    char dstChar = items[1].charAt(0);
		     
		   	int linkcost = Integer.parseInt(items[2]);
		    
		    // Add links to the network map. Note that the network map assumes 
		   	// single direction links.
		    testNetwork.addDirectRoute(Node.valueOf(srcChar), Node.valueOf(dstChar), linkcost);
		    testNetwork.addDirectRoute(Node.valueOf(dstChar), Node.valueOf(srcChar), linkcost);
		}		
		
		ArrayList<Node> nlist = new ArrayList<Node>();
		
		for(int i = 0; i < nwsize; i++) 
		{
		    nlist.add(Node.valueOf(i));
		}
		
		Node myNode = null;
		
		myNode = Node.valueOf('A');
		
		LinkStateRouters routers = new LinkStateRouters(myNode, nlist, testNetwork);
		routers.waitForIncomingPackets();			
    }
}
