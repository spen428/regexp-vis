package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.BasicRegexp;
import model.InvalidRegexpException;

public class BasicRegexpTest {

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testParseRegexp_invalid1()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("sdf(jjj))");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_invalid2()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("|a");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_invalid3()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("a|");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_invalid4()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("*");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_invalid5()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("+");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_invalid6()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("?");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_invalid7()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("sdf(jjj");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_invalid8()
    {
        boolean caught = false;
        try {
            // Invalid, Stefan's Java applet doesn't like this either, not
            // particularly useful to allow it
            BasicRegexp.parseRegexp("()");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void testParseRegexp_valid1()
        throws InvalidRegexpException
    {
        // This is valid, but should return null
        BasicRegexp re = BasicRegexp.parseRegexp("");
        assertEquals(re, null);
    }
    
    @Test
    public void testParseRegexp_valid2()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("(a)(b)(c)(d)");
        assertEquals(re.toString(), "abcd");
    }
    
    @Test
    public void testParseRegexp_valid3()
        throws InvalidRegexpException
    {
        // Verify spaces are ignored
        BasicRegexp re = BasicRegexp.parseRegexp("a b c d");
        assertEquals(re.toString(), "abcd");
    }
    
    @Test
    public void testParseRegexp_valid4()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("sdf**");
        assertEquals(re.toString(), "sdf**");
    }    
    
