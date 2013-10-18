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

public class InvokationObject implements Serializable {

    protected String host = null;
    protected int portDest = 0;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    public Socket channel = null;
    public String objectName = null;

    private void connectTo() throws IOException {
        if (channel != null) {
            return;
        }

        System.out.println("Binding the socket" + host + " : " + portDest);
        channel = new Socket(host, portDest);
        channel.setSoTimeout(10000);
    }

    public void close() throws IOException {
        channel.close();
        out.flush();
        out.close();
        in.close();

        out = null;
        in = null;
        channel = null;
    }

    public ObjectOutputStream getOutputStream() throws IOException {
        connectTo();
        if (out == null) {
            out = new ObjectOutputStream(channel.getOutputStream());
        }
        return out;
    }

    public ObjectInputStream getInputStream() throws IOException {
        connectTo();
        if (in == null) {
            in = new ObjectInputStream(channel.getInputStream());
        }

        return in;
    }
}