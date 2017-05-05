/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcessingAlgorithms;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 *
 * @author Rich Dionne <rdionne@purdue.edu>
 */
public class OnetoOne implements Callable {
    
    private final Map oscData;
    private final Map universeA;

    /**
     *
     * @param oscData
     * @param universeA
     */
    public OnetoOne(Map oscData, Map universeA) {
        this.oscData = oscData;
        this.universeA = universeA;
    }
    
    /**
     *
     * @return
     */
    @Override
    public int[] call() {
        int[] levels = new int[512];
        for(int i = 0; i < 512; i++) {
            String url = "";
            int data = 0;
            if(universeA.get(i+1)!=null) url = (String)universeA.get(i+1);
            if(null!=oscData.get(url)) {
                data = (int)oscData.get(url);
                levels[i] = data;
            }
            System.out.println("URL: " + url + "DMX Slot: data->" + (i+1) + ": " + data);
        }
        return levels;
    }
}
