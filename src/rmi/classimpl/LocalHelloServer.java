package rmi.classimpl;

import rmi.Service;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.IOException;
import rmi.Remote;
import rmi.RemoteHeader;

public class LocalHelloServer extends Remote implements HelloServer, Serializable {
	static { Service.getInstance().registerProxy("HelloServer",new LocalHelloServer()); }

	@Override
	public void sayHello() throws IOException {
		Object[] args = new Object[] {};
		Class<?>[] cls = new Class<?>[] { };


		ObjectOutputStream out = getRemote().getOutputStream();
		ObjectInputStream in = getRemote().getInputStream();
		RemoteHeader header = new RemoteHeader();
		header.order = "CALL_METHOD";
		header.methodName = "sayHello";
		header.bindedTo = getRemote().objectName;
		out.writeObject(header);
		out.writeObject(cls);
		out.writeObject(args);
		out.flush();
		getRemote().close();
	}

	@Override
	public int say(String msg) throws IOException {
		Object[] args = new Object[] {msg,};
		Class<?>[] cls = new Class<?>[] { msg.getClass(),};


		ObjectOutputStream out = getRemote().getOutputStream();
		ObjectInputStream in = getRemote().getInputStream();
		RemoteHeader header = new RemoteHeader();
		header.order = "CALL_METHOD";
		header.methodName = "say";
		header.bindedTo = getRemote().objectName;
		out.writeObject(header);
		out.writeObject(cls);
		out.writeObject(args);
		out.flush();
		getRemote().close();
            return 0;
	}

	@Override
	public void sayTo(String msg, String dest) throws IOException {
		Object[] args = new Object[] {msg,dest,};
		Class<?>[] cls = new Class<?>[] { msg.getClass(),dest.getClass(),};


		ObjectOutputStream out = getRemote().getOutputStream();
		ObjectInputStream in = getRemote().getInputStream();
		RemoteHeader header = new RemoteHeader();
		header.order = "CALL_METHOD";
		header.methodName = "sayTo";
		header.bindedTo = getRemote().objectName;
		out.writeObject(header);
		out.writeObject(cls);
		out.writeObject(args);
		out.flush();
		getRemote().close();
	}


}