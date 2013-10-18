/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idl;

/**
 *
 * @author erebe
 */
public class Stub {

    public static String generate(Interface inter) {

        return generateInterface(inter) + "\n\n" + generateLocalClass(inter);
    }

    private static void generateImport(StringBuilder b, Interface inter) {
        b.append("package rmi.classimpl;\n\n");
        b.append("import rmi.Service;\n");
        b.append("import java.io.ObjectOutputStream;\n");
        b.append("import java.io.ObjectInputStream;\n");
        b.append("import java.io.Serializable;\n");
        b.append("import java.io.IOException;\n");
        b.append("import rmi.Remote;\n");
        b.append("import rmi.RemoteHeader;\n");
        b.append("\n");
    }

    private static String generateLocalClass(Interface inter) {
        StringBuilder b = new StringBuilder();

        generateImport(b, inter);

        b.append("public class Local");
        b.append(inter.name);
        b.append(" extends Remote implements ");
        b.append(inter.name);
        b.append(", Serializable {\n");
        b.append("\tstatic { Service.getInstance().registerProxy(");
        b.append("\"").append(inter.name).append("\",");
        b.append("new Local").append(inter.name).append("()");
        b.append("); }\n\n");


        for (Interface.Method m : inter.methods) {
            generateLocalMethod(b, m);
        }

        b.append("\n}");

        return b.toString();
    }

    private static void generateLocalMethod(StringBuilder b, Interface.Method m) {
        b.append("\t@Override\n\tpublic ");
        b.append(m.returnType);
        b.append(" ");
        b.append(m.name);
        b.append("(");

        if (m.arguments.size() > 0) {
            for (int i = 0; i < m.arguments.size() - 1; i++) {
                int j = 0;
                if (m.arguments.get(i).length > 2) {
                    j = 1;
                }

                b.append(m.arguments.get(i)[j++]);
                b.append(" ");
                b.append(m.arguments.get(i)[j]);
                b.append(", ");
            }

            int j = 0;
            String[] last = m.arguments.get(m.arguments.size() - 1);
            if (last.length > 2) {
                j = 1;
            }

            b.append(last[j++]);
            b.append(" ");
            b.append(last[j]);

        }


        b.append(") throws IOException {\n");
        generateBodyLocalMethod(b, m);
        b.append("\n\t}\n\n");
    }

    private static void generateBodyLocalMethod(StringBuilder b, Interface.Method m) {

        b.append("\t\tObject[] args = new Object[] {");
        for (String[] args : m.arguments) {
            int i = 1;
            if (args.length > 2) {
                i++;
            }

            b.append(args[i]);
            b.append(",");
        }
        b.append("};\n");

        b.append("\t\tClass<?>[] cls = new Class<?>[] { ");
        for (String[] args : m.arguments) {
            int i = 1;
            if (args.length > 2) {
                i++;
            }

            b.append(args[i]);
            b.append(".getClass(),");
        }
        b.append("};\n\n");



        b.append("\n\t\tObjectOutputStream out = getRemote().getOutputStream();");
        b.append("\n\t\tObjectInputStream in = getRemote().getInputStream();");
        b.append("\n\t\tRemoteHeader header = new RemoteHeader();");
        b.append("\n\t\theader.order = \"CALL_METHOD\";");
        b.append("\n\t\theader.methodName = \"");
        b.append(m.name);
        b.append("\";");
        b.append("\n\t\theader.bindedTo = getRemote().objectName;");
        b.append("\n\t\tout.writeObject(header);");
        b.append("\n\t\tout.writeObject(cls);");
        b.append("\n\t\tout.writeObject(args);");
        b.append("\n\t\tout.flush();");
        b.append("\n\t\tgetRemote().close();");
    }

    private static String generateInterface(Interface inter) {
        StringBuilder b = new StringBuilder();
        b.append("import java.io.IOException;\n\n");
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
                int j = 0;
                if (m.arguments.get(i).length > 2) {
                    j = 1;
                }

                b.append(m.arguments.get(i)[j++]);
                b.append(" ");
                b.append(m.arguments.get(i)[j]);
                b.append(", ");
            }

            int j = 0;
            String[] last = m.arguments.get(m.arguments.size() - 1);
            if (last.length > 2) {
                j = 1;
            }

            b.append(last[j++]);
            b.append(" ");
            b.append(last[j]);

        }
        b.append(") throws IOException;\n");

    }
}
