/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LightingDevices;

/**
 *
 * @author richd
 */
public class DMXFixture {
    private String type;
    private String id;
    private int number;
    private int startAddress;       // The DMX slot/address of the first attribute of the fixture
    private int attributeCount;     // The number of controllable attributes of the fixture
    private int[] attributeLevels;  // An array to hold attribute levels for the fixture

    public DMXFixture(int address, int count) {
        startAddress = address;
        attributeCount = count;
        setAttributeCount(attributeCount);
    }
    
    public void setType(String newValue) {
        type = newValue;
    }
    
    public void setID(String newValue) {
        id = newValue;
    }
    
    public void setNumber(int newValue) {
        number = newValue;
    }
    
    public void setStartAddress(int newValue) {
        startAddress = newValue;
    }
    
    public void setAttributeCount(int newValue) {
        attributeCount = newValue;
        attributeLevels = new int[attributeCount];
    }
    
    public void setAttributeLevel(int attributeNumber, int level) {
        attributeLevels[attributeNumber-1] = level;
    }
    
    public String getID(){
        return id;
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getType() {
        return type;
    }
    
    public int getStartAddress() {
        return startAddress;
    }
    
    public int getAttributeCount() {
        return attributeCount;
    }
    
    public int getAttributeLevel(int attributeNumber) {
        return attributeLevels[attributeNumber-1];
    }
    
    public int getAttributeAddress(int attributeNumber) {
        return attributeNumber-1+startAddress;
    }
}
