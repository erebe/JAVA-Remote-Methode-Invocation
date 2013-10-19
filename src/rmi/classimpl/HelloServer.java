/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.classimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface HelloServer {
	void sayHello() throws IOException;
	String say(List<String> msg) throws IOException;
	void sayTo(String msg, String dest) throws IOException;

}