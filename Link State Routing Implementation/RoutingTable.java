/**
 * @authors Luke Agnew
 * 			Ammar Qureshi
 * 			Vincent Lat
 * 			Keith Tunstead 
 */

package cs.tcd.ie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutingTable {
	List<Node> destinations;
	List<Node> shortestPathNodes;
	List<List<Node>> shortestPaths;
	DijkstraAlgorithm dij;
	
	public RoutingTable(List<Node> destinations, DijkstraAlgorithm dij) 
	{
		this.dij = dij;
		this.destinations = destinations;
		
		shortestPaths = new ArrayList<List<Node>>();
	}

	public void populateRoutingTable(Node sourceNode) {
		// calculate shortest path to every other node in the network
		
		for (int i = 0; i < destinations.size(); i++) {
		
			//dij.execute(sourceNode, null);
			dij.execute(destinations.get(i), null);
		
			// for every destination except myself, I should determine
			// the output link (i.e., to which of my direct neighbors
			// I should forward the message to for the destination)
			
			shortestPathNodes = new ArrayList<Node>();
			
			for (Node dest : destinations) 
			{
				List<Node> shortestPath = getShortestPathToNode(destinations.get(i), dest);
				
				Node shortestPathNode = null;	
				
				if (shortestPath.size() > 1)
				{
					shortestPathNode = shortestPath.get(1);
				}
				else
				{
					shortestPathNode = shortestPath.get(0);
				}
				
				if (shortestPathNode != null) 
				{
					shortestPathNodes.add(shortestPathNode);
				}
			}
			
			shortestPaths.add(shortestPathNodes);
		}
    }

    // this method is used to complete the populateRoutingTable()
    // method
    public List<Node> getShortestPathToNode(Node sourceNode, Node n) {
		List<Node> l = new ArrayList<Node>();
		for(Node c = n; c != null; c = dij.getPredecessor(c))
		    l.add(c);
		Collections.reverse(l);
		System.out.println("path from " + sourceNode + " to " + n + ": " + l);
	
		return l;
    }
    
    // This method should return the Node to which the packet 
    // should be forwarded to, based on the destination address
    // included in the packet.
    // 
    // If the destination is this node itself, the self node
    // should be returned
    //
    public Node lookupRoutingTable(char sourceChar, char destinationChar) {
    	boolean foundSrc = false;
    	boolean foundDest = false;
    	
    	int indexOne = 0;
    	Node src = destinations.get(indexOne);
    	
    	while (!foundSrc && indexOne < destinations.size())
    	{
    		src = destinations.get(indexOne);
    		
    		if (src.getName() == sourceChar)
    		{
    			foundSrc = true;
    		}
    		else
    		{
    			indexOne++;
    		}
    	}
    	
    	int indexTwo = 0;
    	Node dest = destinations.get(indexTwo);
    	
    	while (!foundDest && indexTwo < destinations.size()) 
    	{
    		dest = destinations.get(indexTwo);
    		
    		if (dest.getName() == destinationChar) 
    		{
    			foundDest = true;
    		}
    		else
    		{
    			indexTwo++;
    		}
    	}
   
    	Node shortestPathNode = shortestPaths.get(indexOne).get(indexTwo);
   
    	return shortestPathNode;
    }
}
