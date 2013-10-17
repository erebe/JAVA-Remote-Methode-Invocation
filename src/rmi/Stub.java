/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import idl.Interface;

/**
 *
 * @author erebe
 */
public class Stub {

    public static String generate(Interface inter) {

        return generateInterface(inter) + "\n\n" + generateLocalClass(inter);
    }

    private static void generateImport(StringBuilder b, Interface inter) {
        b.append("import rmi.Service;\n\n");
    }

    private static String generateLocalClass(Interface inter) {
        StringBuilder b = new StringBuilder();

        generateImport(b, inter);

        b.append("public class Local");
        b.append(inter.name);
        b.append(" implements ");
        b.append(inter.name);
        b.append(" {\n");

        b.append("\tprivate InvokationObject header = null;\n\n");
        b.append("\tpublic Local");
        b.append(inter.name);
        b.append("() {\n\t\theader = new InvokationObject(); ");
        b.append("\n\t}\n\n");
        
        for (Interface.Method m : inter.methods) {
            generateLocalMethod(b, m);
        }

        b.append("\n}");

        return b.toString();
    }

    private static void generateLocalMethod(StringBuilder b, Interface.Method m) {
        b.append("\tpublic ");
        b.append(m.returnType);
        b.append(" ");
        b.append(m.name);
        b.append("(");

        if (m.arguments.size() > 0) {
            for (int i = 0; i < m.arguments.size() - 1; i++) {
                for (String str : m.arguments.get(i)) {
                    b.append(str);
                    b.append(" ");
                }
                b.append(", ");
            }

            for (String str : m.arguments.get(m.arguments.size() - 1)) {
                b.append(str);
                b.append(" ");
            }

        }

        b.append(") {\n\t}\n\n");
    }

    private static void generateBodyLocalMethod(StringBuilder b, Interface.Method m) {
    }

    private static String generateInterface(Interface inter) {
        StringBuilder b = new StringBuilder();

        b.append("public interface ");
        b.append(inter.name);
        b.append(" {\n");

        for (Interface.Method m : inter.methods) {
            generateInterfaceMethod(b, m);
        }

        b.append("\n}");

        return b.toString();
    }

    private static void generateInterfaceMethod(StringBuilder b, Interface.Method m) {
        b.append("\t");
        b.append(m.returnType);
        b.append(" ");
        b.append(m.name);
        b.append("(");

        if (m.arguments.size() > 0) {
            for (int i = 0; i < m.arguments.size() - 1; i++) {
                for (String str : m.arguments.get(i)) {
                    b.append(str);
                    b.append(" ");
                }
                b.append(", ");
            }

            for (String str : m.arguments.get(m.arguments.size() - 1)) {
                b.append(str);
                b.append(" ");
            }
        }
        b.append(");\n");

    }
}
