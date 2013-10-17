/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erebe
 */
public class Interface {

    public String name = null;
    public String procedure_option = null;
    public List<Method> methods = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append("interface ");
        b.append(name);
        b.append(" {\n");
        b.append(procedure_option);
        b.append("\n");

        for (Method m : methods) {
            b.append(m.toString());
        }
        b.append("\n};");

        return b.toString();
    }

    public class Method {

        public String returnType = null;
        public String name = null;
        public Boolean isOneway = null;
        public List<String[]> arguments = new ArrayList<>();

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append((isOneway) ? "\toneway " : "\t");
            b.append(returnType);
            b.append(" ");
            b.append(name);
            b.append("(");

            for (String[] arg : arguments) {
                for (String str : arg) {
                    b.append(str);
                    b.append(" ");
                }
                b.append(", ");
            }
            b.append(");\n");

            return b.toString();

        }
    }
}
