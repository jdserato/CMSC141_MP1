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
                for (int i = 0; i < regexRead.length(); i++) {
                    if (regexRead.charAt(i) != ' ') {
                        newRegexRead = newRegexRead.concat(regexRead.charAt(i) + "");
                    }
                }
                Regex root = new Regex(newRegexRead);

                // TODO get the number of sample expressions
                int numberOfSampleExp = Integer.parseInt(br.readLine());

                // TODO identification of expressions
                while (numberOfSampleExp-- > 0) {
                    br.readLine();
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
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            System.out.println("Scanning " + c);
            if (c == '(') {
                parentheses++;
                if (parentheses > 1) {

                    current = current.concat(c + "");
                    System.out.println("New current at " + current);
                }
            } else if (c == ')') {
                parentheses--;
                if (parentheses >= 1) {
                    current = current.concat(c + "");
                    System.out.println("New current at " + current);
                }
            } else if (!(parentheses == 0 && (c == 'U'|| c == '*'))){
                current = current.concat(c + "");
                System.out.println("New current at " + current);
            }
            if (parentheses == 0 || i == content.length() - 1) {
                System.out.println(c + " entered holy land.");
                if (i + 1 < content.length() && content.charAt(i + 1) == 'U') {
                    if (!unioned) {
                        unioned = true;
                        subcontent.add(current);
                        System.out.println("Unioned " + current);
                        current = "";
                    }
                } else if (unioned && i == content.length() - 1) {
                    subcontent.add(current);
                    System.out.println("Unioned " + current);
                } else {
                    // FIXME this should make sense to all of the following: a*b*, ab*, (ab) and abc*
                    if (i + 1 < content.length() && content.charAt(i + 1) == '*') {
                        i++;
                        if (c != ')') {
                            List<String> sub = new ArrayList<>();
                            String subs = current.substring(0, current.length() - 1);
                            sub.add(subs);
                            children.add(new Regex(sub, false, src));
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
                        subcontent = new ArrayList<>();
                        System.out.println(children + " is children.");
                        current = "";
                        System.out.println(children + " is children.");
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
