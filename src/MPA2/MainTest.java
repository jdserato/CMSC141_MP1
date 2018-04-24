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
        assertEquals("[ab ]", Main.fragment(new Regex("(ab)*(aaUbb)*"), "(ab)*(aaUbb)*").toString());
    }
    public void test5() {
        assertEquals("[ab ]", Main.fragment(new Regex("ab*"), "ab*").toString());
    }
    public void test6() {
        assertEquals("[ab , c ]", Main.fragment(new Regex("abc*"), "abc*").toString());
    }
    public void test7() {
        assertEquals("[ab , c ]", Main.fragment(new Regex("ab(cU(cd)*)*"), "ab(cU(cd)*)*").toString());
    }
    public void test8() {
        assertEquals("[ab , c ]", Main.fragment(new Regex("(cU(cd)*)*"), "(cU(cd)*)*").toString());
    }
}