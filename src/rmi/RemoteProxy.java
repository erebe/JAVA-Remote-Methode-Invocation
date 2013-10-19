/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author erebe
 */
public class RemoteProxy implements InvocationHandler, Serializable {

    private ArrayList<Object[]> actions = new ArrayList<>();
    private Object proxied;

    public RemoteProxy(Object obj) {
        proxied = obj;
    }
    
    

    @Override
    public Object invoke(Object o, Method method, Object[] os) throws Throwable {
        Object[] action = new Object[]{ method.getName(), method.getParameterTypes(),  os};
        actions.add(action);
        return method.invoke(proxied, os);
    }
    
    public void replayOn(Object obj) throws Throwable {
        for(Object[] action: actions) {
            Method m = obj.getClass().getMethod( (String) action[0], (Class<?>[]) action[1]);
            m.invoke(obj, (Object[]) action[2]);
        }
    }
}
