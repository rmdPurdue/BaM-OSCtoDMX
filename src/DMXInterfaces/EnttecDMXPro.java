package DMXInterfaces;

import com.fazecast.jSerialComm.*;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author richd
 */
public class EnttecDMXPro extends Thread {
    
    private byte[] dmxProMessage = null;
    
    private String dmxProSerialPortName = null;
    
    private int dmxProBaudRate = -1;
    
    private SerialPort dmxProSerialPort;
    
    private int universeSize = 512;
    
    private long refreshDelay = Long.MAX_VALUE;
    
    private long sleepDelay = 1;
    
    private long lastSend = System.currentTimeMillis();
    
    private boolean needSend = true;
    
    private boolean buffered = true;
    
    private boolean presence = false;
    
    public EnttecDMXPro(int channels) {
        universeSize = channels;
    }
    
    public void setupDMXPro(String portName, int baudRate) {
        int dataSize = universeSize;
        dataSize++;
        byte[] message = new byte[universeSize + 6];
        message[0] = (byte)0x7E;
        message[1] = (byte)6;
        message[2] = (byte)(dataSize & 255);
        message[3] = (byte)((dataSize >> 8) & 255);
        message[4] = 0;
        for(int i = 0; i < universeSize; i++) {
            message[5+i] = 0;
        }
        message[universeSize + 5] = (byte)0xE7;
        dmxProMessage = Arrays.copyOf(message, message.length);
        dmxProSerialPortName = portName;
        dmxProBaudRate = baudRate;
        
        dmxProSerialPort = SerialPort.getCommPort(dmxProSerialPortName);
        dmxProSerialPort.setBaudRate(dmxProBaudRate);
        dmxProSerialPort.openPort();
    }
    
    public boolean areYouThere(String port) throws InterruptedException {
        byte[] message = new byte[5];
        message[0] = (byte)0x7E;
        message[1] = (byte)0xA;
        message[2] = (byte)0x00;
        message[3] = (byte)0x00;
        message[4] = (byte)0xE7;
        
        dmxProSerialPort = SerialPort.getCommPort(port);
        dmxProSerialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[dmxProSerialPort.bytesAvailable()];
                int numRead = dmxProSerialPort.readBytes(newData, newData.length);
                if(numRead == 9 && newData[1] == 10) {
                    presence = true;
                }
            }
        });
        dmxProSerialPort.openPort();
        dmxProSerialPort.writeBytes(message, message.length);
        Thread.sleep(500);
        return presence;
    }
    
    public void reset() {
        for(int i = 1; i <= universeSize; i++) {
            if(dmxProMessage!=null) {
                dmxProMessage[4+i] = 0;
            }
        }
    }
    
    public void set(int channel, int value) {
        set(channel, new int[]{value});
    }
    
    public void set(int channel, int[] values) {
        boolean needSend = false;
        
        for(int i = 0; i < values.length; i++) {
            if(dmxProMessage!=null) {
                if(channel + 4 + i < dmxProMessage.length) {
                    if(dmxProMessage[4 + channel + i] != (byte)values[i]) {
                        dmxProMessage[channel + 4 + i] = (byte)values[i];
                        needSend = true;
                    }
                }
            }
        }
        
        if(needSend) {
            this.sendDMXFrame();
        }
    }
    
    private void sendDMXFrame() {
        long now = System.currentTimeMillis();

        if(now-lastSend > refreshDelay || needSend || !buffered) {
            needSend = false;
            lastSend = now;
            if(dmxProMessage != null) {
                if(dmxProSerialPort!=null) {
                    dmxProSerialPort.writeBytes(dmxProMessage, universeSize+6);
                } else {
                    System.out.println("Not sending DMX frame. Serial port is null!");
                }
            }
        }
    }
}
