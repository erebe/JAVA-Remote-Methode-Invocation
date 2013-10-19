/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import rmi.classimpl.HelloServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public String say(List<String> msg) throws IOException {
        
        msg.add("Toto");
        return "Salut à toi " + msg.get(0);
      

    }

    @Override
    public void sayTo(String msg, String dest) throws IOException {
      
    }
    
}
