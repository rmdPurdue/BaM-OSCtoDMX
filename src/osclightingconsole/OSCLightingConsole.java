/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osclightingconsole;

import MVC.OSCLightingConsoleView;
import MVC.OSCLightingConsoleModel;
import MVC.OSCLightingConsoleController;
import java.net.SocketException;

/**
 *
 * @author richd
 */
public class OSCLightingConsole {
    
    /**
     * @param args the command line arguments
     * @throws java.net.SocketException
     */
    public static void main(String[] args) throws SocketException {

        //Create Model and View
        OSCLightingConsoleModel model = new OSCLightingConsoleModel();
        OSCLightingConsoleView view = new OSCLightingConsoleView();
        
        //Create Controller and connect Model and View to it
        OSCLightingConsoleController controller = new OSCLightingConsoleController();
        controller.addModel(model);
        controller.addView(view);
        
        //Initialize the Model
        controller.initModel();
        
        //Tell Model about View
        model.addObserver(view);
        
        //Connect Controller to View
        view.addController(controller);
        
        //Start Application
        controller.startApplication();
    }
    
}
