package model;

/**
 * Command which breaks down an iteration expression (e.g. "a+" or
 * "a*") into a set of simpler transitions. For the regular
 * expression to NFA conversion process.
 */
public class BreakdownIterationCommand extends BreakdownCommand {
    /**
     * @param automaton The automaton for the transition
     * @param t The transition to break down
     * @throws IllegalArgumentException if the transition expression
     * isn't STAR or PLUS
     */
    public BreakdownIterationCommand(Automaton automaton, AutomatonTransition t)
    {
        super(automaton, t);

        BasicRegexp re = (BasicRegexp)t.getData();
        AutomatonState from = t.getFrom();
        AutomatonState to = t.getTo();

        // FIXME(mjn33): Implement STAR, don't always use fully
        // isolated version
        if (re.getOperator() != BasicRegexp.RegexpOperator.STAR &&
            re.getOperator() != BasicRegexp.RegexpOperator.PLUS) {
            throw new IllegalArgumentException(
                "BreakdownIterationCommand must be passed either " +
                "the PLUS or STAR operators, i.e. \"a+\" or \"a*\"");
        }

        mCommands.add(new RemoveTransitionCommand(automaton, t));
        AutomatonState fromIsolated = automaton.createNewState();
        AutomatonState toIsolated = automaton.createNewState();

        mCommands.add(new AddStateCommand(automaton, fromIsolated));
        mCommands.add(new AddStateCommand(automaton, fromIsolated));

        AutomatonTransition fromTrans = automaton.createNewTransition(from,
            fromIsolated, BasicRegexp.EPSILON_EXPRESSION);
        AutomatonTransition toTrans = automaton.createNewTransition(toIsolated,
            to, BasicRegexp.EPSILON_EXPRESSION);
        // Transition which skips over the iteration
        AutomatonTransition epsilonSkipTrans = automaton.createNewTransition(
            from, to, BasicRegexp.EPSILON_EXPRESSION);
        // Transition for iteration
        AutomatonTransition epsilonBackwardTrans = automaton
            .createNewTransition(toIsolated, fromIsolated,
                BasicRegexp.EPSILON_EXPRESSION);

        mCommands.add(new AddTransitionCommand(automaton, fromTrans));
        mCommands.add(new AddTransitionCommand(automaton, toTrans));
        mCommands.add(new AddTransitionCommand(automaton, epsilonSkipTrans));
        mCommands.add(new AddTransitionCommand(automaton, epsilonBackwardTrans));

        BasicRegexp operand = re.getOperands().get(0);
        AutomatonTransition subexprTrans = automaton.createNewTransition(
            fromIsolated, toIsolated, operand);

        mCommands.add(new AddTransitionCommand(automaton, subexprTrans));
    }
}
