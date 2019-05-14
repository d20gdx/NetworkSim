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
    static final String DEFAULT_GATEWAY = "Multitech"; 
    static final int START_SF = 7; 
    static final int START_RUN_DURATION_SECS = 60; 
    static final int INCREASE_RUN_STEP_SIZE = 30; 
    /**
     * Main method to execute LPWAN Network Contention Simulator
     * Run with gateway name parameter to switch targets
     * 
     * @param args - Gateway type (Multitech / TTN)
     */
    public static void main(String args[]) {

        // set up initial simulation parameters
        SimParameters props = new SimParameters();
        props.setMinNodesInRun(1);
        props.setMaxNodesInRun(100);
        props.setIncreaseRunStepSize(INCREASE_RUN_STEP_SIZE);
        props.setRunDurationSecs(START_RUN_DURATION_SECS);
        props.setTargetGateway(args.length > 0 ? args[0] : DEFAULT_GATEWAY);
        props.setSpreadingFactor(START_SF);
        
        // output text headers in console for CSV output
        System.out.println("Status, Duration, Spreading Factor, Number of Nodes");

        for (int run = props.getMinNodesInRun(); run <= props.getMaxNodesInRun(); run += props.getIncreaseRunStepSize()) {
           
            props.setNetworkSwamped(false);
            props.setStartTime(System.currentTimeMillis());
            props.setNoNodesInRun(run);
    
            System.out.println("Starting run gateway " + props.getTargetGateway() + " for " + run + " nodes at spreading factor " + props.getSpreadingFactor());

            ConnectionManager.setProperties(props);
            
            for (int i = 1; i <= run; i++) {
                log4j.trace("spooling new thread"); 
                (new Thread(new NodeThread(i, props.getSpreadingFactor()))).start();
            }

            try {
                Thread.sleep((props.getRunDurationSecs() + 10) * 1000);
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
