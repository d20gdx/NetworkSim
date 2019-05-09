/************************************************************************
 *  @author: SRoderick                                                  *
 *  Date: 21/12/2018                                                    *
 ***********************************************************************/
package com.roderick.steve.netsim;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage simulated connections
 */
public class ConnectionManager {
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

        // check if target duration meet
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        if (duration > runDurationSecs) {

            System.out.println("completed run," + duration + "," + sf + "," + numberOfNodes);
            return false;
        }

        if (percentage > 80f) {

            if (!swamped) {
                System.out.println("network swamped," + duration + "," + sf + "," + numberOfNodes);
                swamped = true;
            }
            return false;
        }

        // increment channel
        if (channelOccupancy.get(channel) == null) {
            channelOccupancy.put(channel, 0);
        }

        int count = (int) channelOccupancy.get(channel);
        totalPacketsSent++;

        if (count++ == 8) {
            System.out.println("Lost packet");
            packetsLost++;
        } else {

            // successful connection
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
        System.out.println("end connection on channel:" + channel);
        int count = (int) channelOccupancy.get(channel);
        channelOccupancy.put(channel, count--);
    }

}
