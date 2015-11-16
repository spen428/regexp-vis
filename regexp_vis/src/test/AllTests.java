package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.model.AllModelTests;
import test.ui.AllUITests;

@RunWith(Suite.class)
@SuiteClasses({ AllModelTests.class, AllUITests.class })
public class AllTests {
}
