/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

/**
 *
 * @author erebe
 */
public class Service {
        private static Service instance = null;
 
        private Service() {       
        }
 
        public static synchronized Service getInstance() {
            
                if (instance == null) {
                        instance = new Service ();
                }
                return instance;
        }
        
        
        public class InvokationObject {
            public String className = null;
            public String methodName = null;
        }
}
