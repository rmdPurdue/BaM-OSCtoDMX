/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcessingAlgorithms;

import DMXInterfaces.*;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 * @author Rich Dionne <rdionne@purdue.edu>
 */
public class OnetoOne /*implements Runnable*/ {
    
//    private final Map oscData;
//    private final Map universeA;
//    private final EnttecDMXPro dmxPro;
//    private final ReadWriteLock lock;

    /**
     *
     * @param oscData
     * @param universeA
     * @param lock
     * @param dmxPro
     */
/*
    public OnetoOne(Map oscData, Map universeA, ReadWriteLock lock, EnttecDMXPro dmxPro) {
        this.oscData = oscData;
        this.universeA = universeA;
        this.dmxPro = dmxPro;
        this.lock = lock;
    }
  */  
    /**
     *
     */

    public int[] getLevels(Map oscData, Map universeA) {
        int[] levels = new int[512];
        for(int i = 0; i < 512; i++) {
            String url = "";
            String data = "";
            int level = 0;
            if(universeA.get(i+1)!=null) url = (String)universeA.get(i+1);
                if(null!=oscData.get(url)) {
                    data = oscData.get(url).toString();
                }
            if(!"".equals(data)) {
                String[] parts = data.split("\\.");
                if(parts.length>0) {
                    level = Integer.parseInt(parts[0]);
                }
            }
            levels[i] = level;
        }
        return levels;
    }
    
/*    @Override
    public void run() {
        while(true) {
            for(int i = 0; i < 512; i++) {
                String url = "";
                String data = "";
                int level = 0;
                if(universeA.get(i+1)!=null) url = (String)universeA.get(i+1);
                lock.readLock().lock();
                try {
                    if(null!=oscData.get(url)) {
                        data = oscData.get(url).toString();
                    }
                } finally {
                    lock.readLock().unlock();
                }
                if(!"".equals(data)) {
                    String[] parts = data.split("\\.");
                    if(parts.length>0) {
                        level = Integer.parseInt(parts[0]);
                    }
                }
                dmxPro.set(i+1,level);
            }
        }
    }
*/
}
