package com.roderick.steve.netsim;

/**
 * Class to represent all connection properties
 * @author sroderick
 *
 */
public class SimParameters {

    private int totalPacketsSent;
    private int packetsLost;
    private long startTime;
    private long noNodesInRun;
    private int spreadingFactor;
    private int runDurationSecs;
    private boolean networkSwamped;
    private String targetGateway;
    private int minNodesInRun;
    private int maxNodesInRun;
    private int increaseRunStepSize;
    
    
    public int getIncreaseRunStepSize() {
        return increaseRunStepSize;
    }
    public void setIncreaseRunStepSize(int increaseRunStepSize) {
        this.increaseRunStepSize = increaseRunStepSize;
    }
    public int getTotalPacketsSent() {
        return totalPacketsSent;
    }
    public void setTotalPacketsSent(int totalPacketsSent) {
        this.totalPacketsSent = totalPacketsSent;
    }
    public int getPacketsLost() {
        return packetsLost;
    }
    public void setPacketsLost(int packetsLost) {
        this.packetsLost = packetsLost;
    }
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getNoNodesInRun() {
        return noNodesInRun;
    }
    public void setNoNodesInRun(long noNodesInRun) {
        this.noNodesInRun = noNodesInRun;
    }
    public int getSpreadingFactor() {
        return spreadingFactor;
    }
    public void setSpreadingFactor(int spreadingFactor) {
        this.spreadingFactor = spreadingFactor;
    }
    public int getRunDurationSecs() {
        return runDurationSecs;
    }
    public void setRunDurationSecs(int runDurationSecs) {
        this.runDurationSecs = runDurationSecs;
    }
    public boolean isNetworkSwamped() {
        return networkSwamped;
    }
    public void setNetworkSwamped(boolean networkSwamped) {
        this.networkSwamped = networkSwamped;
    }
    public String getTargetGateway() {
        return targetGateway;
    }
    public void setTargetGateway(String targetGateway) {
        this.targetGateway = targetGateway;
    }
    public int getMinNodesInRun() {
        return minNodesInRun;
    }
    public void setMinNodesInRun(int minNodesInRun) {
        this.minNodesInRun = minNodesInRun;
    }
    public int getMaxNodesInRun() {
        return maxNodesInRun;
    }
    public void setMaxNodesInRun(int maxNodesInRun) {
        this.maxNodesInRun = maxNodesInRun;
    }
    
    
    
}
