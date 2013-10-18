/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import idl.Stub;
import idl.Interface;
import idl.Parser;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author erebe
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            Parser parser = new Parser();
            Interface inter = parser.parse();
            System.out.println(Stub.generate(inter));
        } catch (Throwable ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
