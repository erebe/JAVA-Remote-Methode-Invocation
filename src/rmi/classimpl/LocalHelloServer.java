package rmi.classimpl;

import rmi.Service;
import java.io.Serializable;
import java.io.IOException;
import java.util.List;
import rmi.Remote;

public class LocalHelloServer extends Remote implements HelloServer, Serializable {
	static { Service.getInstance().registerProxy("HelloServer",new LocalHelloServer()); }

	@Override
	public void sayHello() throws IOException {
		Object[] args = new Object[] {};
		Class<?>[] cls = new Class<?>[] { };

		Object[] rets = getRemote().remoteCall("sayHello", cls, args, true);

	}

	@Override
	public String say(List<String> msg) throws IOException {
		Object[] args = new Object[] {msg,};
		Class<?>[] cls = new Class<?>[] { List.class,};

		Object[] rets = getRemote().remoteCall("say", cls, args, false);
		return (String) rets[0];
	}

	@Override
	public void sayTo(String msg, String dest) throws IOException {
		Object destp = getRemote().proxifyVariable(dest, String.class);
		Object[] args = new Object[] {msg,destp,};
		Class<?>[] cls = new Class<?>[] { String.class,String.class,};

		Object[] rets = getRemote().remoteCall("sayTo", cls, args, false);
		getRemote().replayOn(rets[2], dest);
	}


}