/************************************************************************
 *  @author: SRoderick                                                  *
 *  Date: 21/12/2018                                                    *
 ***********************************************************************/
package com.roderick.steve.netsim;

import java.util.Random;

/**
 * Class representing an end user LoraWAN node.
 * Each node is capable of spooling connections. However the 
 * duty cycle must adhere to LoraWAN Specifications (1.1)
 * https://lora-alliance.org/resource-hub/lorawantm-specification-v11
 *
 */
public class NodeThread implements Runnable {
    static int numberOfNodes = 0;
    int nodeId;
    int sf;

    /**
     * Constructor, spawn new node thread
     * @param nodeId
     * @param sf
     */
    public NodeThread(int nodeId, int sf) {
        this.sf = sf;
        this.nodeId = nodeId;

    }

    @Override
    public void run() {
        boolean continueThread = true;
        numberOfNodes++;
        while (continueThread) {

            // Assign random LoraWAN channel, assuming 8 multi-mode channel
            // Target gateway: 
            Random r = new Random();
            int min = 1;
            int max = 8;
            int channel = r.nextInt((max - min) + 1) + min;

            // insert a random offset delay
            Random r2 = new Random();
            int min2 = 1;
            int max2 = 10;
            int random_delay = r2.nextInt((max2 - min2) + 1) + min2;
            try {
                Thread.sleep(random_delay * 1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            continueThread = ConnectionManager.makeConnection(channel, sf, nodeId);
        }
        numberOfNodes--;
    }
}
