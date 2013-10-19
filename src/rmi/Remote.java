/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.Serializable;

public abstract class Remote implements Serializable {

    private InvokationObject inv = new InvokationObject();

    public InvokationObject getRemote() {
        return inv;
    }

    public void setRemote(InvokationObject obj) {
        inv = obj;
    }
    
}