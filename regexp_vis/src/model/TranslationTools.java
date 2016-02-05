package model;

public final class TranslationTools {

    public static BreakdownCommand createBreakdownCommand(Automaton automaton,
            AutomatonTransition transition) {
        if (automaton == null) {
            throw new IllegalArgumentException("Automaton cannot be null");
        } else if (transition == null) {
            throw new IllegalArgumentException(
                    "AutomatonTransition cannot be null");
        }

        BasicRegexp re = (BasicRegexp) transition.getData();
        switch (re.getOperator()) {
        case NONE:
            return null; // No operator to breakdown, nothing to do
        case STAR:
        case PLUS:
            return new BreakdownIterationCommand(automaton, transition,
                    BreakdownIterationCommand.calcBestIsolationLevel(automaton,
                            transition));
        case OPTION:
            return new BreakdownOptionCommand(automaton, transition);
        case SEQUENCE:
            return new BreakdownSequenceCommand(automaton, transition);
        case CHOICE:
            return new BreakdownChoiceCommand(automaton, transition);
        default:
            throw new UnsupportedOperationException(
                    "Translation not implemented for operator "
                            + re.getOperator().toString());
        }
    }

}
