/************************************************************************
 *  @author: SRoderick                                                  *
 *  Date: 21/12/2018                                                    *
 ***********************************************************************/
package com.roderick.steve.netsim;

import java.util.Random;

/**
 * Class representing an end user LoraWAN node.
 * 
 * Each node is capable of spooling connections. However the 
 * duty cycle must adhere to LoraWAN Specifications (1.1)
 * 
 * https://lora-alliance.org/resource-hub/lorawantm-specification-v11
 */
public class NodeThread implements Runnable {
    
    static int numberOfNodes = 0;
    int nodeId;
    int sf;

    /**
     * Constructor, spawns new node thread
     * with a specified node id and Spreading Factor. 
     * ASSUMPTION: Nodes continue to send at the same modulation rate.
     * 
     * @param nodeId
     * @param sf - Spreading Factor / Modulation Rate
     */
    public NodeThread(int nodeId, int sf) {
        this.sf = sf;
        this.nodeId = nodeId;
    }

    @Override
    public void run() {
        boolean success = true;
        numberOfNodes++;
        
        // Continue creating connections until there is a connection failure
        while (success) {
            // Assign random LoraWAN channel, assuming an 8 multi-mode channel gateway
            // and even distribution across sending channels
            // NB in practice gateways may back off from certain channels due to 
            // narrow band interference.
            Random r = new Random();
            int min = 1;
            int max = 8;
            int channel = r.nextInt((max - min) + 1) + min;

            // insert a random offset delay in range 1 to 10 secs. 
            // NB The above delay was chosen to agree with sending rates seen in practice during the 
            // measurement campaign.
            int min_delay = 1;
            int max_delay = 10;
            int random_delay = r.nextInt((max_delay - min_delay) + 1) + min_delay;
            try {
                Thread.sleep(random_delay * 1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            success = ConnectionManager.makeConnection(channel, sf, nodeId);
        }
        numberOfNodes--;
    }
}
