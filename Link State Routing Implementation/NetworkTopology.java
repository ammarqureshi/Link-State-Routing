/**
 * @authors Luke Agnew
 * 			Ammar Qureshi
 * 			Vincent Lat
 * 			Keith Tunstead 
 */

package cs.tcd.ie;

import java.util.ArrayList;
import java.util.List;

public class NetworkTopology implements NetworkInterface
{
	private final int[][] distances;
	
	NetworkTopology(int numNodes)
	{
		distances = new int[numNodes][numNodes];
	}
	
	/**
	 * Link two nodes by a direct route with the given distance.
	 */
	public void addDirectRoute(Node start, Node end, int distance)
	{
		distances[start.getIndex()][end.getIndex()] = distance;
	}
	
	/**
	 * @return the distance between the two nodes, or 0 if no path exists.
	 */
	public int getDistance(Node start, Node end)
	{
		return distances[start.getIndex()][end.getIndex()];
	}
	
	/**
	 * @return the list of all valid destinations from the given node.
	 */
	public List<Node> getDestinations(Node node)
	{
		List<Node> list = new ArrayList<Node>();
		
		for (int i = 0; i < distances.length; i++)
		{
			if (distances[node.getIndex()][i] > 0)
			{
				list.add( Node.valueOf(i) );
			}
		}
		
		return list;
	}

	/**
	 * @return the list of all nodes leading to the given node.
	 */
	public List<Node> getPredecessors(Node node)
	{
		List<Node> list = new ArrayList<Node>();
		
		for (int i = 0; i < distances.length; i++)
		{
			if (distances[i][node.getIndex()] > 0)
			{
				list.add( Node.valueOf(i) );
			}
		}
		
		return list;
	}
	
	/**
	 * @return the transposed graph of this graph, as a new RoutesMap instance.
	 */
	public NetworkInterface getInverse()
	{
		NetworkTopology transposed = new NetworkTopology(distances.length);
		
		for (int i = 0; i < distances.length; i++)
		{
			for (int j = 0; j < distances.length; j++)
			{
				transposed.distances[i][j] = distances[j][i];
			}
		}
		
		return transposed;
	}
}
