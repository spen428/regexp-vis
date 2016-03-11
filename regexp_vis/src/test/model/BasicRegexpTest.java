package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.BasicRegexp;
import model.InvalidRegexpException;

@SuppressWarnings({ "unused", "static-method" })
public class BasicRegexpTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArg0() {
        new BasicRegexp(new ArrayList<>(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArg1() {
        new BasicRegexp(new BasicRegexp('0'), null);
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid1() throws Throwable {
        BasicRegexp.parseRegexp("sdf(jjj))");
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid2() throws Throwable {
        BasicRegexp.parseRegexp("|a");
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid3() throws Throwable {
        BasicRegexp.parseRegexp("a|");
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid4() throws Throwable {
        BasicRegexp.parseRegexp("*");
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid5() throws Throwable {
        BasicRegexp.parseRegexp("+");
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid6() throws Throwable {
        BasicRegexp.parseRegexp("?");
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid7() throws Throwable {
        BasicRegexp.parseRegexp("sdf(jjj");
    }

    @Test(expected = InvalidRegexpException.class)
    public void testParseRegexp_invalid8() throws Throwable {
        // Invalid, Stefan's Java applet doesn't like this either, not
        // particularly useful to allow it
        BasicRegexp.parseRegexp("()");
    }

    @Test
    public void testParseRegexp_valid1() throws InvalidRegexpException {
        // This is valid, but should return null
        BasicRegexp re = BasicRegexp.parseRegexp("");
        assertEquals(re, null);
    }

    @Test
    public void testParseRegexp_valid2() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("(a)(b)(c)(d)");
        assertEquals(re.toString(), "abcd");
    }

    @Test
    public void testParseRegexp_valid3() throws InvalidRegexpException {
        // Verify spaces are ignored
        BasicRegexp re = BasicRegexp.parseRegexp("a b c d");
        assertEquals(re.toString(), "abcd");
    }

    @Test
    public void testParseRegexp_valid4() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("sdf**");
        assertEquals(re.toString(), "sdf**");
    }

    @Test
    public void testParseRegexp_valid5() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("(sdf)*+");
        assertEquals(re.toString(), "(sdf)*+");
    }

    @Test
    public void testParseRegexp_valid6() throws InvalidRegexpException {
        // Test deeper nesting of parentheses
        BasicRegexp re = BasicRegexp.parseRegexp("ab(cd(e))ff");
        assertEquals(re.toString(), "abcdeff");
    }

    @Test
    public void testParseRegexp_valid7() throws InvalidRegexpException {
        // Test a unary operator on single char, there indeed was a bug
        // relating to this...
        BasicRegexp re = BasicRegexp.parseRegexp("abc*def");
        assertEquals(re.toString(), "abc*def");
    }

    @Test
    public void testParseRegexp_valid8() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("(sdf)**");
        assertEquals(re.toString(), "(sdf)**");
    }

    @Test
    public void testParseRegexp_valid9() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("(sdf)*?");
        assertEquals(re.toString(), "(sdf)*?");
    }

    @Test
    public void testParseRegexp_correctness1() throws InvalidRegexpException {
        String testRegexp = "(01|10)*1111";
        BasicRegexp re = BasicRegexp.parseRegexp(testRegexp);

        // (01|10)*1111
        assertEquals(re.getOperator(), BasicRegexp.RegexpOperator.SEQUENCE);
        assertEquals(re.getOperands().size(), 5);

        // (01|10)*
        BasicRegexp reSubExpr_0 = re.getOperands().get(0);
        assertEquals(reSubExpr_0.getOperator(),
                BasicRegexp.RegexpOperator.STAR);
        assertEquals(reSubExpr_0.getOperands().size(), 1);

        // 01|10
        BasicRegexp reSubExpr_0_0 = reSubExpr_0.getOperands().get(0);
        assertEquals(reSubExpr_0_0.getOperator(),
                BasicRegexp.RegexpOperator.CHOICE);
        assertEquals(reSubExpr_0_0.getOperands().size(), 2);

        {
            // 01
            BasicRegexp reSubExpr_0_0_0 = reSubExpr_0_0.getOperands().get(0);
            assertEquals(reSubExpr_0_0_0.getOperator(),
                    BasicRegexp.RegexpOperator.SEQUENCE);
            assertEquals(reSubExpr_0_0_0.getOperands().size(), 2);

            BasicRegexp reSubExpr_0_0_0_0 = reSubExpr_0_0_0.getOperands()
                    .get(0);
            assertEquals(reSubExpr_0_0_0_0.getOperator(),
                    BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_0_0.isSingleChar());
            assertEquals(reSubExpr_0_0_0_0.getChar(), '0');

            BasicRegexp reSubExpr_0_0_0_1 = reSubExpr_0_0_0.getOperands()
                    .get(1);
            assertEquals(reSubExpr_0_0_0_1.getOperator(),
                    BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_0_1.isSingleChar());
            assertEquals(reSubExpr_0_0_0_1.getChar(), '1');
        }

        {
            // 10
            BasicRegexp reSubExpr_0_0_1 = reSubExpr_0_0.getOperands().get(1);
            assertEquals(reSubExpr_0_0_1.getOperator(),
                    BasicRegexp.RegexpOperator.SEQUENCE);
            assertEquals(reSubExpr_0_0_1.getOperands().size(), 2);

            BasicRegexp reSubExpr_0_0_1_0 = reSubExpr_0_0_1.getOperands()
                    .get(0);
            assertEquals(reSubExpr_0_0_1_0.getOperator(),
                    BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_1_0.isSingleChar());
            assertEquals(reSubExpr_0_0_1_0.getChar(), '1');

            BasicRegexp reSubExpr_0_0_1_1 = reSubExpr_0_0_1.getOperands()
                    .get(1);
            assertEquals(reSubExpr_0_0_1_1.getOperator(),
                    BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_1_1.isSingleChar());
            assertEquals(reSubExpr_0_0_1_1.getChar(), '0');
        }

        // 1
        BasicRegexp reSubExpr_1 = re.getOperands().get(1);
        assertEquals(reSubExpr_1.getOperator(),
                BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_1.isSingleChar());
        assertEquals(reSubExpr_1.getChar(), '1');

        // 1
        BasicRegexp reSubExpr_2 = re.getOperands().get(2);
        assertEquals(reSubExpr_2.getOperator(),
                BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_2.isSingleChar());
        assertEquals(reSubExpr_2.getChar(), '1');

        // 1
        BasicRegexp reSubExpr_3 = re.getOperands().get(3);
        assertEquals(reSubExpr_3.getOperator(),
                BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_3.isSingleChar());
        assertEquals(reSubExpr_3.getChar(), '1');

        // 1
        BasicRegexp reSubExpr_4 = re.getOperands().get(4);
        assertEquals(reSubExpr_4.getOperator(),
                BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_4.isSingleChar());
        assertEquals(reSubExpr_4.getChar(), '1');
    }

    @Test
    public void testParseRegexp_correctness1new()
            throws InvalidRegexpException {
        // New version of testParseRegexp_correctness1, now that
        // BasicRegexp.toString is implemented, maybe remove the old one
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)*1111");
        assertEquals(re.toString(), "(01|10)*1111");
    }

    @Test
    public void testParseRegexp_correctness2() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)*11*11*");
        assertEquals(re.toString(), "(01|10)*11*11*");
    }

    @Test
    public void testParseRegexp_correctness3() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)*1(1|0)1");
        assertEquals(re.toString(), "(01|10)*1(1|0)1");
    }

    @Test
    public void testParseRegexp_correctness4() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)**abc?+*");
        assertEquals(re.toString(), "(01|10)**abc?+*");
    }

    @Test
    public void testParseRegexp_correctness5() throws InvalidRegexpException {
        BasicRegexp re = BasicRegexp.parseRegexp("abc?+*|abc?+*");
        assertEquals(re.toString(), "abc?+*|abc?+*");
    }

    @Test
    public void testOptimise_1() throws InvalidRegexpException {
        // Test combinations of iteration simplifying down to a single STAR
        BasicRegexp re1 = BasicRegexp.parseRegexp("a**");
        BasicRegexp re2 = BasicRegexp.parseRegexp("a+*");
        BasicRegexp re3 = BasicRegexp.parseRegexp("a*+");
        BasicRegexp re4 = BasicRegexp.parseRegexp("a*********************");
        BasicRegexp re5 = BasicRegexp.parseRegexp("a++++++++++++++++++++*");
        BasicRegexp re6 = BasicRegexp.parseRegexp("a????????????????????*");
        BasicRegexp re7 = BasicRegexp.parseRegexp("a********************?");
        BasicRegexp re8 = BasicRegexp.parseRegexp("a+?*+?*+?*");
        BasicRegexp re9 = BasicRegexp.parseRegexp("a+?");
        BasicRegexp re10 = BasicRegexp.parseRegexp("a?+");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re2.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re3.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re4.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re5.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re6.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re7.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re8.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re9.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re10.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
    }

    @Test
    public void testOptimise_2() throws InvalidRegexpException {
        // Test combinations of iteration simplifying down to a single PLUS
        BasicRegexp re1 = BasicRegexp.parseRegexp("a+++++++++++++++++++++");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a+");
    }

    @Test
    public void testOptimise_3() throws InvalidRegexpException {
        // Test simplifying parts of SEQUENCE
        // Test single character expressions get simplified
        BasicRegexp re1 = BasicRegexp.parseRegexp("a*a*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("a*a?");
        BasicRegexp re3 = BasicRegexp.parseRegexp("a?a*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("a?a?a?a?a*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("a?a*a*a?a*a?a?a?a*a*a?a*a?");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re2.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re3.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re4.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
        assertEquals(re5.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*");
    }

    @Test
    public void testOptimise_4() throws InvalidRegexpException {
        // Test simplifying parts of SEQUENCE
        // Test complex expressions get simplified
        BasicRegexp re1 = BasicRegexp.parseRegexp("(a|b)*(a|b)*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("(a|b)*(a|b)?");
        BasicRegexp re3 = BasicRegexp.parseRegexp("(a|b)?(a|b)*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("(a|b)+(a|b)*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("(a|b)*(a|b)+");
        BasicRegexp re6 = BasicRegexp.parseRegexp("(a|b)+(a|b)?");
        BasicRegexp re7 = BasicRegexp.parseRegexp("(a|b)?(a|b)+");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)*");
        assertEquals(re2.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)*");
        assertEquals(re3.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)*");
        assertEquals(re4.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)+");
        assertEquals(re5.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)+");
        assertEquals(re6.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)+");
        assertEquals(re7.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)+");
    }

    @Test
    public void testOptimise_5() throws InvalidRegexpException {
        // Test simplifying parts of SEQUENCE
        // Test different char prevents oversimplifying, recognise a*b* can't
        // be simplified to a* for example
        BasicRegexp re1 = BasicRegexp.parseRegexp("a*b*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("a*b?");
        BasicRegexp re3 = BasicRegexp.parseRegexp("a?b*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("a?a?b?a?a*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("a?a*a*a?a*b?a?a?a*b*a?a*a?");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*b*");
        assertEquals(re2.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*b?");
        assertEquals(re3.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a?b*");
        assertEquals(re4.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a?a?b?a*");
        assertEquals(re5.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*b?a*b*a*");
    }

    @Test
    public void testOptimise_6() throws InvalidRegexpException {
        // Test removing duplicate parts of CHOICE, verify ordering is
        // maintained
        BasicRegexp re1 = BasicRegexp.parseRegexp("a|a|b");
        BasicRegexp re2 = BasicRegexp.parseRegexp("b|a|a");
        BasicRegexp re3 = BasicRegexp.parseRegexp("(b|b)*|(a|a)*|a*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("(c|b|b)*|(a|a)*|a*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("a*b*|a*b*");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a|b");
        assertEquals(re2.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "b|a");
        assertEquals(re3.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "b*|a*");
        assertEquals(re4.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(c|b)*|a*");
        assertEquals(re5.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "a*b*");
    }

    @Test
    public void testOptimise_7() throws InvalidRegexpException {
        // Test more complex iteration simplification, tests the
        // optimiseStarOnSC method
        BasicRegexp re1 = BasicRegexp.parseRegexp("(a*b*)*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("((a|d)*b*c?)*");
        BasicRegexp re3 = BasicRegexp.parseRegexp("(abc)*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("((a*|b*)b*c*)*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("((a*b*)*b*c*)*");
        BasicRegexp re6 = BasicRegexp.parseRegexp("((ab)*b*c*)*");
        BasicRegexp re7 = BasicRegexp.parseRegexp("((a|b)*b*|c*)*");
        BasicRegexp re8 = BasicRegexp.parseRegexp("((a|b)+b?|c)*");
        BasicRegexp re9 = BasicRegexp.parseRegexp("(a*bc*)*");
        BasicRegexp re10 = BasicRegexp.parseRegexp("((a*|b*)+b?|c)*");
        BasicRegexp re11 = BasicRegexp.parseRegexp("(abc**)*");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)*");
        assertEquals(re2.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|d|b|c)*");
        assertEquals(re3.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(abc)*");
        assertEquals(re4.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b|c)*");
        assertEquals(re5.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b|c)*");
        assertEquals(re6.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(ab|b|c)*");
        assertEquals(re7.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b|c)*");
        // Can't optimise further
        assertEquals(re8.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "((a|b)+b?|c)*");
        // Can't optimise further
        assertEquals(re9.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a*bc*)*");
        assertEquals(re10.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b|c)*");
        assertEquals(re11.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(abc*)*");
    }

    @Test
    public void testOptimise_8() throws InvalidRegexpException {
        // Test optimisations realise different orderings of CHOICE are
        // equivalent
        BasicRegexp re1 = BasicRegexp.parseRegexp("(a|b)*(b|a)*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("(c|d)*|(d|c)*");

        assertEquals(re1.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(a|b)*");
        assertEquals(re2.optimise(BasicRegexp.OPTIMIZE_ALL, -1).toString(),
                "(c|d)*");

    }

    @Test
    public void testDebugPrintBasicRegexp() throws InvalidRegexpException {
        // Can't mark methods to be ignored by EclEmma coverage tool, this test
        // is a workaround
        BasicRegexp re1 = BasicRegexp.parseRegexp("(01|10)*1111");
        BasicRegexp.debugPrintBasicRegexp(0, re1);
    }

    @Test
    public void testCompareTo() throws InvalidRegexpException {
        BasicRegexp re1 = BasicRegexp.parseRegexp("(01|10)*1111");
        BasicRegexp re2 = BasicRegexp.parseRegexp("(01|10)*1111");
        BasicRegexp re3 = BasicRegexp
                .parseRegexp("((a*|b*)+b?|c)*def((g*|h*)+h?|i)*");
        BasicRegexp re4 = BasicRegexp
                .parseRegexp("((a*|b*)+b?|c)*def((g*|h*)+h?|i)*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("(01|10)*111");

        // Test equality, simpler
        assertEquals(re1.compareTo(re2), 0);
        // Test equality, more complex
        assertEquals(re3.compareTo(re4), 0);
        // Test ordering
        assertTrue(re5.compareTo(re1) < 0);
        assertTrue(re1.compareTo(re5) > 0);
    }

    @Test
    public void test00() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("\" K2v2't|La");
        basicRegexp0.getOperator();
    }

    @Test
    public void test01() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('[');
        basicRegexp0.getChar();
    }

    @Test
    public void test02() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('T');
        basicRegexp0.getChar();
    }

    @Test
    public void test03() throws Throwable {
        BasicRegexp.parseRegexp("X%h-6@?");
    }

    @Test
    public void test04() throws Throwable {
        // Undeclared exception!
        try {
            BasicRegexp.parseRegexp((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test05() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.PLUS;
        BasicRegexp basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                basicRegexp_RegexpOperator0);
        // Undeclared exception!
        try {
            BasicRegexp.debugPrintBasicRegexp((-1881), basicRegexp0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test06() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp(" lw/L");
        // Undeclared exception!
        try {
            basicRegexp0.compareTo((BasicRegexp) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test07() throws Throwable {
        BasicRegexp basicRegexp0 = null;
        try {
            basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                    (BasicRegexp.RegexpOperator) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test08() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('o');
        BasicRegexp.debugPrintBasicRegexp((-1), basicRegexp0);
    }

    @Test
    public void test09() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.PLUS;
        BasicRegexp basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                basicRegexp_RegexpOperator0);
        // Undeclared exception!
        try {
            basicRegexp0.optimise(BasicRegexp.OPTIMIZE_ALL, -1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test10() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('#');
        basicRegexp0.optimise(BasicRegexp.OPTIMIZE_ALL, -1);
    }

    @Test
    public void test11() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.OPTION;
        BasicRegexp basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                basicRegexp_RegexpOperator0);
        // Undeclared exception!
        try {
            basicRegexp0.toString();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test12() throws Throwable {
        BasicRegexp.parseRegexp("");
    }

    @Test
    public void test13() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("4[75N*VdA#Z2");
        basicRegexp0.isNullable();
    }

    @Test
    public void test14() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('#');
        basicRegexp0.isNullable();
    }

    @Test
    public void test15() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("\" K2v2't|La");
        basicRegexp0.isSingleChar();
    }

    @Test
    public void test16() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('#');
        basicRegexp0.isSingleChar();
    }

    @Test
    public void test17() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('[');
        basicRegexp0.compareTo(basicRegexp0);
    }

    @Test
    public void test18() throws Throwable {
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.CHOICE;
        arrayList0.add((BasicRegexp) null);
        BasicRegexp basicRegexp0 = null;
        try {
            basicRegexp0 = new BasicRegexp(arrayList0,
                    basicRegexp_RegexpOperator0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test19() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('#');
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.SEQUENCE;
        arrayList0.add(basicRegexp0);
        BasicRegexp basicRegexp1 = new BasicRegexp(arrayList0,
                basicRegexp_RegexpOperator0);
    }

    @Test
    public void test20() throws Throwable {
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.SEQUENCE;
        BasicRegexp basicRegexp0 = null;
        try {
            basicRegexp0 = new BasicRegexp(arrayList0,
                    basicRegexp_RegexpOperator0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No operands passed
            //
        }
    }

    @Test
    public void test21() throws Throwable {
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        arrayList0.add((BasicRegexp) null);
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.OPTION;
        BasicRegexp basicRegexp0 = new BasicRegexp(arrayList0,
                basicRegexp_RegexpOperator0);
        basicRegexp0.getOperator();
    }

    @Test
    public void test22() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.SEQUENCE;
        basicRegexp_RegexpOperator0.isUnary();
    }

    @Test
    public void test23() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.PLUS;
        basicRegexp_RegexpOperator0.getPrecedence();
    }

    @Test
    public void test24() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("t X5N]");
        BasicRegexp.debugPrintBasicRegexp((-1), basicRegexp0);
    }

    @Test
    public void test26() throws Throwable {
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        arrayList0.add((BasicRegexp) null);
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.OPTION;
        BasicRegexp basicRegexp0 = new BasicRegexp(arrayList0,
                basicRegexp_RegexpOperator0);
        // Undeclared exception!
        try {
            basicRegexp0.optimise(BasicRegexp.OPTIMIZE_ALL, -1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test27() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("Fv+2~{z3`=5");
        basicRegexp0.optimise(BasicRegexp.OPTIMIZE_ALL, -1);
    }

    @Test
    public void test28() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("3xi-+P*0D");
        basicRegexp0.optimise(BasicRegexp.OPTIMIZE_ALL, -1);
    }

    @Test
    public void test29() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("\" K2v2't|La");
        basicRegexp0.toString();
    }

    @Test
    public void test30() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("Fv+2~{z3`=5");
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.OPTION;
        arrayList0.add(basicRegexp0);
        BasicRegexp basicRegexp1 = new BasicRegexp(arrayList0,
                basicRegexp_RegexpOperator0);
        basicRegexp1.toString();
    }

    @Test
    public void test31() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("Fv+2~{z3`=5");
        basicRegexp0.toString();
    }

    @Test
    public void test32() throws Throwable {
        BasicRegexp.parseRegexp("8");
    }

    @Test
    public void test33() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("3xi-+P*0D");
        basicRegexp0.toString();
    }

    @Test
    public void test34() throws Throwable {
        try {
            BasicRegexp.parseRegexp("tB)5N]");
            fail("Expecting exception: InvalidRegexpException");
        } catch (InvalidRegexpException e) {
            //
            // Stray closing parenthesis found
            //
        }
    }

    @Test
    public void test35() throws Throwable {
        BasicRegexp.parseRegexp("!|f%7yP=F!LNf_`");
    }

    @Test
    public void test36() throws Throwable {
        try {
            BasicRegexp.parseRegexp("|");
            fail("Expecting exception: InvalidRegexpException");
        } catch (InvalidRegexpException e) {
            //
            // CHOICE operator on empty word
            //
        }
    }

    @Test
    public void test37() throws Throwable {
        try {
            BasicRegexp.parseRegexp("?,uE;K");
            fail("Expecting exception: InvalidRegexpException");
        } catch (InvalidRegexpException e) {
            //
            // OPTION operator on empty word
            //
        }
    }

    @Test
    public void test38() throws Throwable {
        try {
            BasicRegexp.parseRegexp(
                    "Cannot call getOperands() on a single character expression");
            fail("Expecting exception: InvalidRegexpException");
        } catch (InvalidRegexpException e) {
            //
            // Empty parentheses found
            //
        }
    }

    @Test
    public void test39() throws Throwable {
        try {
            BasicRegexp.parseRegexp("g;.VP;(/;(v@THTf$");
            fail("Expecting exception: InvalidRegexpException");
        } catch (InvalidRegexpException e) {
            //
            // Unclosed parenthesis found
            //
        }
    }

    @Test
    public void test40() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("Fv+2~{z3`=5");
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.OPTION;
        arrayList0.add(basicRegexp0);
        BasicRegexp basicRegexp1 = new BasicRegexp(arrayList0,
                basicRegexp_RegexpOperator0);
        boolean boolean0 = basicRegexp1.isNullable();
        assertTrue(boolean0);
    }

    @Test
    public void test41() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.PLUS;
        BasicRegexp basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                basicRegexp_RegexpOperator0);
        // Undeclared exception!
        try {
            basicRegexp0.isNullable();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }

    @Test
    public void test42() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.STAR;
        BasicRegexp basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                basicRegexp_RegexpOperator0);
        boolean boolean0 = basicRegexp0.isNullable();
        assertTrue(boolean0);
    }

    @Test
    public void test43() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("\" K2v2't|La");
        boolean boolean0 = basicRegexp0.isNullable();
        assertFalse(boolean0);
    }

    @Test
    public void test44() throws Throwable {
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        arrayList0.add((BasicRegexp) null);
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.STAR;
        BasicRegexp basicRegexp0 = new BasicRegexp(arrayList0,
                basicRegexp_RegexpOperator0);
        // Undeclared exception!
        try {
            basicRegexp0.getChar();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // getChar() must only be called on single character expressions
            //
        }
    }

    @Test
    public void test45() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('6');
        char char0 = basicRegexp0.getChar();
        assertEquals('6', char0);
    }

    @Test
    public void test46() throws Throwable {
        BasicRegexp basicRegexp0 = new BasicRegexp('d');
        // Undeclared exception!
        try {
            basicRegexp0.getOperands();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Cannot call getOperands() on a single character expression
            //
        }
    }

    @Test
    public void test47() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("t X5N]");
        assertFalse(basicRegexp0.isSingleChar());
        List<BasicRegexp> list0 = basicRegexp0.getOperands();
        assertEquals(5, list0.size());
    }

    @Test
    public void test48() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("_w8Hd<p0");
        BasicRegexp basicRegexp1 = BasicRegexp.parseRegexp("_w8Hd<p0");
        boolean boolean0 = basicRegexp1.equals(basicRegexp0);
        assertTrue(boolean0);
        assertFalse(basicRegexp1.isSingleChar());
    }

    @Test
    public void test49() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("\" K2v2't|La");
        boolean boolean0 = basicRegexp0.equals((Object) null);
        assertFalse(boolean0);
    }

    @Test
    public void test50() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("t X5N]");
        boolean boolean0 = basicRegexp0.equals(basicRegexp0);
        assertTrue(boolean0);
    }

    @Test
    public void test51() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("Fv+2~{z3`=5");
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        boolean boolean0 = basicRegexp0.equals(arrayList0);
        assertFalse(boolean0);
    }

    @Test
    public void test52() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("t X5N]");
        int int0 = basicRegexp0.compareTo(basicRegexp0);
        assertEquals(0, int0);
        assertFalse(basicRegexp0.isSingleChar());
    }

    @Test
    public void test53() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.CHOICE;
        BasicRegexp basicRegexp0 = null;
        try {
            basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                    basicRegexp_RegexpOperator0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Non-unary operators require multiple operands
            //
        }
    }

    @Test
    public void test54() throws Throwable {
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.NONE;
        BasicRegexp basicRegexp0 = null;
        try {
            basicRegexp0 = new BasicRegexp((BasicRegexp) null,
                    basicRegexp_RegexpOperator0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // RegexpOperator.NONE only allowed for single character expressions
            //
        }
    }

    @Test
    public void test55() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("\" K2v2't|La");
        BasicRegexp basicRegexp1 = basicRegexp0.optimise(
                BasicRegexp.OPTIMIZE_ALL, -1);
        assertSame(basicRegexp1, basicRegexp0);
    }

    @Test
    public void test56() throws Throwable {
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.OPTION;
        arrayList0.add((BasicRegexp) null);
        arrayList0.add((BasicRegexp) null);
        BasicRegexp basicRegexp0 = null;
        try {
            basicRegexp0 = new BasicRegexp(arrayList0,
                    basicRegexp_RegexpOperator0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Multiple operands passed for a unary operator
            //
        }
    }

    @Test
    public void test57() throws Throwable {
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.NONE;
        BasicRegexp basicRegexp0 = null;
        try {
            basicRegexp0 = new BasicRegexp(arrayList0,
                    basicRegexp_RegexpOperator0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // RegexpOperator.NONE only allowed for single character expressions
            //
        }
    }

    @Test
    public void test58() throws Throwable {
        BasicRegexp basicRegexp0 = BasicRegexp.parseRegexp("Fv+2~{z3`=5");
        ArrayList<BasicRegexp> arrayList0 = new ArrayList<>();
        BasicRegexp.RegexpOperator basicRegexp_RegexpOperator0 = BasicRegexp.RegexpOperator.OPTION;
        arrayList0.add(basicRegexp0);
        BasicRegexp basicRegexp1 = new BasicRegexp(arrayList0,
                basicRegexp_RegexpOperator0);
        int int0 = basicRegexp1.compareTo(basicRegexp0);
        assertEquals((-1), int0);
    }

}
