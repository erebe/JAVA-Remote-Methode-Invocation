/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//        Object obj = registers.get("HelloServer");
//        Method m = obj.getClass().getMethod("sayHello", new Class[] {});
//        m.invoke(obj, new Object[] {});
/**
 *
 * @author erebe
 */
public class Service {

    private static final int portNumber = 8888;
    private static Service instance = null;
    public String host = null;
    private Map<String, Object> registers = new TreeMap<>();
    private Map<String, Object> proxies = new TreeMap<>();

    private Service() {
    }

    public static synchronized Service getInstance() {

        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void initRMIServer(String hostname, int port) {
        host = hostname;
    }

    public void waitForEver() throws IOException {
        assert (host != null && portNumber != 0);

        ServerSocket srv = new ServerSocket(portNumber);
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        RemoteHeader header = null;

        for (;;) {
            try {
                Socket s = srv.accept();
                out = new ObjectOutputStream(s.getOutputStream());
                in = new ObjectInputStream(s.getInputStream());

                header = (RemoteHeader) in.readObject();

                switch (header.order) {
                    case "GET_REMOTEBIND":

                        Remote obj = (Remote) proxies.get(getSimpleProxyClassName(registers.get(header.bindedTo)));
                        obj.getRemote().objectName = header.bindedTo;
                        obj.getRemote().portDest = Service.portNumber;
                        obj.getRemote().host = header.host;
                        out.writeObject(obj);
                        out.flush();
                        out.close();
                        break;

                    case "CALL_METHOD":
                        Object t = registers.get(header.bindedTo);
                        Class<?>[] cls = (Class<?>[]) in.readObject();
                        Object[] args = (Object[]) in.readObject();
                        

                        Method m = t.getClass().getMethod(header.methodName, cls);
                        Object ret = m.invoke(t, args);
                        List<Object> tmp = new ArrayList<>();
                        tmp.add(ret);
                        tmp.addAll(Arrays.asList(args));
                        Object[] rets = tmp.toArray();
                        out.writeObject(rets);

                        break;

                    default:
                        System.out.println("Uncaught RMIServer order");
                }

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Object bind(String uri) throws IOException, ClassNotFoundException {

        String[] info = parseUri(uri);
        Socket channel;
        channel = new Socket(info[0], Service.portNumber);
        RemoteHeader header = new RemoteHeader();
        header.host = info[0];
        header.bindedTo = info[1];
        header.order = "GET_REMOTEBIND";

        ObjectOutputStream out = new ObjectOutputStream(channel.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(channel.getInputStream());
        out.writeObject(header);
        out.flush();



        return in.readObject();
    }

    private String[] parseUri(String uri) {

        String[] els = uri.split("//")[1].split("/");
        System.out.println(Arrays.asList(els));
        return els;
    }

    public void register(String name, Object obj) throws ClassNotFoundException {
        assert (host != null);

        Class.forName(getProxyClassName(obj));
        registers.put(name, obj);
    }

    private String getProxyClassName(Object obj) {

        String fqdn = null;

        for (Class<?> cl : obj.getClass().getInterfaces()) {
            if (cl.getName().startsWith("rmi.classimpl.")) {
                fqdn = cl.getName();
                break;
            }
        }
        String[] fqdns = fqdn.split("\\.");
        String className = fqdns[fqdns.length - 1];

        System.out.println("Loading class rmi.classimpl.Local" + className);
        return "rmi.classimpl.Local" + className;
    }

    private String getSimpleProxyClassName(Object obj) {

        String fqdn = null;

        for (Class<?> cl : obj.getClass().getInterfaces()) {
            if (cl.getName().startsWith("rmi.classimpl.")) {
                fqdn = cl.getName();
                break;
            }
        }
        String[] fqdns = fqdn.split("\\.");
        String className = fqdns[fqdns.length - 1];


        return className;
    }

    public void registerProxy(String className, Object obj) {

        proxies.put(className, obj);
    }
}
