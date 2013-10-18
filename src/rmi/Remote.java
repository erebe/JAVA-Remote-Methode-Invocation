/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public abstract class Remote implements Serializable {

    private InvokationObject inv = new InvokationObject();

    public InvokationObject getRemote() {
        return inv;
    }

    public void setRemote(InvokationObject obj) {
        inv = obj;
    }
    
}