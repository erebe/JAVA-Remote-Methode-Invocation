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
        b.append("import java.io.Serializable;\n");
        b.append("import java.io.IOException;\n");
        b.append("import rmi.Remote;\n");
        b.append("import java.util.List;\n");
        b.append("import rmi.RemoteThread.Future;\n");
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
            generateAsyncLocalMethod(b, m);
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

        for (String[] arg : m.arguments) {
            switch (arg[0]) {
                case "inout":
                case "out":
                    b.append("\t\tObject ");
                    b.append(arg[2]).append("p");
                    b.append(" = getRemote().proxifyVariable(");
                    b.append(arg[2]);
                    b.append(", ");
                    b.append(arg[1].split("<")[0]);
                    b.append(".class);\n");
                    break;
            }
        }

        b.append("\t\tObject[] args = new Object[] {");
        for (String[] args : m.arguments) {
            int i = 1;
            if (args.length > 2) {
                i++;
            }

            b.append(args[i]);
            if (args[0].equals("out") || args[0].equals("inout")) {
                b.append("p");
            }


            b.append(",");
        }
        b.append("};\n");

        b.append("\t\tClass<?>[] cls = new Class<?>[] { ");
        for (String[] args : m.arguments) {
            int i = 0;
            if (args.length > 2) {
                i++;
            }

            b.append(args[i].split("<")[0]);
            b.append(".class,");
        }
        b.append("};\n\n");


        b.append("\t\tObject[] rets = getRemote().remoteCall(\"");
        b.append(m.name);
        b.append("\", cls, args, ");
        b.append(m.isOneway.toString());
        b.append(");\n");

        if (!m.isOneway) {
            int i = 0;
            for (String[] arg : m.arguments) {
                switch (arg[0]) {
                    case "inout":
                    case "out":
                        b.append("\t\tgetRemote().replayOn(rets[");
                        b.append(i + 1);
                        b.append("], ");
                        b.append(arg[2]);
                        b.append(");\n");
                        break;
                }
                i++;
            }
        }

        if (m.returnType.equals("void")) {
            return;
        }



        b.append("\t\treturn (");
        b.append(m.returnType);
        b.append(") rets[0];");

    }

    private static String generateInterface(Interface inter) {
        StringBuilder b = new StringBuilder();
        b.append("import java.util.List;\n");
        b.append("import rmi.RemoteThread.Future;\n");
        b.append("import java.io.IOException;\n\n");
        b.append("public interface ");
        b.append(inter.name);
        b.append(" {\n");

        for (Interface.Method m : inter.methods) {
            generateInterfaceMethod(b, m);
            generateAsyncInterfaceMethod(b, m);
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

    private static void generateAsyncLocalMethod(StringBuilder b, Interface.Method m) {
        b.append("\tpublic ");
        if (m.returnType.equals("void")) {
            b.append(m.returnType);
        } else {
            b.append("Future<");
            b.append(m.returnType);
            b.append(">");
        }

        b.append(" async");
        b.append(Character.toUpperCase(m.name.charAt(0))).append(m.name.substring(1));
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


        b.append(") {\n");
        generateAsyncBodyLocalMethod(b, m);
        b.append("\n\t}\n\n");
    }

    private static void generateAsyncBodyLocalMethod(StringBuilder b, Interface.Method m) {
        for (String[] arg : m.arguments) {
            switch (arg[0]) {
                case "inout":
                case "out":
                    b.append("\t\tObject ");
                    b.append(arg[2]).append("p");
                    b.append(" = getRemote().proxifyVariable(");
                    b.append(arg[2]);
                    b.append(", ");
                    b.append(arg[1].split("<")[0]);
                    b.append(".class);\n");
                    break;
            }
        }

        b.append("\t\tObject[] args = new Object[] {");
        for (String[] args : m.arguments) {
            int i = 1;
            if (args.length > 2) {
                i++;
            }

            b.append(args[i]);
            if (args[0].equals("out") || args[0].equals("inout")) {
                b.append("p");
            }


            b.append(",");
        }
        b.append("};\n");

        b.append("\t\tClass<?>[] cls = new Class<?>[] { ");
        for (String[] args : m.arguments) {
            int i = 0;
            if (args.length > 2) {
                i++;
            }

            b.append(args[i].split("<")[0]);
            b.append(".class,");
        }
        b.append("};\n\n\t\t");

        if (!m.returnType.equals("void")) {
            b.append("return ");
        }

        b.append("getRemote().asyncRemoteCall(this ,\"");
        b.append(m.name);
        b.append("\", cls, args);");


    }

    private static void generateAsyncInterfaceMethod(StringBuilder b, Interface.Method m) {
        b.append("\t");
        if (m.returnType.equals("void")) {
            b.append(m.returnType);
        } else {
            b.append("Future<");
            b.append(m.returnType);
            b.append(">");
        }

        b.append(" async");
        b.append(Character.toUpperCase(m.name.charAt(0))).append(m.name.substring(1));
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
        b.append(");\n");

    }
}
