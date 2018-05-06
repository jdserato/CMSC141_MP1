package MPA2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serato, Jay Vince; Alvarez, Mary Michaelle; and Famat, Ruffa Mae on April 10, 2018.
 */
public class Main {
    private static final File INPUT = new File("mp3.in");
    private static final File OUTPUT = new File("AlvarezFamatSerato.out");

    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(INPUT));
            bw = new BufferedWriter(new FileWriter(OUTPUT));
            int numberOfTestCases = Integer.parseInt(br.readLine()); // First line is defined to be the total number of test cases.

            while (numberOfTestCases-- > 0) {
                String regexRead = br.readLine(); // The regular expression (regex) is defined here.
                regexRead = "(".concat(regexRead).concat(")");
                String newRegexRead = "";
                for (int i = 0; i < regexRead.length(); i++) { // This loop will get rid of spaces because they are of no use.
                    if (regexRead.charAt(i) != ' ') {
                        newRegexRead = newRegexRead.concat(regexRead.charAt(i) + "");
                    }
                }
                Regex root = new Regex(newRegexRead);
                root = fragment(root, newRegexRead).get(0);

                int numberOfSampleExp = Integer.parseInt(br.readLine());

                while (numberOfSampleExp-- > 0) {
                    boolean accepted = true;
                    String testCase = br.readLine();
                    if (testCase.equals("e")) {
                        testCase = "";
                    }
                    Regex current = root;
                    current = current.clearAllDoneStatus(current);
                    while (testCase.length() > 0) {
                        while (current.getChildren() != null) {
                            if (!current.isUnion()) {
                                current.setChildrenInsp(current.getChildrenInsp() + 1);
                            }
                            if (current.getChildrenInsp() > current.getChildren().size()) {
                                current.setDone(true);
                                current = current.getParent();
                            } else if (current.isUnion()) {
                                current = current.getChildren().get(0);
                            } else {
                                current = current.getChildren().get(current.getChildrenInsp() - 1);
                            }
                        }
                        int maxLen = 0;
                        String partial;
                        if (current.getParent() != null && current.getParent().isUnion()) { // it's a union
                            //current = current.getParent();
                            for (String c : current.getContents()) {
                                if (maxLen < c.length()) {
                                    maxLen = c.length();
                                }
                            }
                        } else { // a concatenation
                            maxLen = current.getContents().get(0).length();
                        }
                        if (maxLen > testCase.length()) {
                            partial = testCase;
                        } else {
                            partial = testCase.substring(0, maxLen);
                        }
                        boolean matched = false;
                        for (String s : current.getContents()) {
                            if (partial.equals(s)) {
                                matched = true;
                                break;
                            }
                        }
                        if (!matched || !current.isStar()) {
                            if (!current.isUnion()) {
                                current.setDone(true);
                            }
                            if (!matched) {
                                if (current.getParent() != null && current.getParent().isUnion()) {
                                    current = current.getParent();
                                    current.setDone(true);
                                }
                            }
                            if (current.getParent() != null) {
                                current = current.getParent();
                            } else {
                                accepted = false;
                                break;
                            }
                        }
                        if (matched) {
                            testCase = testCase.substring(maxLen);
                        }
                    }
                    //current = root;
                    if (current.getChildren() == null) {
                        current = root;
                    }
                    while (current != root) {
                        if (current.getChildren() != null && current.getChildren().size() > 0 && current.getChildrenInsp() >= current.getChildren().size()) {
                            current.setDone(true);
                        } else if (current.getChildren() != null && current.getChildren().size() == 0) {
                            if (!current.isDone() || !current.isStar()) {
                                accepted = false;
                            }
                        }
                        if (current.getChildren() != null && (!current.isDone() || !current.isStar())) {
                            current.setChildrenInsp(current.getChildrenInsp() + 1);
                            current = current.getChildren().get(current.getChildrenInsp() - 1);
                        } else {
                            current = current.getParent();
                        }
                    }
                    if (!accepted) {
                        bw.append("no");
                        bw.newLine();
                    } else {
                        bw.append("yes");
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("File not found.");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    static List<Regex> fragment(Regex src, String content) {
        List<Regex> children = new ArrayList<>();
        List<String> subcontent = new ArrayList<>();
        int parentheses = 0;
        boolean unioned = false;
        String current = "";
        for (int i = 0; i < content.length(); i++) { // Inspecting each and every character.
            char c = content.charAt(i);
            if (c == '(') { // Mapping out first-level parentheses.
                parentheses++;
                if (parentheses > 1) { // If it is not a first-level, save it for the next fragmentation.
                    current = current.concat(c + "");
                }
            } else if (c == ')') {
                parentheses--;
                if (parentheses >= 1) { // If it is not a first-level, save it for the next fragmentation.
                    current = current.concat(c + "");
                }
            } else if (!(parentheses == 0 && (c == 'U'|| c == '*'))){ // Save everything else.
                current = current.concat(c + "");
            }
            if (parentheses == 0 || i == content.length() - 1) { // First-level inspection.
                if (i + 1 < content.length() && content.charAt(i + 1) == 'U') { // If union is detected,...
                    subcontent.add(current);
                    current = "";
                    if (!unioned) {
                        unioned = true;
                    }
                } else if (unioned && i == content.length() - 1) { // If it has been unioned but it is the last character
                    subcontent.add(current);
                } else {
                    if (i + 1 < content.length() && content.charAt(i + 1) == '*') {
                        i++;
                        if (c != ')') {
                            List<String> sub = new ArrayList<>();
                            String subs = current.substring(0, current.length() - 1);
                            if (current.length() != 1) {
                                sub.add(subs);
                                children.add(new Regex(sub, false, src));
                            }
                            if (subs.contains("*") || subs.contains("U") || subs.contains("(")) {
                                children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), subs));
                            }
                            sub = new ArrayList<>();
                            subs = current.substring(current.length() - 1);
                            sub.add(subs);
                            children.add(new Regex(sub, true, src));
                            if (subs.contains("*") || subs.contains("U") || subs.contains("(")) {
                                children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), subs));
                            }
                        } else {
                            subcontent.add(current);
                            children.add(new Regex(subcontent, true, src));
                            if (current.contains("*") || current.contains("U") || current.contains("(")) {
                                children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), current));
                            }
                        }
                        subcontent = new ArrayList<>();
                        current = "";
                    } else if (i + 1 == content.length() || content.charAt(i + 1) == '('){
                        subcontent.add(current);
                        children.add(new Regex(subcontent, false, src));
                        if (current.contains("*") || current.contains("U") || current.contains("(")) {
                            children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), current));
                        }
                        subcontent = new ArrayList<>();
                        current = "";
                    }
                }
            }
        }
        if (!subcontent.isEmpty()) {
            children.add(new Regex(subcontent, false, src));
        }
        return children;
    }
}
