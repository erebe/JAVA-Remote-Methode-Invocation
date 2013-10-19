/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rmi.RemoteThread.Future;
import rmi.Service;
import rmi.classimpl.IHelloServer;

/**
 *
 * @author erebe
 */
public class MainClient {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        Service.getInstance().initRMIServer("localhost", 8888);
        IHelloServer test = (IHelloServer) Service.getInstance().bind("rmi://localhost/HelloServer");

        ArrayList<String> str = new ArrayList<>();
        str.add("Romain");
        Future<String> f = test.asyncSay(str);

        for (int i = 1; i < 6; i++) {
            System.out.println(i);
            Thread.sleep(1000);
        }

        System.out.println(f.get());
        System.out.println(f.get());
        System.out.println(str);



    }
}
