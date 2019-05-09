/************************************************************************
 *  @author: SRoderick                                                  *
 *  Date: 21/12/2018                                                    *
 ***********************************************************************/
package com.roderick.steve.netsim;
import org.apache.logging.log4j.Logger; 
import org.apache.logging.log4j.LogManager;
/**
 * Test harness and main class runner
 *
 */
public class Runner {
    private static final Logger log4j = LogManager.getLogger(Runner.class.getName());
    
    /**
     * Main method to execute LPWAN Network Contention Simulator
     * Run with gateway name parameter to switch targets
     * 
     * @param args - Gateway type (Multitech / TTN)
     */
    public static void main(String args[]) {

        final int minNodes = 1;
        final int maxNodes = 100;
        final int stepSize = 30;
        final int spreadingFactor = 7;
        final int runDurationSecs = 60;
        String gateway;
      
        // determine the gateway that the simulation is targeting at
        if (args.length == 0) {
            gateway = "Multitech";
        }
        else {
            gateway = args[0];
        }
       
        System.out.println("Status, Duration, Spreading Factor, Number of Nodes");

        for (int run = minNodes; run <= maxNodes; run += stepSize) {

            ConnectionManager.totalPacketsSent = 0;
            ConnectionManager.packetsLost = 0;
            ConnectionManager.runDurationSecs = runDurationSecs;
            ConnectionManager.swamped = false;
            ConnectionManager.startTime = System.currentTimeMillis();
            ConnectionManager.numberOfNodes = run;
            ConnectionManager.spreadingFactor = spreadingFactor;
            ConnectionManager.gateway = "Multitech";

            System.out.println("Starting run gateway " + gateway + " for " + run + " nodes at spreading factor " + spreadingFactor);

            for (int i = 1; i <= run; i++) {
                log4j.trace("spooling new thread"); 
                (new Thread(new NodeThread(i, ConnectionManager.spreadingFactor))).start();
            }

            try {
                Thread.sleep((runDurationSecs + 10) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // wait for all nodes to complete
            while (NodeThread.numberOfNodes > 0) {
                System.out.println("Number of nodes:" + NodeThread.numberOfNodes);
            }

        }
    }
}
