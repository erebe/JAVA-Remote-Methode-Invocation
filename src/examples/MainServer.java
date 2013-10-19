/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import rmi.classimpl.IHelloServer;
import java.io.IOException;
import rmi.Service;

/**
 *
 * @author erebe
 */
public class MainServer {

    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        
        Service.getInstance().initRMIServer("localhost", 8888);
        
        IHelloServer toto = new HHelloServer();
        Service.getInstance().register("HelloServer", toto);
        
        Service.getInstance().waitForEver();
        
    }
}
