package model;

import java.util.*;

public final class TranslationTools {
    public static Command createBreakdownCommand(Automaton automaton, AutomatonTransition t)
    {
        BasicRegexp re = (BasicRegexp)t.getData();
        switch (re.getOperator()) {
        case NONE:
            return null; // No operator to breakdown, nothing to do
        case STAR:
            return new BreakdownIterationCommand(automaton, t);
        case PLUS:
            return new BreakdownIterationCommand(automaton, t);
        case OPTION:
            return new BreakdownOptionCommand(automaton, t);
        case SEQUENCE:
            return new BreakdownSequenceCommand(automaton, t);
        case CHOICE:
            return new BreakdownChoiceCommand(automaton, t);
        default:
            throw new UnsupportedOperationException("BUG: Translation not implemented for given operator");
        }
    }
}
