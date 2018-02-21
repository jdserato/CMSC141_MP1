import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by Alvarez, Mary Michaelle; Famat, Ruffa Mae; and Serato, Jay Vince on February 05, 2018. hdsjdsd
 */
public class Main {
    private static BufferedReader br;
    private static BufferedWriter wr;
    private static final File INPUT_FILE = new File("mpa1.in");
    private static final File OUTPUT_FILE = new File("AlvarezFamatSerato.out");
    private static final List<String> dataTypes = Arrays.asList("int", "short", "long", "float", "double", "char"); // FIXME eliminate return and make some special function to handle this
    private static boolean valid = true;
    private static String readLine = "";

    public static void main(String[] args) {
        int types = 0;
        String sCurrLine;
        Type type;
        List<String> fDef_DeclVar;

        try {
            // Putting the input and output files in a buffered reader and writer respectively.
            br = new BufferedReader(new FileReader(INPUT_FILE));
            wr = new BufferedWriter(new FileWriter(OUTPUT_FILE));

            // Identifying the number of types (found in the first line of the INPUT_FILE).
            try {
                types = Integer.parseInt(br.readLine());
            } catch (InputMismatchException e) {
                System.err.println(e.getMessage());
            }
            System.out.println(types);
            while (--types >= 0) {
                type = null;
                fDef_DeclVar = new ArrayList<>();
                valid = true;
                sCurrLine = "";
                boolean returned = false;
                boolean testCaseSearch = true;
                // Finding the next test case:
                do {
                    while (readLine.length() == 0) {
                        sCurrLine = sCurrLine.concat(" ");
                        readLine = br.readLine();
                    }
                    System.out.println(readLine + " and " + sCurrLine + " where readline is " + readLine.length());

                    while (readLine.length() > 0) {
                        sCurrLine = sCurrLine.concat(readLine.charAt(0) + "");
                        if (readLine.charAt(0) == ';' || readLine.charAt(0) == '{') {
                            readLine = readLine.substring(1);
                            testCaseSearch = false; // Test case search finished
                            break;
                        } else {
                            readLine = readLine.substring(1);
                        }
                    }

                } while (testCaseSearch);

                // FIXME eliminate debug (all sout)
                wr.append(String.valueOf(types));
                System.out.println(types + "");
                wr.newLine();

                sCurrLine = sCurrLine.trim(); // Removing redundant spaces, if any are present
                System.out.println("TESTCASE: " + sCurrLine);
                // Checking its type whether its a variable declaration, a function declaration, or function definition.
                if (sCurrLine.charAt(sCurrLine.length() - 1) == ';') { // The type is either a variable or a function declaration.
                    if (sCurrLine.contains("(") && sCurrLine.contains(")")) { // The type is likely a function declaration.
                        type = Type.FUNCTION_DECLARATION;
                        System.out.println("The type is a valid function declaration.");
                        String name = "";
                        boolean space = false;
                        for (int i = 0; i < sCurrLine.length(); i++) {
                            if (sCurrLine.charAt(i) == ' ') {
                                space = true;
                                name = name.concat(sCurrLine.charAt(i) + "");
                            }
                            if (space && sCurrLine.charAt(i) != '(') {
                                name = name.concat(sCurrLine.charAt(i) + "");
                            } else if (sCurrLine.charAt(i) == '(') {
                                name = name.trim();
                                System.out.println(name + " is name");
                                if (name.contains(" ")) {
                                    valid = false;
                                    while (name.charAt(0) != ' ') {
                                        name = name.substring(1);
                                    }
                                    name = name.trim();
                                    for (int j = 0; j < name.length(); j++) {
                                        if (name.charAt(j) == ' ') {
                                            name = name.substring(0, j);
                                            break;
                                        }
                                    }
                                    System.out.println(name + " is rev name");
                                    for (String s : dataTypes) {
                                        if (s.equals(name)) {
                                            type = Type.VARIABLE_DECLARATION;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }

                        }
                    } else { // The type is likely a variable declaration.
                        type = Type.VARIABLE_DECLARATION;
                        System.out.println("The type is a valid variable declaration.");
                    }
                } else if (sCurrLine.contains("(") && sCurrLine.contains(")") && sCurrLine.charAt(sCurrLine.length() - 1) == '{') { // The type is likely a function declaration.
                    type = Type.FUNCTION_DEFINITION;
                    System.out.println("The type is a valid function definition.");
                } else { // Preliminary test: INVALID
                    valid = false;
                    wr.append("INVALID ");
                    if (sCurrLine.contains("(") || sCurrLine.contains(")")) { // An invalid function
                        wr.append("FUNCTION");
                        if (sCurrLine.contains("{")) {
                            terminate();
                            wr.append(" DEFINITION");
                        } else {
                            wr.append(" DECLARATION");
                        }
                    } else {
                        wr.append("VARIABLE DECLARATION");
                    }
                }
                String returnType = "";
                int i;
                // Obtaining and checking the validity of the return type
                for (i = 0; i < sCurrLine.length() && sCurrLine.charAt(i) != ' '; i++) {
                    returnType = returnType.concat(sCurrLine.charAt(i) + "");
                }
                System.out.println(returnType);
                boolean validRetType = false;
                for (String s : dataTypes) {
                    if (returnType.equals(s)) {
                        validRetType = true;
                        break;
                    }
                }
                if (valid && type.toString().contains("FUNCTION")) {
                    if (returnType.equals("void")) {
                        validRetType = true;
                    }
                }

                if (!validRetType) {
                    System.out.println("type is invalid.");
                    if (type == Type.FUNCTION_DEFINITION) {
                        terminate();
                    } else {
                        wr.append("INVALID ").append(type.toString());
                        valid = false;
                    }
                }

                i++;
                /**
                 * The string is stored in the sCurrLine. [sCurrLine = "int x = 0;"]
                 * I already have checked the return type and the variable type. ["int"]
                 * The incrementation of i indicates the start of the variable name or function name.
                 * Example:
                 * int x = 0;
                 * i is pointing at x already.
                 *
                 */
                if (valid && type == Type.VARIABLE_DECLARATION) { // TODO VARIABLE DECLARATION (Ruffa, start here)

                } else if (valid && type == Type.FUNCTION_DECLARATION) { // TODO FUNCTION DECLARATION (MM, start here)
                    System.out.println("nisulod sa function declaration");

                    for (; sCurrLine.charAt(i) == ' '; i++);

                    if (!(sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z'))) {
                        //terminate(type);
                        valid = false;
                        System.out.println("Naming deficiency at first letter.");
                    } else {
                        String functionName = sCurrLine.charAt(i) + ""; //istore sa functionName ang kana na character

                        for (i++; sCurrLine.charAt(i) != '(' && sCurrLine.charAt(i) != ' '; i++) { //name ra jud ni sa function way apil ang after sa parenthesis
                            if (sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z') || (sCurrLine.charAt(i) >= '0' && sCurrLine.charAt(i) <= '9')) {
                                functionName = functionName.concat(sCurrLine.charAt(i) + "");
                                //System.out.println("mm " + functionName);
                            } else {
                                System.out.println("Naming deficiency at next letters: " + sCurrLine.charAt(i));
                                //terminate(type);
                                valid = false;
                                break;
                            }
                            //System.out.println(i);
                        }

                        //mana ko here
                        if (valid) { //valid ang pag name sa function;
                            System.out.println("Function name cleared.");
                            for (; sCurrLine.charAt(i) != '('; i++){
                                if (i + 1 == sCurrLine.length()) {
                                    System.out.println("invalid");
                                    //terminate(type);
                                    valid = false;
                                    break;
                                } else if (sCurrLine.charAt(i) != ' ') {
                                    System.out.println("Invalid function name.");
                                    //terminate(type);
                                    valid = false;
                                    break;
                                }
                            }

                            if (valid){ //ang scurrLine kay naa na sa (
                                while (sCurrLine.charAt(i) != ')'){
                                    System.out.println(sCurrLine.charAt(i));

                                    String param = "";
                                        for (i++; sCurrLine.charAt(i) != ',' && sCurrLine.charAt(i) != ')'; i++){
                                            param = param.concat(sCurrLine.charAt(i) + "");
                                            System.out.println(sCurrLine.charAt(i) + " inserted");

                                        }
                                        System.out.println("param: " + param);
                                        param = param.trim();

                                        if (param.length() == 0 && sCurrLine.charAt(i) != ')') {
                                            System.out.println("Hmm. Something's not right.");
                                            valid = false;
                                            break;
                                        }

                                        boolean parameterType = false;
                                        String parameter = "";
                                        while (param.length() > 0 && param.charAt(0) != ' '){
                                            parameter = parameter.concat(param.charAt(0) + "");
                                            param = param.substring(1);
                                        }

                                        parameter = parameter.trim();
                                        for (String string : dataTypes) { //check if the parameter is valid
                                            if (parameter.equals(string)){
                                                parameterType = true;
                                                break;
                                            }else {
                                                if (parameter.length() == 0){ //if no parameter
                                                    parameterType = true;
                                                    break;
                                                }
                                            }
                                        }

                                    String varName = "";
                                    boolean notAllowed = false;

                                    while (param.length() > 0){
                                        if ((param.charAt(0) >= 'a' && param.charAt(0) <= 'z') || (param.charAt(0) >= 'A' && param.charAt(0) <= 'Z') || (param.charAt(0) >= '0' && param.charAt(0) <='9') || param.charAt(0) == '_' || param.charAt(0) == ' '){
                                            varName = varName.concat(param.charAt(0) + "");
                                            param = param.substring(1);
                                        } else {
                                            notAllowed = true;
                                            break;
                                        }
                                    }

                                    varName = varName.trim();
                                    if (varName.length() > 0 && (varName.charAt(0) >= '0' && varName.charAt(0) <= '9')){
                                        valid = false;
                                        break;
                                    } else {
                                        if (!notAllowed){
                                            for (String string : dataTypes){
                                                if (string.equals(varName)){
                                                    valid = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    if (notAllowed){
                                        for (String string : dataTypes){
                                            if (string.equals(varName)){
                                                valid = false;
                                                break;
                                            }
                                        }
                                    }
                                    
                                    if (!parameterType){
                                        System.out.println("error " + parameter);
                                        valid = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                } else if (valid) { // TODO FUNCTION DEFINITION (Vince, start here)
                    System.out.println("Staring");
                    String funcName = sCurrLine.charAt(i) + "";
                    // Inspecting function/variable name
                    for (; sCurrLine.charAt(i) == ' '; i++);
                    if (!(sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z'))) {
                        terminate();
                        System.out.println("Naming deficiency at first letter.");
                    } else {
                        String funcDefName = sCurrLine.charAt(i) + "";
                        for (i++; sCurrLine.charAt(i) != '(' && sCurrLine.charAt(i) != ' '; i++) {
                            funcName = funcName.concat(sCurrLine.charAt(i) + "");
                            if (sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z') || (sCurrLine.charAt(i) >= '0' && sCurrLine.charAt(i) <= '9')) {
                                funcDefName = funcDefName.concat(sCurrLine.charAt(i) + "");
                            } else {
                                System.out.println("Naming deficiency at next letters: " + sCurrLine.charAt(i));
                                terminate();
                                break;
                            }
                        }
                        if (valid) {
                            for (String s : dataTypes) {
                                if (s.equals(funcName)) {
                                    valid = false;
                                    break;
                                }
                            }
                            if (!valid || funcName.equals("void")) {
                                System.out.println("Invalid name.");
                                terminate();
                            }
                            System.out.println("Function name cleared : " + funcName);
                            for (; sCurrLine.charAt(i) != '('; i++){
                                if (i + 1 == sCurrLine.length()) {
                                    System.out.println("EOF");
                                    terminate();
                                    break;
                                } else if (sCurrLine.charAt(i) != ' ') {
                                    System.out.println("Invalid function name.");
                                    terminate();
                                    break;
                                }
                            }
                            if (valid) {
                                while (sCurrLine.charAt(i) != ')') {
                                    String param = "";
                                    for (i++; sCurrLine.charAt(i) != ',' && sCurrLine.charAt(i) != ')'; i++) {
                                        param = param.concat(sCurrLine.charAt(i) + "");
                                        System.out.println(sCurrLine.charAt(i) + " inserted");
                                    }
                                    param = param.trim();
                                    if (param.length() == 0 && sCurrLine.charAt(i) != ')') {
                                        System.out.println("Hmm. Something's not right.");
                                        terminate();
                                        break;
                                    } else if (sCurrLine.charAt(i) != ')' || param.length() > 0){

                                        boolean param_type = false;
                                        String parameter_type = "";
                                        while (param.length() > 0 && param.charAt(0) != ' ') {
                                            parameter_type = parameter_type.concat(param.charAt(0) + "");
                                            param = param.substring(1);
                                        }
                                        parameter_type = parameter_type.trim();
                                        for (String s : dataTypes) {
                                            if (parameter_type.equals(s)) {
                                                param_type = true;
                                                break;
                                            }
                                        }
                                        if (!param_type) {
                                            System.out.println("Parameter type error: " + parameter_type);
                                            terminate();
                                            break;
                                        }
                                        boolean param_name = false;
                                        String parameter_name = "";
                                        while(param.length() > 0) {
                                            if ((param.charAt(0) >= 'a' && param.charAt(0) <= 'z') || (param.charAt(0) >= 'A' && param.charAt(0) <= 'Z') || (param.charAt(0) >= '0' && param.charAt(0) <='9') || param.charAt(0) == '_' || param.charAt(0) == ' '){
                                                parameter_name = parameter_name.concat(param.charAt(0) + "");
                                                param = param.substring(1);
                                            } else {
                                                param_name = true;
                                                break;
                                            }
                                        }
                                        parameter_name = parameter_name.trim();
                                        for (String s : fDef_DeclVar) {
                                            if (parameter_name.equals(s)) {
                                                param_name = true;
                                                break;
                                            }
                                        }
                                        if (parameter_name.length() > 0 && (param_name || parameter_name.contains(" ") || (parameter_name.charAt(0) >= '0' && parameter_name.charAt(0) <= '9'))) {
                                            System.out.println("Parameter name error");
                                            terminate();
                                            break;
                                        } else {
                                            if (!parameter_name.isEmpty()) {
                                                for (String s : dataTypes) {
                                                    if (s.equals(parameter_name)) {
                                                        valid = false;
                                                        break;
                                                    }
                                                }
                                                if (!valid || parameter_name.equals("void")) {
                                                    System.out.println("Invalid name. Should not be a data type.");
                                                    terminate();
                                                } else {
                                                    fDef_DeclVar.add(parameter_name);
                                                }
                                            }
                                            System.out.println("Contents of variables declared: " );
                                            for (String s : fDef_DeclVar) {
                                                System.out.print(s + " ");
                                            }
                                        }

                                    }
                                }
                                if (valid) {
                                    for (i++; sCurrLine.charAt(i) != '{'; i++) {
                                        if (sCurrLine.charAt(i) != ' ') {
                                            terminate();
                                            System.out.println("Inability to find EOF");
                                            break;
                                        }
                                    }
                                }
                                if (valid) {
                                    while (readLine.isEmpty()) {
                                        readLine = br.readLine();
                                    }
                                    System.out.println("Readline's content: " + readLine);
                                    readLine = readLine.trim();
                                    while (valid && readLine.charAt(0) != '}') {
                                        sCurrLine = "";
                                        readLine = readLine.trim();
                                        while (readLine.isEmpty()) {
                                            readLine = br.readLine();
                                        }
                                        for (i = 0; readLine.charAt(0) != ';'; i++) {
                                            sCurrLine = sCurrLine.concat(readLine.charAt(0) + "");
                                            readLine = readLine.substring(1);
                                            while (readLine.isEmpty()) {
                                                //System.out.println("Empty. Finding next.");
                                                readLine = br.readLine();
                                            }
                                        }
                                        sCurrLine = sCurrLine.trim();
                                        sCurrLine = sCurrLine.concat(";");
                                        System.out.println("sCurrLine's content: " + sCurrLine);
                                        readLine = readLine.substring(1);
                                        if (sCurrLine.startsWith("return ")) {
                                            returned = true;
                                            System.out.println("A return value");
                                            sCurrLine = sCurrLine.substring(7);
                                            System.out.println(sCurrLine);
                                            int parentheses = 0;
                                            String tempSCurrLine = "";
                                            for (int j = 0; j < sCurrLine.length(); j++) {
                                                System.out.println("s " + tempSCurrLine);
                                                if (sCurrLine.charAt(j) == '(') {
                                                    parentheses++;
                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                    if (sCurrLine.charAt(j + 1) != '(') {
                                                        j++;
                                                        tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                    }
                                                } else if (sCurrLine.charAt(j) == ')') {
                                                    parentheses--;
                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                } else {
                                                    tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                }
                                                System.out.println(tempSCurrLine + " is temp");
                                            }
                                            sCurrLine = tempSCurrLine;
                                            System.out.println(sCurrLine);
                                            if (parentheses != 0) {
                                                System.out.println("Expected parenthesis.");
                                                terminate();
                                                break;
                                            }
                                            System.out.println("Parenthesis cleared");
                                            boolean operationDetected = false;
                                            System.out.println("Valid: " + valid);
                                            System.out.println("scurrlayn: " + sCurrLine);
                                            while (valid && (sCurrLine.charAt(0) != ';' || operationDetected)) {
                                                System.out.println("scurrline: " + sCurrLine);
                                                String returning = "";
                                                sCurrLine = sCurrLine.trim();
                                                System.out.println("sCurrLine's content for god's sake: " + sCurrLine);
                                                while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                    returning = returning.concat(sCurrLine.charAt(0) + "");
                                                    System.out.println("Sending " + sCurrLine.charAt(0));
                                                    sCurrLine = sCurrLine.substring(1);
                                                }
                                                try {
                                                    Integer.parseInt(returning); // Returned is a number which is valid.
                                                    System.out.println(sCurrLine + "(!)");
                                                    operationDetected = false;
                                                } catch (NumberFormatException e) { // Returned is not a number.
                                                    if (returning.contains("'")) { // Might be a character...
                                                        if (returning.length() <= 2 || returning.length() >= 5) {
                                                            System.out.println("Too much or too less character.");
                                                            terminate();
                                                            break;
                                                        }
                                                        int quoteCtr = 0;
                                                        for (int j = 0; j < returning.length(); j++) {
                                                            if (returning.charAt(j) == '\'') {
                                                                quoteCtr++;
                                                            }
                                                        }
                                                        if (quoteCtr != 2) {
                                                            System.out.println("Quotes must be two.");
                                                            terminate();
                                                            break;
                                                        }
                                                        if (returning.length() == 4 && !returning.contains("\\")) {
                                                            System.out.println("Special case is not met.");
                                                            terminate();
                                                            break;
                                                        }
                                                        operationDetected = false;
                                                    } else {
                                                        boolean variableExists = false;
                                                        for (String s : fDef_DeclVar) {
                                                            if (s.equals(returning)) {
                                                                variableExists = true;
                                                            }
                                                        }
                                                        if (!variableExists) {
                                                            System.out.println("Return value is non-existent: " + returning);
                                                            terminate();
                                                            break;
                                                        }
                                                        if (!returning.isEmpty()) {
                                                            operationDetected = false;
                                                        }
                                                    }
                                                }
                                                sCurrLine = sCurrLine.trim();
                                                System.out.println(sCurrLine + "(!)");
                                                if (sCurrLine.charAt(0) != ';') { // We are expecting a return type with arithmetic expression/s.
                                                    if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                        operationDetected = true;
                                                        sCurrLine = sCurrLine.substring(1);
                                                        if (returning.isEmpty()) {
                                                            System.out.println("Expected token.");
                                                            terminate();
                                                            break;
                                                        }
                                                    } else {
                                                        System.out.println("Whoa man. We expect an operation");
                                                        terminate();
                                                        break;
                                                    }
                                                }
                                                System.out.println(sCurrLine + "(!)");

                                                System.out.println(sCurrLine + "(!)");
                                            }
                                            System.out.println("readline is " + readLine);
                                            if (valid) {
                                                readLine = readLine.trim();
                                                while (readLine.isEmpty()) {
                                                    readLine = br.readLine();
                                                    readLine = readLine.trim();
                                                }
                                                System.out.println("readline is " + readLine);
                                            }
                                        } else {
                                            String identifier = "";
                                            System.out.println("sCurrLine: " + sCurrLine);
                                            while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != '=') {
                                                System.out.println("Character is " + sCurrLine.charAt(0));
                                                if (sCurrLine.charAt(0) == ';') {
                                                    System.out.println("Seriously dude. It's plain wrong.");
                                                    terminate();
                                                    break;
                                                }
                                                identifier = identifier.concat(sCurrLine.charAt(0) + "");
                                                sCurrLine = sCurrLine.substring(1);
                                            }
                                            if (valid) {
                                                boolean varDec = false;
                                                for (String s : dataTypes) {
                                                    if (s.equals(identifier)) {
                                                        varDec = true;
                                                    }
                                                }
                                                sCurrLine = sCurrLine.trim();
                                                System.out.println("scurrline's : " + sCurrLine);
                                                if (varDec) {
                                                    System.out.println("A variable declaration");
                                                    while (sCurrLine.charAt(0) != ';') {
                                                        if (sCurrLine.charAt(0) == '=' || sCurrLine.charAt(0) == ',') {
                                                            System.out.println("Invalid variable name (which is null).");
                                                            terminate();
                                                            break;
                                                        }
                                                        identifier = "";
                                                        while (sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '=' && sCurrLine.charAt(0) != ',') {
                                                            identifier = identifier.concat(sCurrLine.charAt(0) + "");
                                                            sCurrLine = sCurrLine.substring(1);
                                                        }
                                                        sCurrLine = sCurrLine.trim();
                                                        identifier = identifier.trim();
                                                        if (identifier.contains(" ")) {
                                                            System.out.println("Invalid variable name");
                                                            terminate();
                                                            break;
                                                        }
                                                        if (!(identifier.charAt(0) == '_' || (identifier.charAt(0) >= 'A' && identifier.charAt(0) <= 'Z') || (identifier.charAt(0) >= 'a' && identifier.charAt(0) <= 'z'))) {
                                                            terminate();
                                                            System.out.println("Naming deficiency at first letter.");
                                                            break;
                                                        }
                                                        for (int j = 1; j < identifier.length(); j++) {
                                                            if (!(identifier.charAt(j) == '_' || (identifier.charAt(j) >= 'A' && identifier.charAt(j) <= 'Z') || (identifier.charAt(j) >= 'a' && identifier.charAt(j) <= 'z') || (identifier.charAt(j) >= '0' && identifier.charAt(j) <= '9'))) {
                                                                System.out.println("Naming deficiency at next letters: " + sCurrLine.charAt(j));
                                                                terminate();
                                                                break;
                                                            }
                                                        }

                                                        boolean varDeclared = false;
                                                        for (String s : fDef_DeclVar) {
                                                            if (s.equals(identifier)) {
                                                                varDeclared = true;
                                                            }
                                                        }
                                                        if (varDeclared) {
                                                            System.out.println("Redeclaration of variable");
                                                            terminate();
                                                            break;
                                                        } else {
                                                            for (String s : dataTypes) {
                                                                if (s.equals(identifier)) {
                                                                    valid = false;
                                                                    break;
                                                                }
                                                            }
                                                            if (!valid || identifier.equals("void")) {
                                                                System.out.println("Invalid name. Should not be a data type.");
                                                                terminate();
                                                            } else {
                                                                fDef_DeclVar.add(identifier);
                                                            }
                                                        }
                                                        if (sCurrLine.charAt(0) == '=') {
                                                            sCurrLine = sCurrLine.substring(1);
                                                            int parentheses = 0;
                                                            String tempSCurrLine = "";
                                                            int j;
                                                            for (j = 0; j < sCurrLine.length() && sCurrLine.charAt(j) != ',' && sCurrLine.charAt(j) != ';'; j++) {
                                                                System.out.println("s " + tempSCurrLine);
                                                                if (sCurrLine.charAt(j) == '(') {
                                                                    parentheses++;
                                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                                    if (sCurrLine.charAt(j + 1) != '(') {
                                                                        j++;
                                                                        tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                                    }
                                                                } else if (sCurrLine.charAt(j) == ')') {
                                                                    parentheses--;
                                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                                } else {
                                                                    tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                                }
                                                                System.out.println(tempSCurrLine + " is temp");
                                                            }
                                                            sCurrLine = tempSCurrLine;
                                                            sCurrLine = sCurrLine.concat(";");
                                                            System.out.println(sCurrLine);
                                                            if (parentheses != 0) {
                                                                System.out.println("Expected parenthesis.");
                                                                terminate();
                                                                break;
                                                            }
                                                            System.out.println("Parenthesis cleared");
                                                            boolean operationDetected = false;
                                                            System.out.println("Valid: " + valid);
                                                            System.out.println("scurrlayn: " + sCurrLine);
                                                            while (valid && ((sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != ',') || operationDetected)) {
                                                                System.out.println("scurrline: " + sCurrLine);
                                                                String returning = "";
                                                                sCurrLine = sCurrLine.trim();
                                                                System.out.println("sCurrLine's content for god's sake: " + sCurrLine);
                                                                while (sCurrLine.charAt(0) != ',' && sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                                    returning = returning.concat(sCurrLine.charAt(0) + "");
                                                                    System.out.println("Sending " + sCurrLine.charAt(0));
                                                                    sCurrLine = sCurrLine.substring(1);
                                                                }
                                                                try {
                                                                    Integer.parseInt(returning); // Returned is a number which is valid.
                                                                    System.out.println(sCurrLine + "(!)");
                                                                    operationDetected = false;
                                                                } catch (NumberFormatException e) { // Returned is not a number.
                                                                    if (returning.contains("'")) { // Might be a character...
                                                                        if (returning.length() <= 2 || returning.length() >= 5) {
                                                                            System.out.println("Too much or too less character.");
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                        int quoteCtr = 0;
                                                                        for (int k = 0; k < returning.length(); k++) {
                                                                            if (returning.charAt(k) == '\'') {
                                                                                quoteCtr++;
                                                                            }
                                                                        }
                                                                        if (quoteCtr != 2) {
                                                                            System.out.println("Quotes must be two.");
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                        if (returning.length() == 4 && !returning.contains("\\")) {
                                                                            System.out.println("Special case is not met.");
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                        operationDetected = false;
                                                                    } else {
                                                                        boolean variableExists = false;
                                                                        for (String s : fDef_DeclVar) {
                                                                            if (s.equals(returning)) {
                                                                                variableExists = true;
                                                                            }
                                                                        }
                                                                        if (!variableExists) {
                                                                            System.out.println("Return value is non-existent: " + returning);
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                        if (!returning.isEmpty()) {
                                                                            operationDetected = false;
                                                                        }
                                                                    }
                                                                }
                                                                sCurrLine = sCurrLine.trim();
                                                                System.out.println(sCurrLine + "(!)");
                                                                if (sCurrLine.charAt(0) != ';') { // We are expecting a return type with arithmetic expression/s.
                                                                    if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                                        operationDetected = true;
                                                                        sCurrLine = sCurrLine.substring(1);
                                                                        if (returning.isEmpty()) {
                                                                            System.out.println("Expected token.");
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        System.out.println("Whoa man. We expect an operation");
                                                                        terminate();
                                                                        break;
                                                                    }
                                                                }
                                                                System.out.println(sCurrLine + "(!)");

                                                                System.out.println(sCurrLine + "(!)");
                                                            }
                                                            System.out.println("readline is " + readLine);
                                                            if (valid) {
                                                                readLine = readLine.trim();
                                                                while (readLine.isEmpty()) {
                                                                    readLine = br.readLine();
                                                                    readLine = readLine.trim();
                                                                }
                                                                System.out.println("readline is " + readLine);
                                                            }
                                                        }
                                                        if (sCurrLine.charAt(0) == ',') {
                                                            sCurrLine = sCurrLine.substring(1);
                                                        }
                                                    }
                                                } else {
                                                    System.out.println("Not a var dec");
                                                    if (sCurrLine.charAt(0) == '=') {
                                                        sCurrLine = sCurrLine.substring(1);
                                                    }
                                                    int parentheses = 0;
                                                    String tempSCurrLine = "";
                                                    for (int j = 0; j < sCurrLine.length(); j++) {
                                                        System.out.println("s " + tempSCurrLine);
                                                        if (sCurrLine.charAt(j) == '(') {
                                                            parentheses++;
                                                            tempSCurrLine = tempSCurrLine.concat(" ");
                                                            if (sCurrLine.charAt(j + 1) != '(') {
                                                                j++;
                                                                tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                            }
                                                        } else if (sCurrLine.charAt(j) == ')') {
                                                            parentheses--;
                                                            tempSCurrLine = tempSCurrLine.concat(" ");
                                                        } else {
                                                            tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                        }
                                                        System.out.println(tempSCurrLine + " is temp");
                                                    }
                                                    sCurrLine = tempSCurrLine;
                                                    System.out.println(sCurrLine);
                                                    if (parentheses != 0) {
                                                        System.out.println("Expected parenthesis.");
                                                        terminate();
                                                        break;
                                                    }
                                                    System.out.println("Parenthesis cleared");
                                                    boolean operationDetected = false;
                                                    System.out.println("Valid: " + valid);
                                                    System.out.println("scurrlayn: " + sCurrLine);
                                                    while (valid && (sCurrLine.charAt(0) != ';' || operationDetected)) {
                                                        System.out.println("scurrline: " + sCurrLine);
                                                        String returning = "";
                                                        sCurrLine = sCurrLine.trim();
                                                        System.out.println("sCurrLine's content for god's sake: " + sCurrLine);
                                                        while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                            returning = returning.concat(sCurrLine.charAt(0) + "");
                                                            System.out.println("Sending " + sCurrLine.charAt(0));
                                                            sCurrLine = sCurrLine.substring(1);
                                                        }
                                                        try {
                                                            Integer.parseInt(returning); // Returned is a number which is valid.
                                                            System.out.println(sCurrLine + "(!)");
                                                            operationDetected = false;
                                                        } catch (NumberFormatException e) { // Returned is not a number.
                                                            if (returning.contains("'")) { // Might be a character...
                                                                if (returning.length() <= 2 || returning.length() >= 5) {
                                                                    System.out.println("Too much or too less character.");
                                                                    terminate();
                                                                    break;
                                                                }
                                                                int quoteCtr = 0;
                                                                for (int j = 0; j < returning.length(); j++) {
                                                                    if (returning.charAt(j) == '\'') {
                                                                        quoteCtr++;
                                                                    }
                                                                }
                                                                if (quoteCtr != 2) {
                                                                    System.out.println("Quotes must be two.");
                                                                    terminate();
                                                                    break;
                                                                }
                                                                if (returning.length() == 4 && !returning.contains("\\")) {
                                                                    System.out.println("Special case is not met.");
                                                                    terminate();
                                                                    break;
                                                                }
                                                                operationDetected = false;
                                                            } else {
                                                                boolean variableExists = false;
                                                                for (String s : fDef_DeclVar) {
                                                                    if (s.equals(returning)) {
                                                                        variableExists = true;
                                                                    }
                                                                }
                                                                if (!variableExists) {
                                                                    System.out.println("Return value is non-existent: " + returning);
                                                                    terminate();
                                                                    break;
                                                                }
                                                                if (!returning.isEmpty()) {
                                                                    operationDetected = false;
                                                                }
                                                            }
                                                        }
                                                        sCurrLine = sCurrLine.trim();
                                                        System.out.println(sCurrLine + "(!)");
                                                        if (sCurrLine.charAt(0) != ';') { // We are expecting a return type with arithmetic expression/s.
                                                            if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                                operationDetected = true;
                                                                sCurrLine = sCurrLine.substring(1);
                                                                if (returning.isEmpty()) {
                                                                    System.out.println("Expected token.");
                                                                    terminate();
                                                                    break;
                                                                }
                                                            } else {
                                                                System.out.println("Whoa man. We expect an operation");
                                                                terminate();
                                                                break;
                                                            }
                                                        }
                                                        System.out.println(sCurrLine + "(!)");

                                                        System.out.println(sCurrLine + "(!)");
                                                    }
                                                    System.out.println("readline is " + readLine);
                                                    if (valid) {
                                                        readLine = readLine.trim();
                                                        while (readLine.isEmpty()) {
                                                            readLine = br.readLine();
                                                            readLine = readLine.trim();
                                                        }
                                                        System.out.println("readline is " + readLine);
                                                    }
                                                }
                                                System.out.println(sCurrLine + " is scurr and " + varDec);
                                                /*if (!((varDec && sCurrLine.charAt(0) == ';') || sCurrLine.charAt(0) == '=' || sCurrLine.charAt(0) == ',')) {
                                                    System.out.println("haha dude are you trying to make me laugh?");
                                                    terminate();
                                                    break;
                                                }*/
                                            }
                                        }
                                        /*if (valid) {
                                            int parentheses = 0;
                                            String tempSCurrLine = "";
                                            for (int j = 0; j < sCurrLine.length(); j++) {
                                                System.out.println("s " + tempSCurrLine);
                                                if (sCurrLine.charAt(j) == '(') {
                                                    parentheses++;
                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                    if (sCurrLine.charAt(j + 1) != '(') {
                                                        j++;
                                                        tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                    }
                                                } else if (sCurrLine.charAt(j) == ')') {
                                                    parentheses--;
                                                    tempSCurrLine = tempSCurrLine.concat(" ");
                                                } else {
                                                    tempSCurrLine = tempSCurrLine.concat(sCurrLine.charAt(j) + "");
                                                }
                                                System.out.println(tempSCurrLine + " is temp");
                                            }
                                            sCurrLine = tempSCurrLine;
                                            System.out.println(sCurrLine);
                                            if (parentheses != 0) {
                                                System.out.println("Expected parenthesis.");
                                                terminate();
                                                break;
                                            }
                                            System.out.println("Parenthesis cleared");
                                            boolean operationDetected = false;
                                            System.out.println("Valid: " + valid);
                                            System.out.println("scurrlayn: " + sCurrLine);
                                            while (valid && (sCurrLine.charAt(0) != ';' || operationDetected)) {
                                                System.out.println("scurrline: " + sCurrLine);
                                                String returning = "";
                                                sCurrLine = sCurrLine.trim();
                                                System.out.println("sCurrLine's content for god's sake: " + sCurrLine);
                                                while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                    returning = returning.concat(sCurrLine.charAt(0) + "");
                                                    System.out.println("Sending " + sCurrLine.charAt(0));
                                                    sCurrLine = sCurrLine.substring(1);
                                                }
                                                try {
                                                    Integer.parseInt(returning); // Returned is a number which is valid.
                                                    System.out.println(sCurrLine + "(!)");
                                                    operationDetected = false;
                                                } catch (NumberFormatException e) { // Returned is not a number.
                                                    if (returning.contains("'")) { // Might be a character...
                                                        if (returning.length() <= 2 || returning.length() >= 5) {
                                                            System.out.println("Too much or too less character.");
                                                            terminate();
                                                            break;
                                                        }
                                                        int quoteCtr = 0;
                                                        for (int j = 0; j < returning.length(); j++) {
                                                            if (returning.charAt(j) == '\'') {
                                                                quoteCtr++;
                                                            }
                                                        }
                                                        if (quoteCtr != 2) {
                                                            System.out.println("Quotes must be two.");
                                                            terminate();
                                                            break;
                                                        }
                                                        if (returning.length() == 4 && !returning.contains("\\")) {
                                                            System.out.println("Special case is not met.");
                                                            terminate();
                                                            break;
                                                        }
                                                        operationDetected = false;
                                                    } else {
                                                        boolean variableExists = false;
                                                        for (String s : fDef_DeclVar) {
                                                            if (s.equals(returning)) {
                                                                variableExists = true;
                                                            }
                                                        }
                                                        if (!variableExists) {
                                                            System.out.println("Return value is non-existent: " + returning);
                                                            terminate();
                                                            break;
                                                        }
                                                        if (!returning.isEmpty()) {
                                                            operationDetected = false;
                                                        }
                                                    }
                                                }
                                                sCurrLine = sCurrLine.trim();
                                                System.out.println(sCurrLine + "(!)");
                                                if (sCurrLine.charAt(0) != ';') { // We are expecting a return type with arithmetic expression/s.
                                                    if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                        operationDetected = true;
                                                        sCurrLine = sCurrLine.substring(1);
                                                        if (returning.isEmpty()) {
                                                            System.out.println("Expected token.");
                                                            terminate();
                                                            break;
                                                        }
                                                    } else {
                                                        System.out.println("Whoa man. We expect an operation");
                                                        terminate();
                                                        break;
                                                    }
                                                }
                                                System.out.println(sCurrLine + "(!)");

                                                System.out.println(sCurrLine + "(!)");
                                            }
                                            System.out.println("readline is " + readLine);
                                            if (valid) {
                                                readLine = readLine.trim();
                                                while (readLine.isEmpty()) {
                                                    readLine = br.readLine();
                                                    readLine = readLine.trim();
                                                }
                                                System.out.println("readline is " + readLine);
                                            }
                                        }*/
                                        if (valid) {
                                            readLine = readLine.trim();
                                            while (readLine.isEmpty()) {
                                                readLine = br.readLine();
                                                readLine = readLine.trim();
                                            }
                                            System.out.println("readline is " + readLine);
                                        }
                                    }
                                    System.out.println("We found the ender!");
                                    if (!returnType.equals("void") && !returned) {
                                        valid = false;
                                        System.out.println("Ended without Return");
                                    }
                                    readLine = readLine.substring(1);
                                }
                            }
                        }
                    }
                }

                if (valid) {
                    wr.append("VALID ").append(type.toString());
                    System.out.println("VALID po");
                } else {
                    wr.append("INVALID ").append(type.toString());
                    System.out.println("INVALID po");
                }
                wr.newLine();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            // Before terminating, close all buffered readers and writers.
            try {
                br.close();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void terminate() {
        try {
            valid = false;
            wr.append("INVALID FUNCTION DEFINITION");
            System.out.println(readLine + " is readline");
            while (/*readLine == null || */readLine.length() == 0) {
                readLine = br.readLine();
            }
            while (readLine.charAt(0) != '}') {
                readLine = readLine.substring(1);
                while (readLine.length() == 0) {
                    readLine = br.readLine();
                }
            }
            readLine = readLine.substring(1);
        } catch (IOException e) {
            // do nothing
        }
    }

    private enum Type {
        FUNCTION_DECLARATION, FUNCTION_DEFINITION, VARIABLE_DECLARATION
    }
}
