/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rmi.Service;
import rmi.classimpl.HelloServer;

/**
 *
 * @author erebe
 */
public class MainClient {
    
      public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
          
          Service.getInstance().initRMIServer("localhost", 8888);
          HelloServer test = (HelloServer) Service.getInstance().bind("rmi://localhost/HelloServer");
          
          ArrayList<String> str = new ArrayList<>();
          str.add(" Romain");
          test.say(str);
          System.out.println(str);
          
      }
}
