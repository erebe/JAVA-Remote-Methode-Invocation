/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author erebe
 */
public class RemoteThread<T> extends Thread {

    private Object obj = null;
    Object[] args = null;
    Class[] cls = null;
    String funcName = null;
    Object ret = null;

    public RemoteThread(Object obj, String funcName, Class[] cls, Object[] args) {
        this.obj = obj;
        this.args = args;
        this.funcName = funcName;
        this.cls = cls;
    }

    @Override
    public void run() {
        try {
            Method m = obj.getClass().getMethod(funcName, cls);
            ret = m.invoke(obj, args);
        } catch (Throwable ex) {
        }
    }

    public Future<T> getFuture() {

        return new Future<>(this);
    }

    public static class Future<T> {

        RemoteThread<T> th = null;

        public Future(RemoteThread<T> th) {
            this.th = th;
        }

        public T get() throws InterruptedException {
            if (th.isAlive()) {
                th.join();
            }

            return (T) th.ret;
        }
    }
};
