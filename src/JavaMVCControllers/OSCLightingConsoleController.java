/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaMVCControllers;

import DataProcessingAlgorithms.OnetoOne;
import JavaMVCViews.*;
import JavaMVCModels.*;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author richd
 */
public class OSCLightingConsoleController implements ActionListener{
    
    private static String[] portList;
    private String dmxPort;
    private OSCLightingConsoleView view;
    private OSCLightingConsoleModel model;
    private boolean dmxStarted = false;
    private boolean dmxConnected = false;
    
    public void OSCLightingConsoleController(OSCLightingConsoleView view, OSCLightingConsoleModel model) {
        this.view = view;
        this.model = model;
    }
    
    public void startApplication() throws SocketException {
        portList = OSCLightingConsoleModel.getCommPortList();
        String[] errorList = {"None found."};
        if(portList.length > 0) {
            view.setCommPortList(portList);
        } else {
            view.setCommPortList(errorList);
        }
        view.setVisible(true);
        
        model.startOSC(8000);
        model.startOSCListening();
    }
    
    public void connectDMX(String port) throws InterruptedException {
        if(model.checkDMXPresence(port)) {
            view.displayDMXStatus("Connected");
            model.setDMXCommPort(port);
            dmxConnected = true;
        } else {
            view.displayDMXStatus("Could not find DMXPro.");
            dmxConnected = false;
        }
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent event) {
        String command;
        command = event.getActionCommand();
        if("Connect DMX".equals(command)) {
            String selectedPort = (String)view.commPort.getSelectedItem();
            if("None found.".equals(selectedPort)) {
                view.displayDMXStatus("No Ports Found.");
            } else {
                try {
                    connectDMX((String) view.commPort.getSelectedItem());
                } catch (InterruptedException ex) {
                    Logger.getLogger(OSCLightingConsoleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if("Start DMX".equals(command)) {
            dmxStarted = true;
            while(dmxStarted && dmxConnected) {
                try {
                    model.updateDMXData();
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(OSCLightingConsoleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if("Stop DMX".equals(command)) {
            dmxStarted = false;
        }
        if("Add DMX Fixture".equals(command)) {
            String type = view.dmxFixtureType.getText();
            String id = view.dmxFixtureID.getText();
            int startAddress = Integer.parseInt(view.dmxFixtureStartAddress.getText());
            int attributeCount = Integer.parseInt(view.dmxFixtureAttributeCount.getText());
            model.addDMXFixture(type,id,attributeCount,startAddress);
            view.dmxFixtureType.setText("");
            view.dmxFixtureID.setText("");
            view.dmxFixtureStartAddress.setText("");
            view.dmxFixtureAttributeCount.setText("");
        }
        if("Add URL".equals(command)) {
            String URL = view.newOSCURL.getText();
            model.addNewListener(URL);
            view.newOSCURL.setText("");
        }
        if("Add Connection".equals(command)) {
            String connection = (String)view.oscURLPicker.getSelectedItem();
            String slot = (String)view.dmxAddressPicker.getSelectedItem();
            model.connectURL(slot, connection);
            view.dmxOSCConnection.setText(connection);
        }
        if("comboBoxChanged".equals(command)) {
            String slot = (String)view.dmxAddressPicker.getSelectedItem();
            model.setCurrentOSCConnection(slot);
        }
    }
    
    /*
    public boolean addDMXFixture(String type, String name, int address) {
        model.addDMXFixture(type, name, address);
        view.addFixtureToList(type, name, address);
        return true;
    }
*/
    
    public void addModel(OSCLightingConsoleModel model) {
        this.model = model;
    }
    
    public void addView(OSCLightingConsoleView view) {
        this.view  = view;
    }
    
    public void initModel() {
        
    }
}
