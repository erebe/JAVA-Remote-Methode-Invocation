/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.classimpl;

import java.io.IOException;

public interface HelloServer {
	void sayHello() throws IOException;
	int say(String msg) throws IOException;
	void sayTo(String msg, String dest) throws IOException;

}