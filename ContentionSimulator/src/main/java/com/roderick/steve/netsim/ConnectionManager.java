/************************************************************************
 *  @author: SRoderick                                                  *
 *  Date: 21/12/2018                                                    *
 ***********************************************************************/
package com.roderick.steve.netsim;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to manage simulated connections
 */
public class ConnectionManager {
    
    private static final Logger log4j = LogManager.getLogger(ConnectionManager.class.getName());
    static Map<Integer, Integer> channelOccupancy = new HashMap<Integer, Integer>();

    static int totalPacketsSent = 0;
    static int packetsLost = 0;
    static long startTime = 0;
    static long numberOfNodes = 0;
    static int spreadingFactor = 0;
    static int runDurationSecs = 0;
    static boolean swamped = false;
    static String gateway = "";

    /**
     * Instigate a new connection for a given channel and modulation rate
     * 
     * @param channel
     * @param sf
     * @param nodeId
     * @return
     */
    public static synchronized boolean makeConnection(int channel, int sf, int nodeId) {

        float percentage = 0f;
        if (totalPacketsSent != 0) {
            percentage = (float) (packetsLost * 100 / totalPacketsSent);
        }

        // Check to see if target duration is met
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        if (duration > runDurationSecs) {
            System.out.println("Completed run," + duration + "," + sf + "," + numberOfNodes);
            log4j.trace("Completed run," + duration + "," + sf + "," + numberOfNodes); 
            return false;
        }

        // Calculate the percentage of packets loss. Assume network 
        // congestion at levels of 80% loss and above. Cease to send
        // further connection requests
        if (percentage > 80f) {
            if (!swamped) {
                log4j.trace("Network congestion detected," + duration + "," + sf + "," + numberOfNodes); 
                System.out.println("Network congestion detected," + duration + "," + sf + "," + numberOfNodes);
                swamped = true;
            }
            return false;
        }

        // increment channel occupancy count
        if (channelOccupancy.get(channel) == null) {
            channelOccupancy.put(channel, 0);
        }

        int count = (int) channelOccupancy.get(channel);
        totalPacketsSent++;

        // determine if all channels are currently occupied
        // if not then spawn new connection threads
        if (count++ == 8) {
            log4j.trace("Packet loss detected");
            packetsLost++;
        } else {
            channelOccupancy.put(channel, count++);
            // start TimeOut Thread
            (new Thread(new ConnectionThread(gateway,channel, sf))).start();
        }
        return true;
    }

    /**
     * Destroy and clean up after connection
     * 
     * @param channel
     */
    public static synchronized void endConnection(int channel) {
        log4j.trace("Finished sending on channel:" + channel);
        int count = (int) channelOccupancy.get(channel);
        channelOccupancy.put(channel, count--);
    }

}
