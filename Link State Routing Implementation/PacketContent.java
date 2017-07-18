/**
 * @authors Luke Agnew
 * 			Ammar Qureshi
 * 			Vincent Lat
 * 			Keith Tunstead 
 */

package cs.tcd.ie;

import java.net.DatagramPacket;

public interface PacketContent {
	public String toString();
	public DatagramPacket toDatagramPacket();
}
