package rmi.classimpl;

import rmi.Service;
import java.io.Serializable;
import java.io.IOException;
import rmi.Remote;
import java.util.List;
import rmi.RemoteThread.Future;

public class LocalHelloServer extends Remote implements IHelloServer, Serializable {
	static { Service.getInstance().registerProxy("HelloServer",new LocalHelloServer()); }

	@Override
	public void sayHello() throws IOException {
		Object[] args = new Object[] {};
		Class<?>[] cls = new Class<?>[] { };

		Object[] rets = getRemote().remoteCall("sayHello", cls, args, true);

	}

	public void asyncSayHello() {
		Object[] args = new Object[] {};
		Class<?>[] cls = new Class<?>[] { };

		getRemote().asyncRemoteCall(this ,"sayHello", cls, args);
	}

	@Override
	public String say(List<String> msg) throws IOException {
		Object msgp = getRemote().proxifyVariable(msg, List.class);
		Object[] args = new Object[] {msgp,};
		Class<?>[] cls = new Class<?>[] { List.class,};

		Object[] rets = getRemote().remoteCall("say", cls, args, false);
		getRemote().replayOn(rets[1], msg);
		return (String) rets[0];
	}

	public Future<String> asyncSay(List<String> msg) {
		Object msgp = getRemote().proxifyVariable(msg, List.class);
		Object[] args = new Object[] {msgp,};
		Class<?>[] cls = new Class<?>[] { List.class,};

		return getRemote().asyncRemoteCall(this ,"say", cls, args);
	}

	@Override
	public void sayTo(String msg, String dest) throws IOException {
		Object destp = getRemote().proxifyVariable(dest, String.class);
		Object[] args = new Object[] {msg,destp,};
		Class<?>[] cls = new Class<?>[] { String.class,String.class,};

		Object[] rets = getRemote().remoteCall("sayTo", cls, args, false);
		getRemote().replayOn(rets[2], dest);

	}

	public void asyncSayTo(String msg, String dest) {
		Object destp = getRemote().proxifyVariable(dest, String.class);
		Object[] args = new Object[] {msg,destp,};
		Class<?>[] cls = new Class<?>[] { String.class,String.class,};

		getRemote().asyncRemoteCall(this ,"sayTo", cls, args);
	}

	@Override
	public void fillMe(List<String> msg) throws IOException {
		Object msgp = getRemote().proxifyVariable(msg, List.class);
		Object[] args = new Object[] {msgp,};
		Class<?>[] cls = new Class<?>[] { List.class,};

		Object[] rets = getRemote().remoteCall("fillMe", cls, args, false);
		getRemote().replayOn(rets[1], msg);

	}

	public void asyncFillMe(List<String> msg) {
		Object msgp = getRemote().proxifyVariable(msg, List.class);
		Object[] args = new Object[] {msgp,};
		Class<?>[] cls = new Class<?>[] { List.class,};

		getRemote().asyncRemoteCall(this ,"fillMe", cls, args);
	}


}