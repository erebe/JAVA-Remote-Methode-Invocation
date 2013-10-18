/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.Serializable;

public class RemoteHeader implements Serializable {

    public String order;
    public String methodName;
    public String host;
    public String bindedTo;
}