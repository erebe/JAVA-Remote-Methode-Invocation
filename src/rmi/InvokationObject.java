/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void updateFields(Object old, Object young) {
        for (Field f : young.getClass().getFields()) {
            try {
                f.setAccessible(true);
                f.set(old, f.get(young));
            } catch (Exception e) {
            }
        }
    }

    public void copy(Object dest, Object source) throws IntrospectionException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
        PropertyDescriptor[] pdList = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor pd : pdList) {
            Method writeMethod = null;
            Method readMethod = null;
            try {
                writeMethod = pd.getWriteMethod();
                readMethod = pd.getReadMethod();
            } catch (Exception e) {
            }

            if (readMethod == null || writeMethod == null) {
                continue;
            }

            Object val = readMethod.invoke(source);
            writeMethod.invoke(dest, val);
        }
    }

    public Object[] remoteCall(String name, Class<?>[] cls, Object[] args, boolean isOneway) throws IOException {
        ObjectOutputStream out = getOutputStream();
        ObjectInputStream in = getInputStream();
        RemoteHeader header = new RemoteHeader();
        header.order = "CALL_METHOD";
        header.methodName = name;
        header.bindedTo = objectName;
        out.writeObject(header);
        out.writeObject(cls);
        out.writeObject(args);
        out.flush();
        
        if(isOneway) {
            close();
            return null;
        }

        Object[] rets = null;
        try {

            rets = (Object[]) in.readObject();
            close();
            return rets;
        } catch (ClassNotFoundException ex) {
            return rets;
        }
    }
}