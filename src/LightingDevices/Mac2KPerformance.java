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
public class Mac2KPerformance extends DMXFixture {
    public Mac2KPerformance(int address) {
        super(address,28);
    }
    
    public void lampOn() {
        super.setAttributeLevel(1, 228);
    }
    
    public void closeShutter() {
        super.setAttributeLevel(1,10);
    }
    
    public void openShutter() {
        super.setAttributeLevel(1,21);
    }
    
    public void resetFixture() {
        super.setAttributeLevel(1,210);
    }
    
    public void setIntensity(int level) {
        super.setAttributeLevel(2, level);
    }
    
    public void setCyan(int level) {
        super.setAttributeLevel(3, level);
    }
    
    public void setMagenta(int level) {
        super.setAttributeLevel(4,level);
    }
    
    public void setYellow(int level) {
        super.setAttributeLevel(5,level);
    }
    
    public void setIrisOpening(int level) {
        if(level < 200) {
            super.setAttributeLevel(12,level);
        } else {
            super.setAttributeLevel(12,0);
        }
    }
    
    public void setFocus(int level) {
        super.setAttributeLevel(14, level);
    }
    
    public void setZoom(int level) {
        super.setAttributeLevel(15, level);
    }
    
    public void setPan(int level) {
        super.setAttributeLevel(25,level);
    }
    
    public void setTilt(int level) {
        super.setAttributeLevel(26,level);
    }
}
