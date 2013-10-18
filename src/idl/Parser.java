/*
 * To change this template, choose Tools | Templates
 * and open the template reader the editor.
 */
package idl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author erebe
 */
public class Parser {

    private final static String pathFile = "sample_idl.txt";
    private Scanner reader = null;
    private Interface interfarce = new Interface();

    public Parser() {

        try {
            this.reader = new Scanner(new FileReader(pathFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readBlanks() {
        reader = reader.skip("[\t\n ]*");
    }

    private void skipUntilBodyInterface() throws Throwable {
        String token = "[{]";

        readBlanks();
        if (!reader.hasNext(token)) {
            throw new Throwable("Cannot find bracket of the body interface");
        }

        reader = reader.skip(token);
        readBlanks();
    }

    private String readInterfaceName() throws Throwable {
        String[] tokens = {"interface", "[_#a-zA-Z0-9]+"};


        if (!reader.hasNext(tokens[0])) {
            throw new Throwable("Cannot find \"interface\" token");
        }

        reader.skip(tokens[0]);

        readBlanks();

        if (!reader.hasNext(tokens[1])) {
            throw new Throwable("Interface is not valid, please choose an alphanumeric name");
        }

        return reader.next(tokens[1]);
    }

    public Interface parse() throws Throwable {


        readBlanks();
        interfarce.name = readInterfaceName();
        skipUntilBodyInterface();
        interfarce.procedure_option = readProcedureOption();

        try {
        for (;;) {
            interfarce.methods.add(readMethodDefinition());
        }
        } catch(Throwable e) {
            //do nothing
        }

        return interfarce;
    }

    private void SkipUntilEndBodyInterface() throws Throwable {
        String token = "[}];";

        readBlanks();
        if (!reader.hasNext(token)) {
            throw new Throwable("Cannot find end bracket of the body interface");
        }

        reader = reader.skip(token);
        readBlanks();
    }

    private Interface.Method readMethodDefinition() throws Throwable {

        Interface.Method m = interfarce.new Method();

        if (tryReadOnewayToken() != null) {
            m.isOneway = true;
        } else {
            m.isOneway = false;
        }

        m.returnType = readReturnType();
        m.name = readMethodName();
        readArguments(m);
        reader.skip(";");
        readBlanks();


        return m;

    }

    private String readProcedureOption() throws Throwable {
        String[] tokens = {"(local|remote)",};


        readBlanks();
        if (!reader.hasNext(tokens[0])) {
            throw new Throwable("Your interface should specify if it is remote or local");
        }


        String procedure_option = reader.next(tokens[0]);
        readBlanks();

        return procedure_option;

    }

    private String tryReadOnewayToken() {
        String token = "oneway";

        readBlanks();
        if (!reader.hasNext(token)) {
            return null;
        } else {
            return reader.next(token);
        }

    }

    private String readReturnType() throws Throwable {
        String token = "[a-zA-Z_<>\\s]+";

        readBlanks();

        if (!reader.hasNext(token)) {
            throw new Throwable("Return type of the method is not valid");
        }

        String str = reader.next(token);
        readBlanks();

        return str;
    }

    private String readMethodName() throws Throwable {
        String tokens[] = {"[a-zA-Z_0-9]+\\s*\\([^\\)]*\\)", "\\(", "[a-zA-Z]+"};

        readBlanks();
        Pattern delim = reader.delimiter();
        reader.useDelimiter(";");

        if (!reader.hasNext(tokens[0])) {
            throw new Throwable("Cannot find name of the method");
        }


        reader.useDelimiter(tokens[1]);
        String str = reader.next(tokens[2]);
        reader.useDelimiter(delim);

        return str;
    }

    private void readArguments(Interface.Method m) throws Throwable {
        readBlanks();

        Set<String> identifiers = new TreeSet<>();
        identifiers.add("in");
        identifiers.add("out");
        identifiers.add("inout");

        Pattern delim = reader.delimiter();
        reader.useDelimiter(";");
        if (!reader.hasNext("\\([^\\)]*\\)")) {
            throw new Throwable("Error during parsing method's argument");
        }

        reader.skip("\\(\\s*");
        String[] args = reader.next(".*\\)").split(",");
        

        if (args[args.length - 1].split("\\)").length > 0) {
            args[args.length - 1] = args[args.length - 1].split("\\)")[0];

        } else {
            args[args.length - 1] = null;
        }

        
        if (args.length < 1) {
            reader.useDelimiter(delim);
            return;
        }

        
        int i = 0;
        for (; i < args.length  && args[i] != null; i++) {
            String[] ids = args[i].trim().split(" ");

            if (ids.length > 3) {
                throw new Throwable("Parse error during arguments parse");
            }

            if (ids.length == 3 && !identifiers.contains(ids[0])) {
                throw new Throwable("Parse error parameter_attribute does not exist");
            }

            m.arguments.add(ids);
        }
        reader.useDelimiter(delim);
    }
}
