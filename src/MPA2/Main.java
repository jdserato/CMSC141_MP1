package MPA2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serato, Jay Vince on April 10, 2018.
 */
public class Main {
    private static final File INPUT = new File("mp3.in");

    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(INPUT));
            int numberOfTestCases = Integer.parseInt(br.readLine()); // First line is defined to be the total number of test cases.

            while (numberOfTestCases-- > 0) {
                // TODO get regex and divide them.
                String regex = br.readLine(); // The regular expression (regex) is defined here.
                List<String> expressions = new ArrayList<>();
                String exp = "";
                for (int i = 0; i < regex.length(); i++) {
                    if (regex.charAt(i) == '*' || regex.charAt(i) == ')') {
                        if (exp.isEmpty()) {
                            // TODO shouldn't be. Awaiting for forum opening.
                        } else {
                            if (i + 1 < regex.length() && regex.charAt(i + 1) == '*') {
                                expressions.add(exp.concat(regex.charAt(i) + "*"));
                                i++;
                            } else {
                                expressions.add(exp.concat(regex.charAt(i) + ""));
                            }
                            exp = "";
                        }

                    } else {
                        exp = exp.concat(regex.charAt(i) + "");
                    }
                }
                if (!exp.isEmpty()) {
                    expressions.add(exp);
                }

                // TODO delete debug
                for (String s : expressions) {
                    System.out.println(s);
                }

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
}
