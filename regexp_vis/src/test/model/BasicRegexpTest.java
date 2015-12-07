package test.model;

import static org.junit.Assert.*;
import model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicRegexpTest {

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    // TODO(mjn33): Ideas for future tests
    //pg272 fninished the rest of the test

    @Test
    public void testParseRegexp_invalid1()
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
    public void testParseRegexp_invalid2()
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
    public void testParseRegexp_invalid3()
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
    public void testParseRegexp_invalid4()
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
    public void testParseRegexp_invalid5()
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
    public void testParseRegexp_invalid6()
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
    public void testParseRegexp_invalid7()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("(sdf)**");
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
            BasicRegexp.parseRegexp("(sdf)**");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertTrue(caught);
    }
    
   
	@Test
	public void testParseRegexp_invalid9() {
		boolean caught = false;
		try {
			BasicRegexp.parseRegexp("(sdf)*?");
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
    {
        boolean caught = false;
        try {
            // TODO(mjn33): comments on this? Should this be invalid instead?
            BasicRegexp.parseRegexp("");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
    }
    
    @Test
    public void testParseRegexp_valid3()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("(a)(b)(c)(d)");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
    }
    
    @Test
    public void testParseRegexp_valid4()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("a b c d");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
    }
    
    @Test
    public void testParseRegexp_valid5()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("sdf**");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
    }
    
    @Test
    public void testParseRegexp_valid6()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("sdf**");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
    }
    
    
    
    @Test
    public void testParseRegexp_valid7()
    {
        boolean caught = false;
        try {
            BasicRegexp.parseRegexp("(sdf)*+");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
    }
    

    @Test
    public void testParseRegexp_valid3()
    {
        boolean caught = false;
        try {
            // Test deeper nesting of parentheses
            BasicRegexp.parseRegexp("ab(cd(e))ff");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
    }

    @Test
    public void testParseRegexp_valid4()
    {
        boolean caught = false;
        try {
            // Test a unary operator on single char
            BasicRegexp.parseRegexp("abc*def");
        } catch (InvalidRegexpException e) {
            caught = true;
        }

        assertFalse(caught);
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
}
