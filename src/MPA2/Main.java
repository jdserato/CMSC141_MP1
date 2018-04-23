package MPA2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
                // TODO get regex and divide them.
                String regexRead = br.readLine(); // The regular expression (regex) is defined here.
                regexRead = "(".concat(regexRead).concat(")");
                List<String> content = Collections.singletonList(regexRead);
                Regex root = new Regex(content);

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

    private List<Regex> fragment(Regex src, String content) {
        List<Regex> children = new ArrayList<>();
        List<String> subcontent = new ArrayList<>();
        int parentheses = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (c == '(') {
                parentheses++;
            } else if (c == ')') {
                parentheses--;
            }
            if (parentheses == 0) {
                if (i + 1 < content.length() && content.charAt(i + 1) == '*') {
                    children.add(new Regex(subcontent, true, src));
                } else {
                    children.add(new Regex(subcontent, false, src));
                }
            }
        }
        return children;
    }
}
