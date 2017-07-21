/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import DMXInterfaces.*;
import DataProcessingAlgorithms.OnetoOne;
import LightingDevices.*;
import com.fazecast.jSerialComm.SerialPort;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.SocketException;
import com.illposed.osc.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author richd
 */
public class OSCLightingConsoleModel extends java.util.Observable {
    
    /**
     * Variables and instances required for connecting to USB to Serial devices
     * and for communicating with the EnttecDMX Pro USB DMX interface.
     */
    EnttecDMXPro DMXPro = new EnttecDMXPro(512);
    private static String[] portList;
    public Map universeA = new HashMap();
    private final ArrayList fixtureList = new ArrayList();
    public int[] universeALevels = new int[512];
    OnetoOne algorithm = new OnetoOne();

    /**
     * Variables and instances required for OSC connectivity. Handler1 defines
     * the response to received OSC messages.
     */
    public Map<String, Object> oscData = new HashMap<>();
    OSCPortIn receiver;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    OSCListener handler1 = (java.util.Date time, OSCMessage message) -> {
        Object[] values = message.getArguments();
        oscData.put(message.getAddress(), values[0]);
        universeALevels = algorithm.getLevels(oscData, universeA);
        DMXPro.set(1, universeALevels);
        setChanged();
        notifyObservers(universeALevels);
        /*
        lock.writeLock().lock();
        try {
            oscData.put(message.getAddress(), values[0]);
        } finally {
            lock.writeLock().unlock();
        }*/
    };

    /**
     * Variables for handling background threading of DMX updates
     */
//    OnetoOne algorithm = new OnetoOne(oscData, universeA, lock, DMXPro);
//    Thread dmxThread = new Thread(algorithm, "DMX Thread");
    
    /**
     * Returns a user-readable list of connected communication port
     * devices.
     * @return 
     */
    public static String[] getCommPortList() {
        SerialPort[] ports = SerialPort.getCommPorts();
        ArrayList<String>aListPortList;
        aListPortList = new ArrayList<>();

        for(SerialPort port : ports) {
            aListPortList.add(port.getSystemPortName());
        }
        
        Object[] objPortList = aListPortList.toArray();
        String[] stringPortList = Arrays.copyOf(objPortList, objPortList.length, String[].class);
        
        return stringPortList;
    }
    
    /**
     * Checks for the presence of the Enttec DMX Pro USB interface on
     * communications port "port."
     * Returns true if the device is present.
     * @param port
     * @return 
     * @throws java.lang.InterruptedException 
     */
    public boolean checkDMXPresence(String port) throws InterruptedException {
        return DMXPro.areYouThere(port);
    }
    
    /**
     * Creates a new instance of class DMXFixture, and pushes the parameters
     * to the UI (OSCConsoleView, via notifyObservers). Triggered by user
     * click on "Add DMX Fixture" button in UI (OSCConsoleView).
     * @param type
     * @param id
     * @param attributeCount
     * @param address 
     */
    public void addDMXFixture(String type, String id, int attributeCount, int address) {
        DMXFixture fixture = new DMXFixture(address,1);
        int count = fixtureList.size();
        fixture.setType(type);
        fixture.setID(id);
        fixture.setAttributeCount(attributeCount);
        fixtureList.add(fixture);
        fixture.setNumber(count + 1);
        setChanged();
        notifyObservers(fixture);
    }
    
    public void updateDMXData(boolean started, boolean connected) throws InterruptedException, ExecutionException {
        if(started && connected) {
            DMXPro.startDMX();
            System.out.println("DMX Started.");
            //dmxThread.start();
        }
        if(!started) {
            DMXPro.stopDMX();
            System.out.println("DMX Stopped.");
            //dmxThread.interrupt();
        }
    }
    
    /**
     * Method to create the OSC connection on port "receiverPort" for
     * incoming OSC messages.
     * @param receiverPort
     * @throws java.net.SocketException
     */
    public void startOSC(int receiverPort) throws SocketException {
        receiver = new OSCPortIn(receiverPort);
    }
    
    /**
     * Method to add a listener (i.e., a URL address) to the incoming
     * OSC message handler.
     * @param address
     */
    public void addNewListener(String address) {
        receiver.addListener(address, handler1);
        lock.writeLock().lock();
        try {
            oscData.put(address, 0);
        } finally {
            lock.writeLock().unlock();
        }
        setChanged();
        notifyObservers(oscData);
    }
    
    /**
     * Method to start listening for incoming OSC messages.
     */
    public void startOSCListening() {
        receiver.startListening();
    }
    
    /**
     * Method for connecting OSC URLs with DMX address slots.
     * @param slot
     * @param url 
     */
    public void connectURL(String slot, String url) {
        int address = Integer.parseInt(slot);
        universeA.put(address,url);
    }
    
    /**
     * Method for sending an OSC URL associated with a DMX address slot
     * to the view for display to the user (if one exists).
     * @param slot 
     */
    public void setCurrentOSCConnection(String slot) {
        String url;
        int address = Integer.parseInt(slot);
        if(universeA.get(address)!=null) {
            url = (String)universeA.get(address);
        } else {
            url = "None added.";
        }
        setChanged();
        notifyObservers(url);
    }
}