    @Test
    public void testParseRegexp_valid5()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("(sdf)*+");
        assertEquals(re.toString(), "(sdf)*+");
    }
    

    @Test
    public void testParseRegexp_valid6()
        throws InvalidRegexpException
    {
        // Test deeper nesting of parentheses
        BasicRegexp re = BasicRegexp.parseRegexp("ab(cd(e))ff");
        assertEquals(re.toString(), "abcdeff");
    }

    @Test
    public void testParseRegexp_valid7()
        throws InvalidRegexpException
    {
        // Test a unary operator on single char, there indeed was a bug 
        // relating to this...
        BasicRegexp re = BasicRegexp.parseRegexp("abc*def");
        assertEquals(re.toString(), "abc*def");
    }

    @Test
    public void testParseRegexp_valid8()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("(sdf)**");
        assertEquals(re.toString(), "(sdf)**");
    }

    @Test
    public void testParseRegexp_valid9()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("(sdf)*?");
        assertEquals(re.toString(), "(sdf)*?");
    }

    @Test
    public void testParseRegexp_correctness1()
        throws InvalidRegexpException
    {
        String testRegexp = "(01|10)*1111";
        BasicRegexp re = BasicRegexp.parseRegexp(testRegexp);

        // (01|10)*1111
        assertEquals(re.getOperator(), BasicRegexp.RegexpOperator.SEQUENCE);
        assertEquals(re.getOperands().size(), 5);

        // (01|10)*
        BasicRegexp reSubExpr_0 = re.getOperands().get(0);
        assertEquals(reSubExpr_0.getOperator(), BasicRegexp.RegexpOperator.STAR);
        assertEquals(reSubExpr_0.getOperands().size(), 1);

        // 01|10
        BasicRegexp reSubExpr_0_0 = reSubExpr_0.getOperands().get(0);
        assertEquals(reSubExpr_0_0.getOperator(), BasicRegexp.RegexpOperator.CHOICE);
        assertEquals(reSubExpr_0_0.getOperands().size(), 2);

        {
            // 01
            BasicRegexp reSubExpr_0_0_0 = reSubExpr_0_0.getOperands().get(0);
            assertEquals(reSubExpr_0_0_0.getOperator(), BasicRegexp.RegexpOperator.SEQUENCE);
            assertEquals(reSubExpr_0_0_0.getOperands().size(), 2);

            BasicRegexp reSubExpr_0_0_0_0 = reSubExpr_0_0_0.getOperands().get(0);
            assertEquals(reSubExpr_0_0_0_0.getOperator(), BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_0_0.isSingleChar());
            assertEquals(reSubExpr_0_0_0_0.getChar(), '0');

            BasicRegexp reSubExpr_0_0_0_1 = reSubExpr_0_0_0.getOperands().get(1);
            assertEquals(reSubExpr_0_0_0_1.getOperator(), BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_0_1.isSingleChar());
            assertEquals(reSubExpr_0_0_0_1.getChar(), '1');
        }

        {
            // 10
            BasicRegexp reSubExpr_0_0_1 = reSubExpr_0_0.getOperands().get(1);
            assertEquals(reSubExpr_0_0_1.getOperator(), BasicRegexp.RegexpOperator.SEQUENCE);
            assertEquals(reSubExpr_0_0_1.getOperands().size(), 2);

            BasicRegexp reSubExpr_0_0_1_0 = reSubExpr_0_0_1.getOperands().get(0);
            assertEquals(reSubExpr_0_0_1_0.getOperator(), BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_1_0.isSingleChar());
            assertEquals(reSubExpr_0_0_1_0.getChar(), '1');

            BasicRegexp reSubExpr_0_0_1_1 = reSubExpr_0_0_1.getOperands().get(1);
            assertEquals(reSubExpr_0_0_1_1.getOperator(), BasicRegexp.RegexpOperator.NONE);
            assertTrue(reSubExpr_0_0_1_1.isSingleChar());
            assertEquals(reSubExpr_0_0_1_1.getChar(), '0');
        }

        // 1
        BasicRegexp reSubExpr_1 = re.getOperands().get(1);
        assertEquals(reSubExpr_1.getOperator(), BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_1.isSingleChar());
        assertEquals(reSubExpr_1.getChar(), '1');

        // 1
        BasicRegexp reSubExpr_2 = re.getOperands().get(2);
        assertEquals(reSubExpr_2.getOperator(), BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_2.isSingleChar());
        assertEquals(reSubExpr_2.getChar(), '1');

        // 1
        BasicRegexp reSubExpr_3 = re.getOperands().get(3);
        assertEquals(reSubExpr_3.getOperator(), BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_3.isSingleChar());
        assertEquals(reSubExpr_3.getChar(), '1');

        // 1
        BasicRegexp reSubExpr_4 = re.getOperands().get(4);
        assertEquals(reSubExpr_4.getOperator(), BasicRegexp.RegexpOperator.NONE);
        assertTrue(reSubExpr_4.isSingleChar());
        assertEquals(reSubExpr_4.getChar(), '1');
    }

    @Test
    public void testParseRegexp_correctness1new()
        throws InvalidRegexpException
    {
        // New version of testParseRegexp_correctness1, now that
        // BasicRegexp.toString is implemented, maybe remove the old one
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)*1111");
        assertEquals(re.toString(), "(01|10)*1111");
    }

    @Test
    public void testParseRegexp_correctness2()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)*11*11*");
        assertEquals(re.toString(), "(01|10)*11*11*");
    }

    @Test
    public void testParseRegexp_correctness3()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)*1(1|0)1");
        assertEquals(re.toString(), "(01|10)*1(1|0)1");
    }

    @Test
    public void testParseRegexp_correctness4()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("(01|10)**abc?+*");
        assertEquals(re.toString(), "(01|10)**abc?+*");
    }

    @Test
    public void testParseRegexp_correctness5()
        throws InvalidRegexpException
    {
        BasicRegexp re = BasicRegexp.parseRegexp("abc?+*|abc?+*");
        assertEquals(re.toString(), "abc?+*|abc?+*");
    }

    @Test
    public void testOptimise_1()
        throws InvalidRegexpException
    {
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

        assertEquals(re1.optimise().toString(), "a*");
        assertEquals(re2.optimise().toString(), "a*");
        assertEquals(re3.optimise().toString(), "a*");
        assertEquals(re4.optimise().toString(), "a*");
        assertEquals(re5.optimise().toString(), "a*");
        assertEquals(re6.optimise().toString(), "a*");
        assertEquals(re7.optimise().toString(), "a*");
        assertEquals(re8.optimise().toString(), "a*");
        assertEquals(re9.optimise().toString(), "a*");
        assertEquals(re10.optimise().toString(), "a*");
    }

    @Test
    public void testOptimise_2()
        throws InvalidRegexpException
    {
        // Test combinations of iteration simplifying down to a single PLUS
        BasicRegexp re1 = BasicRegexp.parseRegexp("a+++++++++++++++++++++");

        assertEquals(re1.optimise().toString(), "a+");
    }

    @Test
    public void testOptimise_3()
        throws InvalidRegexpException
    {
        // Test simplifying parts of SEQUENCE
        // Test single character expressions get simplified
        BasicRegexp re1 = BasicRegexp.parseRegexp("a*a*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("a*a?");
        BasicRegexp re3 = BasicRegexp.parseRegexp("a?a*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("a?a?a?a?a*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("a?a*a*a?a*a?a?a?a*a*a?a*a?");

        assertEquals(re1.optimise().toString(), "a*");
        assertEquals(re2.optimise().toString(), "a*");
        assertEquals(re3.optimise().toString(), "a*");
        assertEquals(re4.optimise().toString(), "a*");
        assertEquals(re5.optimise().toString(), "a*");
    }

    @Test
    public void testOptimise_4()
        throws InvalidRegexpException
    {
        // Test simplifying parts of SEQUENCE
        // Test complex expressions get simplified
        BasicRegexp re1 = BasicRegexp.parseRegexp("(a|b)*(a|b)*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("(a|b)*(a|b)?");
        BasicRegexp re3 = BasicRegexp.parseRegexp("(a|b)?(a|b)*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("(a|b)+(a|b)*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("(a|b)*(a|b)+");
        BasicRegexp re6 = BasicRegexp.parseRegexp("(a|b)+(a|b)?");
        BasicRegexp re7 = BasicRegexp.parseRegexp("(a|b)?(a|b)+");

        assertEquals(re1.optimise().toString(), "(a|b)*");
        assertEquals(re2.optimise().toString(), "(a|b)*");
        assertEquals(re3.optimise().toString(), "(a|b)*");
        assertEquals(re4.optimise().toString(), "(a|b)+");
        assertEquals(re5.optimise().toString(), "(a|b)+");
        assertEquals(re6.optimise().toString(), "(a|b)+");
        assertEquals(re7.optimise().toString(), "(a|b)+");
    }

    @Test
    public void testOptimise_5()
        throws InvalidRegexpException
    {
        // Test simplifying parts of SEQUENCE
        // Test different char prevents oversimplifying, recognise a*b* can't 
        // be simplified to a* for example
        BasicRegexp re1 = BasicRegexp.parseRegexp("a*b*");
        BasicRegexp re2 = BasicRegexp.parseRegexp("a*b?");
        BasicRegexp re3 = BasicRegexp.parseRegexp("a?b*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("a?a?b?a?a*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("a?a*a*a?a*b?a?a?a*b*a?a*a?");

        assertEquals(re1.optimise().toString(), "a*b*");
        assertEquals(re2.optimise().toString(), "a*b?");
        assertEquals(re3.optimise().toString(), "a?b*");
        assertEquals(re4.optimise().toString(), "a?a?b?a*");
        assertEquals(re5.optimise().toString(), "a*b?a*b*a*");
    }

    @Test
    public void testOptimise_6()
        throws InvalidRegexpException
    {
        // Test removing duplicate parts of CHOICE, verify ordering is 
        // maintained
        BasicRegexp re1 = BasicRegexp.parseRegexp("a|a|b");
        BasicRegexp re2 = BasicRegexp.parseRegexp("b|a|a");
        BasicRegexp re3 = BasicRegexp.parseRegexp("(b|b)*|(a|a)*|a*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("(c|b|b)*|(a|a)*|a*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("a*b*|a*b*");

        assertEquals(re1.optimise().toString(), "a|b");
        assertEquals(re2.optimise().toString(), "b|a");
        assertEquals(re3.optimise().toString(), "b*|a*");
        assertEquals(re4.optimise().toString(), "(c|b)*|a*");
        assertEquals(re5.optimise().toString(), "a*b*");
    }

    @Test
    public void testOptimise_7()
        throws InvalidRegexpException
    {
        // Test more complex iteration simplification, tests the optimiseStarOnSC method
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

        assertEquals(re1.optimise().toString(), "(a|b)*");
        assertEquals(re2.optimise().toString(), "(a|d|b|c)*");
        assertEquals(re3.optimise().toString(), "(abc)*");
        assertEquals(re4.optimise().toString(), "(a|b|c)*");
        assertEquals(re5.optimise().toString(), "(a|b|c)*");
        assertEquals(re6.optimise().toString(), "(ab|b|c)*");
        assertEquals(re7.optimise().toString(), "(a|b|c)*");
        assertEquals(re8.optimise().toString(), "((a|b)+b?|c)*"); // Can't optimise further
        assertEquals(re9.optimise().toString(), "(a*bc*)*"); // Can't optimise further
        assertEquals(re10.optimise().toString(), "(a|b|c)*");
        assertEquals(re11.optimise().toString(), "(abc*)*");
    }

    @Test
    public void testDebugPrintBasicRegexp()
        throws InvalidRegexpException
    {
        // Can't mark methods to be ignored by EclEmma coverage tool, this test
        // is a workaround
        BasicRegexp re1 = BasicRegexp.parseRegexp("(01|10)*1111");
        BasicRegexp.debugPrintBasicRegexp(0, re1);
    }

    @Test
    public void testCompareTo()
        throws InvalidRegexpException
    {
        BasicRegexp re1 = BasicRegexp.parseRegexp("(01|10)*1111");
        BasicRegexp re2 = BasicRegexp.parseRegexp("(01|10)*1111");
        BasicRegexp re3 = BasicRegexp.parseRegexp("((a*|b*)+b?|c)*def((g*|h*)+h?|i)*");
        BasicRegexp re4 = BasicRegexp.parseRegexp("((a*|b*)+b?|c)*def((g*|h*)+h?|i)*");
        BasicRegexp re5 = BasicRegexp.parseRegexp("(01|10)*111");

        // Test equality, simpler
        assertEquals(re1.compareTo(re2), 0);
        // Test equality, more complex
        assertEquals(re3.compareTo(re4), 0);
        // Test ordering
        assertTrue(re5.compareTo(re1) < 0);
        assertTrue(re1.compareTo(re5) > 0);
    }
}
