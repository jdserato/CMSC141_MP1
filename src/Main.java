import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by Alvarez, Mary Michaelle; Famat, Ruffa Mae; and Serato, Jay Vince on February 05, 2018.
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
            // Putting the INPUT and output files in a buffered reader and writer respectively.
            br = new BufferedReader(new FileReader(INPUT_FILE));
            wr = new BufferedWriter(new FileWriter(OUTPUT_FILE));

            // Identifying the number of types (found in the first line of the INPUT_FILE).
            try {
                types = Integer.parseInt(br.readLine());
            } catch (InputMismatchException e) {
                // do nothing
            }
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

                sCurrLine = sCurrLine.trim(); // Removing redundant spaces, if any are present
                // Checking its type whether its a variable declaration, a function declaration, or function definition.
                if (sCurrLine.charAt(sCurrLine.length() - 1) == ';') { // The type is either a variable or a function declaration.
                    if (sCurrLine.contains("(") && sCurrLine.contains(")")) { // The type is likely a function declaration.
                        type = Type.FUNCTION_DECLARATION;
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
                    }
                } else if (sCurrLine.contains("(") && sCurrLine.contains(")") && sCurrLine.charAt(sCurrLine.length() - 1) == '{') { // The type is likely a function declaration.
                    type = Type.FUNCTION_DEFINITION;
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
                    if (type == Type.FUNCTION_DEFINITION) {
                        terminate();
                    } else {
                        //wr.append("INVALID ").append(type.toString());
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
                    String var_type = null;
                    String var_name = null;
                    String value = null;
                    boolean extracted_type = false;
                    boolean extracted_var = false;
                    boolean extracted_value = false;
                    boolean valid_type = true;
                    boolean valid_name = true;
                    boolean valid_value = true;
                    boolean declaration = false;
                    int new_index = 0;

                    if(sCurrLine.contains("=")){
                        if(sCurrLine.contains(",")){
                            String [] array = new String[20];
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    valid_type = checkTypeIfValid(var_type);

                                    if(valid_type){
                                        int store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        int cntr = 0;
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == ';' ){
                                                array[cntr] = var_name;
                                                extracted_var = true;
                                            }else{
                                                if(sCurrLine.charAt(new_index) == ','){
                                                    array[cntr] = var_name;
                                                    cntr++;
                                                    store_index = new_index+1;
                                                    new_index++;
                                                }else{
                                                    var_name = sCurrLine.substring(store_index, new_index +1);
                                                    new_index++;
                                                }


                                            }
                                        }
                                        if(extracted_var){
                                            boolean separate;
                                            int cntr2 = 0;
                                            String value2 = null;
                                            String temp;
                                            for(int a = 0; a <= array.length; a++){
                                                separate = false;
                                                var_name = array[a];
                                                if(var_name == null){
                                                    break;
                                                }
                                                if(var_name.contains("=")){
                                                    temp = var_name;
                                                    while(!separate){
                                                        if(temp.charAt(cntr2) == '='){
                                                            var_name = temp.substring(0, cntr2);
                                                            value2 = temp.substring(cntr2+1, temp.length());
                                                            separate = true;
                                                        }else{
                                                            var_name = temp.substring(0, cntr2+1);
                                                            cntr2++;
                                                        }
                                                    }
                                                    valid_name = checkNameIfValid(var_name);
                                                    valid_value = checkValueIfValid(var_type,value2);

                                                    if(valid_name && valid_value){
                                                        declaration = true;
                                                    }else{
                                                        declaration = false;
                                                        break;
                                                    }

                                                }else{
                                                    valid_name = checkNameIfValid(var_name);
                                                    if(valid_name){
                                                        declaration = true;
                                                    }else{
                                                        declaration = false;
                                                        break;
                                                    }
                                                }


                                            }

                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                    }


                                }
                            }

                        }else{
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    int store_index = 0;
                                    valid_type = checkTypeIfValid(var_type);
                                    if(valid_type){
                                        store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == '=' ){
                                                extracted_var = true;
                                            }else{
                                                var_name = sCurrLine.substring(store_index, new_index +1);
                                                new_index++;
                                            }
                                        }
                                        if(extracted_var){
                                            valid_name = checkNameIfValid(var_name);
                                            if(valid_name){
                                                store_index = new_index+1;
                                                new_index = store_index;
                                                while(!extracted_value){
                                                    if(sCurrLine.charAt(new_index) == ';'){
                                                        extracted_value = true;
                                                    }else{
                                                        value = sCurrLine.substring(store_index, new_index +1);
                                                        new_index++;
                                                    }

                                                }
                                                if(extracted_value){
                                                    valid_value = checkValueIfValid(var_type, value);

                                                    if(valid_value){

                                                        declaration = true;

                                                    }else{
                                                        declaration = false;
                                                    }
                                                }

                                            }else{
                                                declaration = false;
                                            }
                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                        break;
                                    }
                                }

                            }
                        }

                    }else{

                        String[] array = new String[20];
                        if(sCurrLine.contains(",")){
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    valid_type = checkTypeIfValid(var_type);

                                    if(valid_type){
                                        int store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        int cntr = 0;
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == ';' ){
                                                array[cntr] = var_name;
                                                extracted_var = true;
                                            }else{
                                                if(sCurrLine.charAt(new_index) == ','){
                                                    array[cntr] = var_name;
                                                    cntr++;
                                                    store_index = new_index+1;
                                                    new_index++;
                                                }else{
                                                    var_name = sCurrLine.substring(store_index, new_index +1);
                                                    new_index++;
                                                }


                                            }
                                        }
                                        if(extracted_var){
                                            for(int a = 0; a <= cntr; a++){
                                                var_name = array[a];
                                                valid_name = checkNameIfValid(var_name);
                                                if(valid_name){
                                                    declaration = true;
                                                }else{
                                                    declaration = false;
                                                }

                                            }


                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                    }


                                }
                            }


                        }else{
                            for(int index = 0; index < sCurrLine.length(); index++){
                                //variable type
                                while(!extracted_type){
                                    if(sCurrLine.charAt(index) == ' '){
                                        extracted_type = true;
                                    }else{
                                        if(sCurrLine.charAt(index) == ';'){
                                            declaration = false;
                                            break;
                                        }else {
                                            var_type = sCurrLine.substring(0, index + 1);
                                            index++;
                                        }
                                    }
                                }
                                if(extracted_type){
                                    valid_type = checkTypeIfValid(var_type);

                                    if(valid_type){
                                        int store_index  = index + 1;
                                        new_index = store_index;
                                        //variable name
                                        while(!extracted_var){
                                            if(sCurrLine.charAt(new_index) == ';' ){
                                                extracted_var = true;
                                            }else{
                                                var_name = sCurrLine.substring(store_index, new_index +1);
                                                new_index++;
                                            }
                                        }
                                        if(extracted_var){
                                            valid_name = checkNameIfValid(var_name);
                                            if(valid_name){
                                                declaration = true;
                                            }else{
                                                declaration = false;
                                            }
                                            break;
                                        }

                                    }else{
                                        declaration = false;
                                        break;
                                    }
                                }

                            }
                        }
                    }
                    if (declaration) {
                        valid = true;
                    } else { valid = false;
                    }
                } else if (valid && type == Type.FUNCTION_DECLARATION) { // TODO FUNCTION DECLARATION (MM, start here)

                    for (; sCurrLine.charAt(i) == ' '; i++);

                    if (!(sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z'))) {
                        //terminate(type);
                        valid = false;
                    } else {
                        String functionName = sCurrLine.charAt(i) + ""; //istore sa functionName ang kana na character

                        for (i++; sCurrLine.charAt(i) != '(' && sCurrLine.charAt(i) != ' '; i++) { //name ra jud ni sa function way apil ang after sa parenthesis
                            if (sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z') || (sCurrLine.charAt(i) >= '0' && sCurrLine.charAt(i) <= '9')) {
                                functionName = functionName.concat(sCurrLine.charAt(i) + "");
                            } else {
                                //terminate(type);
                                valid = false;
                                break;
                            }
                        }

                        //mana ko here
                        if (valid) { //valid ang pag name sa function;
                            for (; sCurrLine.charAt(i) != '('; i++){
                                if (i + 1 == sCurrLine.length()) {
                                    //terminate(type);
                                    valid = false;
                                    break;
                                } else if (sCurrLine.charAt(i) != ' ') {
                                    //terminate(type);
                                    valid = false;
                                    break;
                                }
                            }

                            if (valid){ //ang scurrLine kay naa na sa (
                                while (sCurrLine.charAt(i) != ')'){

                                    String param = "";
                                    for (i++; sCurrLine.charAt(i) != ',' && sCurrLine.charAt(i) != ')'; i++){
                                        param = param.concat(sCurrLine.charAt(i) + "");

                                    }
                                    param = param.trim();

                                    if (param.length() == 0 && sCurrLine.charAt(i) != ')') {
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
                                        valid = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else if (valid) { // TODO FUNCTION DEFINITION (Vince, start here)
                    String funcName = sCurrLine.charAt(i) + "";
                    // Inspecting function/variable name
                    for (; sCurrLine.charAt(i) == ' '; i++);
                    if (!(sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z'))) {
                        terminate();
                    } else {
                        String funcDefName = sCurrLine.charAt(i) + "";
                        for (i++; sCurrLine.charAt(i) != '(' && sCurrLine.charAt(i) != ' '; i++) {
                            funcName = funcName.concat(sCurrLine.charAt(i) + "");
                            if (sCurrLine.charAt(i) == '_' || (sCurrLine.charAt(i) >= 'A' && sCurrLine.charAt(i) <= 'Z') || (sCurrLine.charAt(i) >= 'a' && sCurrLine.charAt(i) <= 'z') || (sCurrLine.charAt(i) >= '0' && sCurrLine.charAt(i) <= '9')) {
                                funcDefName = funcDefName.concat(sCurrLine.charAt(i) + "");
                            } else {
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
                                terminate();
                            }
                            for (; sCurrLine.charAt(i) != '('; i++){
                                if (i + 1 == sCurrLine.length()) {
                                    terminate();
                                    break;
                                } else if (sCurrLine.charAt(i) != ' ') {
                                    terminate();
                                    break;
                                }
                            }
                            if (valid) {
                                i++;
                                while (sCurrLine.charAt(i) != ')') {
                                    String param = "";
                                    boolean comma = false;
                                    if (sCurrLine.charAt(i) == ',') {
                                        i++;
                                        comma = true;
                                    }
                                    for (; sCurrLine.charAt(i) != ',' && sCurrLine.charAt(i) != ')'; i++) {
                                        param = param.concat(sCurrLine.charAt(i) + "");
                                    }
                                    param = param.trim();
                                    if (param.length() == 0 && comma) {
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
                                                    terminate();
                                                } else {
                                                    fDef_DeclVar.add(parameter_name);
                                                }
                                            }
                                            for (String s : fDef_DeclVar) {
                                            }
                                        }

                                    }
                                }
                                if (valid) {
                                    for (i++; sCurrLine.charAt(i) != '{'; i++) {
                                        if (sCurrLine.charAt(i) != ' ') {
                                            terminate();
                                            break;
                                        }
                                    }
                                }
                                if (valid) {
                                    while (readLine.isEmpty()) {
                                        readLine = br.readLine();
                                    }
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
                                                readLine = br.readLine();
                                            }
                                        }
                                        sCurrLine = sCurrLine.trim();
                                        sCurrLine = sCurrLine.concat(";");
                                        readLine = readLine.substring(1);
                                        if (sCurrLine.startsWith("return ")) {
                                            returned = true;
                                            sCurrLine = sCurrLine.substring(7);
                                            int parentheses = 0;
                                            String tempSCurrLine = "";
                                            for (int j = 0; j < sCurrLine.length(); j++) {
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
                                            }
                                            sCurrLine = tempSCurrLine;
                                            if (parentheses != 0) {
                                                terminate();
                                                break;
                                            }
                                            boolean operationDetected = false;
                                            while (valid && (sCurrLine.charAt(0) != ';' || operationDetected)) {
                                                String returning = "";
                                                sCurrLine = sCurrLine.trim();
                                                while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                    returning = returning.concat(sCurrLine.charAt(0) + "");
                                                    sCurrLine = sCurrLine.substring(1);
                                                }
                                                try {
                                                    Integer.parseInt(returning); // Returned is a number which is valid.
                                                    operationDetected = false;
                                                } catch (NumberFormatException e) { // Returned is not a number.
                                                    if (returning.contains("'")) { // Might be a character...
                                                        if (returning.length() <= 2 || returning.length() >= 5) {
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
                                                            terminate();
                                                            break;
                                                        }
                                                        if (returning.length() == 4 && !returning.contains("\\")) {
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
                                                            terminate();
                                                            break;
                                                        }
                                                        if (!returning.isEmpty()) {
                                                            operationDetected = false;
                                                        }
                                                    }
                                                }
                                                sCurrLine = sCurrLine.trim();
                                                if (sCurrLine.charAt(0) != ';') { // We are expecting a return type with arithmetic expression/s.
                                                    if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                        operationDetected = true;
                                                        sCurrLine = sCurrLine.substring(1);
                                                        if (returning.isEmpty()) {
                                                            terminate();
                                                            break;
                                                        }
                                                    } else {
                                                        terminate();
                                                        break;
                                                    }
                                                }
                                            }
                                            if (valid) {
                                                readLine = readLine.trim();
                                                while (readLine.isEmpty()) {
                                                    readLine = br.readLine();
                                                    readLine = readLine.trim();
                                                }
                                            }
                                        } else {
                                            String identifier = "";
                                            while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != '=') {
                                                if (sCurrLine.charAt(0) == ';') {
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
                                                if (varDec) {
                                                    boolean commad = false;
                                                    while (sCurrLine.charAt(0) != ';') {
                                                        if (commad) {
                                                            commad = false;
                                                        }
                                                        if (sCurrLine.charAt(0) == '=' || sCurrLine.charAt(0) == ',') {
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
                                                            terminate();
                                                            break;
                                                        }
                                                        if (!(identifier.charAt(0) == '_' || (identifier.charAt(0) >= 'A' && identifier.charAt(0) <= 'Z') || (identifier.charAt(0) >= 'a' && identifier.charAt(0) <= 'z'))) {
                                                            terminate();
                                                            break;
                                                        }
                                                        for (int j = 1; j < identifier.length(); j++) {
                                                            if (!(identifier.charAt(j) == '_' || (identifier.charAt(j) >= 'A' && identifier.charAt(j) <= 'Z') || (identifier.charAt(j) >= 'a' && identifier.charAt(j) <= 'z') || (identifier.charAt(j) >= '0' && identifier.charAt(j) <= '9'))) {
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
                                                                terminate();
                                                            } else {
                                                                fDef_DeclVar.add(identifier);
                                                            }
                                                        }
                                                        if (sCurrLine.charAt(0) == '=') {
                                                            sCurrLine = sCurrLine.substring(1);
                                                            int k;
                                                            for (k = 0; k < sCurrLine.length() && sCurrLine.charAt(k) != ';' && sCurrLine.charAt(k) != ','; k++);
                                                            String storedsline = sCurrLine.substring(k);
                                                            int parentheses = 0;
                                                            String tempSCurrLine = "";
                                                            int j;
                                                            for (j = 0; j < sCurrLine.length() && sCurrLine.charAt(j) != ',' && sCurrLine.charAt(j) != ';'; j++) {
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
                                                            }
                                                            sCurrLine = tempSCurrLine;
                                                            sCurrLine = sCurrLine.concat(",");
                                                            if (parentheses != 0) {
                                                                terminate();
                                                                break;
                                                            }
                                                            boolean operationDetected = false;
                                                            while (valid && ((sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != ',') || operationDetected)) {
                                                                String returning = "";
                                                                sCurrLine = sCurrLine.trim();
                                                                while (sCurrLine.charAt(0) != ',' && sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                                    returning = returning.concat(sCurrLine.charAt(0) + "");
                                                                    sCurrLine = sCurrLine.substring(1);
                                                                }
                                                                try {
                                                                    Integer.parseInt(returning); // Returned is a number which is valid.
                                                                    operationDetected = false;
                                                                } catch (NumberFormatException e) { // Returned is not a number.
                                                                    if (returning.contains("'")) { // Might be a character...
                                                                        if (returning.length() <= 2 || returning.length() >= 5) {
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                        int quoteCtr = 0;
                                                                        for (int l = 0; l < returning.length(); l++) {
                                                                            if (returning.charAt(l) == '\'') {
                                                                                quoteCtr++;
                                                                            }
                                                                        }
                                                                        if (quoteCtr != 2) {
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                        if (returning.length() == 4 && !returning.contains("\\")) {
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
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                        if (!returning.isEmpty()) {
                                                                            operationDetected = false;
                                                                        }
                                                                    }
                                                                }
                                                                sCurrLine = sCurrLine.trim();
                                                                if (sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != ',') { // We are expecting a return type with arithmetic expression/s.
                                                                    if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                                        operationDetected = true;
                                                                        sCurrLine = sCurrLine.substring(1);
                                                                        if (returning.isEmpty()) {
                                                                            terminate();
                                                                            break;
                                                                        }
                                                                    } else {
                                                                        terminate();
                                                                        break;
                                                                    }
                                                                }
                                                            }

                                                            sCurrLine = storedsline;
                                                        }
                                                        sCurrLine = sCurrLine.trim();
                                                        if (sCurrLine.charAt(0) == ',') {
                                                            sCurrLine = sCurrLine.substring(1);
                                                            commad = true;
                                                        }
                                                    }
                                                    if (commad && varDec) {
                                                        terminate();
                                                        break;
                                                    }
                                                } else {
                                                    if (sCurrLine.charAt(0) == '=') {
                                                        sCurrLine = sCurrLine.substring(1);
                                                    }
                                                    int parentheses = 0;
                                                    String tempSCurrLine = "";
                                                    for (int j = 0; j < sCurrLine.length(); j++) {
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
                                                    }
                                                    sCurrLine = tempSCurrLine;
                                                    if (parentheses != 0) {
                                                        terminate();
                                                        break;
                                                    }
                                                    boolean operationDetected = false;
                                                    while (valid && (sCurrLine.charAt(0) != ';' || operationDetected)) {
                                                        String returning = "";
                                                        sCurrLine = sCurrLine.trim();
                                                        while (sCurrLine.charAt(0) != ' ' && sCurrLine.charAt(0) != ';' && sCurrLine.charAt(0) != '+' && sCurrLine.charAt(0) != '/' && sCurrLine.charAt(0) != '-' && sCurrLine.charAt(0) != '*') {
                                                            returning = returning.concat(sCurrLine.charAt(0) + "");
                                                            sCurrLine = sCurrLine.substring(1);
                                                        }
                                                        try {
                                                            Integer.parseInt(returning); // Returned is a number which is valid.
                                                            operationDetected = false;
                                                        } catch (NumberFormatException e) { // Returned is not a number.
                                                            if (returning.contains("'")) { // Might be a character...
                                                                if (returning.length() <= 2 || returning.length() >= 5) {
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
                                                                    terminate();
                                                                    break;
                                                                }
                                                                if (returning.length() == 4 && !returning.contains("\\")) {
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
                                                                    terminate();
                                                                    break;
                                                                }
                                                                if (!returning.isEmpty()) {
                                                                    operationDetected = false;
                                                                }
                                                            }
                                                        }
                                                        sCurrLine = sCurrLine.trim();
                                                        if (sCurrLine.charAt(0) != ';') { // We are expecting a return type with arithmetic expression/s.
                                                            if (sCurrLine.charAt(0) == '+' || sCurrLine.charAt(0) == '/' || sCurrLine.charAt(0) == '-' || sCurrLine.charAt(0) == '*') {
                                                                operationDetected = true;
                                                                sCurrLine = sCurrLine.substring(1);
                                                                if (returning.isEmpty()) {
                                                                    terminate();
                                                                    break;
                                                                }
                                                            } else {
                                                                terminate();
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (valid) {
                                                        readLine = readLine.trim();
                                                        while (readLine.isEmpty()) {
                                                            readLine = br.readLine();
                                                            readLine = readLine.trim();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (valid) {
                                            readLine = readLine.trim();
                                            while (readLine.isEmpty()) {
                                                readLine = br.readLine();
                                                readLine = readLine.trim();
                                            }
                                        }
                                    }
                                    if (!returnType.equals("void") && !returned) {
                                        valid = false;
                                        readLine = readLine.substring(1);
                                    }
                                    if (valid) {
                                        readLine = readLine.substring(1);
                                    }
                                }
                            }
                        }
                    }
                }
                String testCaseType = "";
                for (int j = 0; j < type.toString().length(); j++) {
                    if (type.toString().charAt(j) == '_') {
                        testCaseType = testCaseType.concat(" ");
                    } else {
                        testCaseType = testCaseType.concat(type.toString().charAt(j) + "");
                    }
                }
                if (valid) {
                    wr.append("VALID ").append(testCaseType);
                } else {
                    wr.append("INVALID ").append(testCaseType);
                }
                wr.newLine();
            }
        } catch (IOException e) {
            // do nothing
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
            while (readLine.length() == 0) {
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

    //check if the declared value is valid
    private static boolean checkValueIfValid(String type, String value){
        boolean isValid = false;
        if(value == null){
            isValid = false;
        }else {
            boolean not_empty = false;
            for(int i = 0; i < value.length(); i++){
                if(value.charAt(i)!= ' '){
                    not_empty = true;
                }
            }
            if(not_empty){
                int s = 0;
                if (value.charAt(0) == ' ') {
                    while (value.charAt(s) == ' ') {
                        s++;
                    }
                    value = value.substring(s, value.length());
                }
                int z = value.length() - 1;
                if (value.charAt(z) == ' ') {
                    while (value.charAt(z) == ' ') {
                        z--;
                    }
                    value = value.substring(0, z + 1);

                }

                if (type.equals("int")) {
                    if (value.contains("'")) {
                        if (value.length() == 3 && value.charAt(0) == '\'') {
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            }else{
                                isValid = false;
                            }
                        } else {
                            isValid = false;
                        }
                    } else {
                        if(value.length() == 1){
                            if(value.charAt(0) == '-'){
                                isValid = false;
                            }else{
                                if(value.charAt(0) >= '0' && value.charAt(0) <= '9'){
                                    isValid = true;
                                }else{
                                    isValid = false;
                                }
                            }

                        }else{

                            if(value.charAt(0) == '-'){
                                int i = 1;
                                boolean not_space = true;
                                while(not_space){
                                    if(value.charAt(i) != ' '){
                                        value = value.substring(i, value.length());
                                        not_space = false;
                                    }else{
                                        i++;
                                    }
                                }
                                for(int j = 0; j < value.length(); j++){
                                    if(value.charAt(j) >= '0' && value.charAt(j) <= '9'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }

                                if(isValid){
                                    value = "-" + value;
                                }
                            }else{
                                for(int i = 0; i < value.length(); i++){
                                    if(value.charAt(i) >= '0' && value.charAt(i) <= '9'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                } else if (type.equals("float")) {
                    if (value.contains("'")) {
                        if (value.length() == 3 && value.charAt(0) == '\'') {
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            }else{
                                isValid = false;
                            }
                        } else {
                            isValid = false;
                        }
                    } else {
                        int cnt = 0;
                        for (int i = 0; i < value.length(); i++) {
                            if (value.charAt(i) == '.') {
                                cnt++;
                            }
                        }

                        if (cnt > 1) {
                            return false;
                        } else {
                            if(value.charAt(0) == '-'){
                                int i = 1;
                                boolean not_space = true;
                                while(not_space){
                                    if(value.charAt(i) != ' '){
                                        value = value.substring(i, value.length());
                                        not_space = false;
                                    }else{
                                        i++;
                                    }
                                }
                                for(int j = 0; j < value.length(); j++){
                                    if((value.charAt(j) >= '0' && value.charAt(j) <= '9') || value.charAt(j) == '.'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }

                                if(isValid){
                                    value = "-" + value;
                                }
                            }else {
                                for (int i = 0; i < value.length(); i++) {
                                    if ((value.charAt(i) >= '0' && value.charAt(i) <= '9') || value.charAt(i) == '.') {
                                        isValid = true;
                                    } else {
                                        isValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                    }

                } else if (type.equals("char")) {
                    if (value.contains("'")){
                        if((value.length() == 3) && (value.charAt(0) == '\'')){
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            } else{
                                isValid = false;
                            }
                        }else{
                            isValid = false;
                        }
                    }else{
                        for(int i = 0; i < value.length(); i++){
                            if(value.charAt(i) >= '0' && value.charAt(i) <= '9'){
                                isValid = true;
                            }else{
                                isValid = false;
                                break;
                            }
                        }
                    }

                } else { //means it's a double type
                    if (value.contains("'")) {
                        if (value.length() == 3 && value.charAt(0) == '\'') {
                            if(value.charAt(2) == '\''){
                                isValid = true;
                            }else{
                                isValid = false;
                            }
                        } else {
                            isValid = false;
                        }
                    } else {
                        int cnt = 0;
                        for (int i = 0; i < value.length(); i++) {
                            if (value.charAt(i) == '.') {
                                cnt++;
                            }
                        }

                        if (cnt > 1) {
                            return false;
                        } else {
                            if(value.charAt(0) == '-'){
                                int i = 1;
                                boolean not_space = true;
                                while(not_space){
                                    if(value.charAt(i) != ' '){
                                        value = value.substring(i, value.length());
                                        not_space = false;
                                    }else{
                                        i++;
                                    }
                                }
                                for(int j = 0; j < value.length(); j++){
                                    if((value.charAt(j) >= '0' && value.charAt(j) <= '9') || value.charAt(j) == '.'){
                                        isValid = true;
                                    }else{
                                        isValid = false;
                                        break;
                                    }
                                }

                                if(isValid){
                                    value = "-" + value;
                                }
                            }else {
                                for (int i = 0; i < value.length(); i++) {
                                    if ((value.charAt(i) >= '0' && value.charAt(i) <= '9') || value.charAt(i) == '.') {
                                        isValid = true;
                                    } else {
                                        isValid = false;
                                        break;
                                    }
                                }

                            }
                        }
                    }

                }
            }else{
                isValid = false;
            }


        }
        if(!(type.equals("char"))){
            if(isValid){
                isValid = checkRangeIfValid(type, value);
            }
        }

        return isValid;
    }
    private static boolean checkRangeIfValid(String type, String value){
        int isInteger;
        float isFloat;
        double isDouble;
        //try catch
        if (value.contains("'")) {
            if (value.length() == 3 && value.charAt(0) == '\'') {
                if(value.charAt(2) == '\''){
                    return true;
                }else{
                    return false;
                }
            } else {
                return false;
            }
        }
        else{
            if(type.equals("int")){
                try {
                    isInteger = Integer.parseInt(value);
                    return true;
                }catch  (Exception e){
                    return false;
                }

            }else if(type.equals("float")){
                try {
                    isFloat = Float.parseFloat(value);
                    return true;
                }catch  (Exception e){
                    return false;
                }
            }else { //double
                try {
                    isDouble = Double.parseDouble(value);
                    return true;
                }catch  (Exception e){
                    return false;
                }
            }
        }


    }
    //check if the declared type is valid
    private static boolean checkTypeIfValid(String type){
        if(type.equals("int")){
            return true;
        }else if(type.equals("float")){
            return true;
        }else if(type.equals("char")){
            return true;
        }else if(type.equals("double")){
            return true;
        }else{
            return false;
        }
    }

    //check if the declared variable name is valid
    private static boolean checkNameIfValid(String name){
        boolean isValid = false;
        if(name == null){
            isValid = false;
        }else {
            boolean not_empty = false;
            for(int i = 0; i < name.length(); i++){
                if(name.charAt(i)!= ' '){
                    not_empty = true;
                }
            }if(not_empty){
                int s = 0;

                if (name.charAt(0) == ' ') {
                    while (name.charAt(s) == ' ') {
                        s++;
                    }
                    name = name.substring(s, name.length());
                }
                int z = name.length() - 1;
                if (name.charAt(z) == ' ') {
                    while (name.charAt(z) == ' ') {
                        z--;
                    }
                    name = name.substring(0, z + 1);

                }
                if(!((name.equals("int")) || (name.equals("float")) || (name.equals("double") || (name.equals("char"))))){
                    for (int i = 0; i < name.length(); i++) {
                        if (i == 0) {
                            if ((name.charAt(i) >= 'a' && name.charAt(i) <= 'z') || (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z') || (name.charAt(i) == '_')) {
                                isValid = true;
                            } else {
                                isValid = false;
                                break;
                            }
                        } else {
                            if ((name.charAt(i) >= 'a' && name.charAt(i) <= 'z') || (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z') || (name.charAt(i) == '_') || (name.charAt(i) >= '0' && name.charAt(i) <= '9')) {
                                isValid = true;
                            } else {
                                isValid = false;
                                break;
                            }
                        }

                    }
                }else{
                    isValid = false;
                }

            }else{
                isValid = false;
            }
        }
        return isValid;
    }

    private enum Type {
        FUNCTION_DECLARATION, FUNCTION_DEFINITION, VARIABLE_DECLARATION
    }
}