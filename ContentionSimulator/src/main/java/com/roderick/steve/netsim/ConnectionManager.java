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
    private static Map<Integer, Integer> channelOccupancy = new HashMap<Integer, Integer>();
    private static SimParameters props;

    /**
     * Set the properties for connection manager
     * 
     * @param properties an object holding all the properties required.
     */
    public static void setProperties(SimParameters properties) {
        props = properties;
    }

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
        if (props.getTotalPacketsSent() != 0) {
            percentage = (float) (props.getPacketsLost() * 100 / props.getTotalPacketsSent());
        }

        // Check to see if target duration is met
        long duration = (System.currentTimeMillis() - props.getStartTime()) / 1000;
        if (duration > props.getRunDurationSecs()) {
            System.out.println("Completed run," + duration + "," + sf + "," + props.getNoNodesInRun());
            log4j.trace("Completed run," + duration + "," + sf + "," + props.getNoNodesInRun());
            return false;
        }

        // Calculate the percentage of packets loss. Assume network
        // congestion at levels of 80% loss and above. Cease to send
        // further connection requests
        if (percentage > 80f) {
            if (!props.isNetworkSwamped()) {
                log4j.trace("Network congestion detected," + duration + "," + sf + "," + props.getNoNodesInRun());
                System.out
                        .println("Network congestion detected," + duration + "," + sf + "," + props.getNoNodesInRun());
                props.setNetworkSwamped(true);
            }
            return false;
        }

        // increment channel occupancy count
        if (channelOccupancy.get(channel) == null) {
            channelOccupancy.put(channel, 0);
        }

        int count = (int) channelOccupancy.get(channel);
        int totalPacketsSent = props.getTotalPacketsSent();
        props.setTotalPacketsSent(totalPacketsSent++);

        // determine if all channels are currently occupied
        // if not then spawn new connection threads
        if (count++ == 8) {
            log4j.trace("Packet loss detected");
            int totalPacketsLost = props.getPacketsLost();
            props.setPacketsLost(totalPacketsLost++);
        } else {
            channelOccupancy.put(channel, count++);
            // start TimeOut Thread
            (new Thread(new ConnectionThread(props.getTargetGateway(), channel, sf))).start();
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
