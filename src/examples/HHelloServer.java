/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import rmi.classimpl.HelloServer;
import java.io.IOException;

/**
 *
 * @author erebe
 */
public class HHelloServer implements HelloServer {

    @Override
    public void sayHello() throws IOException {
        System.out.println("Hello World");
    }

    @Override
    public int say(String msg) throws IOException {
        
        System.out.println("Salut à toi" + msg);
      
        return 0;
    }

    @Override
    public void sayTo(String msg, String dest) throws IOException {
      
    }
    
}
