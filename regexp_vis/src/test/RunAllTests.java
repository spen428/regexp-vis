package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.model.AutomatonStateTest;
import test.model.AutomatonTest;
import test.model.AutomatonTransitionTest;
import test.model.BasicRegexpTest;
import test.model.CommandHistoryTest;
import test.model.RemoveEpsilonTransitionsContextTest;
import test.model.RemoveNonDeterminismContextTest;
import test.model.TranslationToolsTest;
import test.view.GraphEdgeTest;
import test.view.GraphNodeTest;
import test.view.GraphUtilsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        /* test.controller */

        /* test.model */
        AutomatonStateTest.class,
        AutomatonTest.class,
        AutomatonTransitionTest.class,
        BasicRegexpTest.class,
        CommandHistoryTest.class,
        RemoveEpsilonTransitionsContextTest.class,
        RemoveNonDeterminismContextTest.class,
        TranslationToolsTest.class,

        /* test.view */
        GraphEdgeTest.class,
        GraphNodeTest.class,
        GraphUtilsTest.class
})

public class RunAllTests {
}
