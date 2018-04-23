package MPA2;

import junit.framework.TestCase;

/**
 * Created by Serato, Jay Vince on April 23, 2018.
 */
public class MainTest extends TestCase {
    public void test1() {
        assertEquals("[a b ]", Main.fragment(new Regex("aUb"), "aUb").toString());
    }
    public void test2() {
        assertEquals("[ab ]", Main.fragment(new Regex("ab"), "ab").toString());
    }
    public void test3() {
        assertEquals("[ab ]", Main.fragment(new Regex("(ab)*"), "(ab)*").toString());
    }
    public void test4() {
        assertEquals("[ab ]", Main.fragment(new Regex("(ab)(aaUbb)*"), "(ab)(aaUbb)*").toString());
    }
}