/************************************************************************
 *  @author: SRoderick                                                  *
 *  Date: 21/12/2018                                                    *
 ***********************************************************************/
package com.roderick.steve.netsim;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Connection thread simulates LPWAN connection between base station 
 * and gateway
 *
 */
public class ConnectionThread implements Runnable {

    private static final Logger log4j = LogManager.getLogger(ConnectionThread.class.getName());
    
    private float timeOnAir;
    private int channel;
    public ConnectionThread() {
        super();
    }
    
    /**
     * Constructor
     * 
     * @param channel
     * @param sf
     */
    public ConnectionThread(String gateway, int channel, int sf) {
    
        // read meta data from resources file into collection
        ClassLoader classLoader = new ConnectionThread().getClass().getClassLoader();
        File file = new File(classLoader.getResource(gateway + ".csv").getFile());
        
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = getMapFromCSV(file);
        } catch (IOException e) {
            e.printStackTrace();
        }        

        String timeOnAir = "-1f";
        if (map != null && map.containsKey(Integer.toString(sf))) {
            timeOnAir= map.get(Integer.toString(sf));
        } 
        // calculate time on air
        this.timeOnAir = Float.parseFloat(timeOnAir);
        this.channel = channel;
    }


    /**
     * Read parameters in CSV file to map
     * @param csvfile
     * @return
     * @throws IOException
     */
    public  Map<String, String> getMapFromCSV(File csvfile) throws IOException{
        log4j.trace("reading CSV config file"); 
        Stream<String> lines = Files.lines(Paths.get(csvfile.getPath()));
        Map<String, String> resultMap = 
                lines.map(line -> line.split(","))
                     .collect(Collectors.toMap(line -> line[0], line -> line[1]));
        lines.close();
        return resultMap;
    }
    
    public float getTimeOnAir() {
        return timeOnAir;
    }


    public void setTimeOnAir(float timeOnAir) {
        this.timeOnAir = timeOnAir;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }  
    
    
    @Override
    public void run() {
        try {
            // LoRaWAN connections persist until their time of air is complete
            // this is governed by duty cycle restrictions
            // could be enhanced by using distribution of actual connection times 
            // from measurement campaign
            Thread.sleep((long) Math.floor(timeOnAir * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ConnectionManager.endConnection(channel);
    }

}
