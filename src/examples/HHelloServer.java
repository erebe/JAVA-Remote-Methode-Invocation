/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import rmi.classimpl.IHelloServer;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi.RemoteThread;

/**
 *
 * @author erebe
 */
public class HHelloServer implements IHelloServer {

    @Override
    public void sayHello() throws IOException {
        System.out.println("Hello World");
    }

    @Override
    public String say(List<String> msg) throws IOException {
        
        msg.add("Toto");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HHelloServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Salut à toi " + msg.get(0);
      

    }

    @Override
    public void sayTo(String msg, String dest) throws IOException {
      
    }

    @Override
    public void fillMe(List<String> msg) throws IOException {
       msg.add("Salut");
       msg.add("a toi");
       msg.add("Romain");
    }

    @Override
    public void asyncSayHello() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RemoteThread.Future<String> asyncSay(List<String> msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void asyncSayTo(String msg, String dest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void asyncFillMe(List<String> msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
