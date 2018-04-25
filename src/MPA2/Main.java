package MPA2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serato, Jay Vince; Alvarez, Mary Michaelle; and Famat, Ruffa Mae on April 10, 2018.
 */
public class Main {
    private static final File INPUT = new File("mp3.in");
    private static Regex root;

    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(INPUT));
            int numberOfTestCases = Integer.parseInt(br.readLine()); // First line is defined to be the total number of test cases.

            while (numberOfTestCases-- > 0) {
                // TODO get regex and fragment them.
                String regexRead = br.readLine(); // The regular expression (regex) is defined here.
                regexRead = "(".concat(regexRead).concat(")");
                String newRegexRead = "";
                for (int i = 0; i < regexRead.length(); i++) { // This loop will get rid of spaces because they are of no use.
                    if (regexRead.charAt(i) != ' ') {
                        newRegexRead = newRegexRead.concat(regexRead.charAt(i) + "");
                    }
                }
                root = new Regex(newRegexRead);
                root = fragment(root, newRegexRead).get(0);
                System.out.println(root + " is root.");

                // TODO get the number of sample expressions
                int numberOfSampleExp = Integer.parseInt(br.readLine());

                // TODO identification of expressions
                while (numberOfSampleExp-- > 0) {
                    boolean accepted = true;
                    String testCase = br.readLine();
                    if (testCase.equals("e")) {
                        testCase = "";
                    }
                    Regex current = root;
                    current = current.clearAllDoneStatus(current);
                    while (testCase.length() > 0) {
                        System.out.println("Testcase: " + testCase);
                        while (current.getChildren() != null) {
                            System.out.println(current.getChildren().get(0) + "'s current inspected num: " + current.getChildrenInsp());
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
                        System.out.println("Current node we're at: " + current.getContents().get(0));
                        int maxLen = 0;
                        String partial;
                        if (current.getParent() != null && current.getParent().isUnion()) { // it's a union
                            //current = current.getParent();
                            for (String c : current.getContents()) {
                                System.out.println("Content: " + c);
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
                        System.out.println("Inspecting the partial testcase : " + partial);
                        boolean matched = false;
                        for (String s : current.getContents()) {
                            System.out.println("Equalizing " + s + " and " + partial);
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
                                System.out.println("Warning, not matched.");
                                if (current.getParent() != null && current.getParent().isUnion()) {
                                    System.out.println("Exiting union.");
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
                            System.out.println("Matched and we shall continue.");
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
                        System.out.println("NO");
                    } else {
                        System.out.println("YE");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File not found.");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    static List<Regex> fragment(Regex src, String content) {
        System.out.println("Content is " + content);
        List<Regex> children = new ArrayList<>();
        List<String> subcontent = new ArrayList<>();
        int parentheses = 0;
        boolean unioned = false;
        String current = "";
        for (int i = 0; i < content.length(); i++) { // Inspecting each and every character.
            char c = content.charAt(i);
            System.out.println("Scanning " + c);
            if (c == '(') { // Mapping out first-level parentheses.
                parentheses++;
                if (parentheses > 1) { // If it is not a first-level, save it for the next fragmentation.
                    current = current.concat(c + "");
                    System.out.println("New current at " + current);
                }
            } else if (c == ')') {
                parentheses--;
                if (parentheses >= 1) { // If it is not a first-level, save it for the next fragmentation.
                    current = current.concat(c + "");
                    System.out.println("New current at " + current);
                }
            } else if (!(parentheses == 0 && (c == 'U'|| c == '*'))){ // Save everything else.
                current = current.concat(c + "");
                System.out.println("New current at " + current);
            }
            if (parentheses == 0 || i == content.length() - 1) { // First-level inspection.
                System.out.println(c + " entered holy land.");
                if (i + 1 < content.length() && content.charAt(i + 1) == 'U') { // If union is detected,...
                    subcontent.add(current);
                    System.out.println("Unioned " + current);
                    current = "";
                    if (!unioned) {
                        unioned = true;
                    }
                } else if (unioned && i == content.length() - 1) { // If it has been unioned but it is the last character
                    subcontent.add(current);
                    System.out.println("Unioned " + current);
                } else {
                    // FIXME this should make sense to all of the following: a*b*, ab*, (ab) and abc*
                    if (i + 1 < content.length() && content.charAt(i + 1) == '*') {
                        i++;
                        if (c != ')') {
                            List<String> sub = new ArrayList<>();
                            String subs = current.substring(0, current.length() - 1);
                            if (current.length() != 1) {
                                sub.add(subs);
                                children.add(new Regex(sub, false, src));
                                System.out.println("ADDED " + subs + " at false.");
                            }
                            if (subs.contains("*") || subs.contains("U") || subs.contains("(")) {
                                children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), subs));
                            }
                            sub = new ArrayList<>();
                            subs = current.substring(current.length() - 1);
                            sub.add(subs);
                            children.add(new Regex(sub, true, src));
                            System.out.println("ADDED " + subs);
                            if (subs.contains("*") || subs.contains("U") || subs.contains("(")) {
                                children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), subs));
                            }
                        } else {
                            System.out.println("Multi presence detected. Ratifying " + current);
                            subcontent.add(current);
                            children.add(new Regex(subcontent, true, src));
                            if (current.contains("*") || current.contains("U") || current.contains("(")) {
                                System.out.println("Further simplifying...");
                                children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), current));
                            }
                        }
                        subcontent = new ArrayList<>();
                        current = "";
                    } else if (i + 1 == content.length() || content.charAt(i + 1) == '('){
                        System.out.println("Found EOF or open parenthesis. Current value: " + current);
                        subcontent.add(current);
                        children.add(new Regex(subcontent, false, src));
                        System.out.println(children + " is children.");
                        if (current.contains("*") || current.contains("U") || current.contains("(")) {
                            children.get(children.size() - 1).setChildren(fragment(children.get(children.size() - 1), current));
                        }
                        subcontent = new ArrayList<>();
                        current = "";
                    }
                }
            }
            System.out.println("Old current at " + current);
            System.out.println(children + " is children.");
        }
        if (!subcontent.isEmpty()) {
            System.out.println("Subcontent is empty.");
            children.add(new Regex(subcontent, false, src));
        }
        System.out.println(children + " is children.");
        return children;
    }
}
