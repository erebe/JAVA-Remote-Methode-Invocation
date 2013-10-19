package rmi.classimpl;

import java.io.IOException;
import java.util.List;
import rmi.RemoteThread.Future;

public interface IHelloServer {
	void sayHello() throws IOException;
	void asyncSayHello();
	String say(List<String> msg) throws IOException;
	Future<String> asyncSay(List<String> msg);
	void sayTo(String msg, String dest) throws IOException;
	void asyncSayTo(String msg, String dest);
	void fillMe(List<String> msg) throws IOException;
	void asyncFillMe(List<String> msg);

}