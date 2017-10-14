package hr.fer.zemris.ppj.compiler.lexical.exec;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.ppj.compiler.lexical.actions.LexAction;
import hr.fer.zemris.ppj.compiler.lexical.automata.LexAutomaton;

/**
 * Represents a single lexical rule.<br>
 * 
 * A lexical rule is executed whenever it's pattern matches
 * part of the input.
 * 
 * @author fiilip
 *
 */
public class LexRule implements LexExecutable, Serializable {

	/**
	 * Generated serial version UID needed by the serializable class.
	 */
	private static final long serialVersionUID = 2082052650666082592L;
	
	/**
	 * Lexical unit.
	 */
	private final String lexUnit;
	
	/**
	 * Corresponding automaton.
	 */
	private final LexAutomaton automaton;
	
	/**
	 * Executable actions.
	 */
	private final List<LexAction> actions;

	/**
	 * Creates a new {@link LexRule} from a given automaton (regEx),
	 * and a list of actions that are executed whenever the input matches the regEx.
	 * 
	 * @param lexUnit Lexical unit of this lexical rule.
	 * @param automaton Automaton for this lexical rule.
	 * @param actions List of actions that are executed on matched regEx.
	 */
	public LexRule(String lexUnit, LexAutomaton automaton, List<LexAction> actions) {
		this.lexUnit = Objects.requireNonNull(lexUnit);
		this.automaton = Objects.requireNonNull(automaton);
		this.actions = Objects.requireNonNull(actions);
	}
	
	/**
     * Tests if this lexical rule has an associated lexical unit.
     * 
     * @return <code>True</code> if it has a lexical unit, <code>false</code> otherwise.
     */
    public boolean hasLexUnit() {
        return !"-".equals(lexUnit.trim());
    }

    /**
     * Returns this rule's lexical unit.
     * 
     * @return Lexical unit of this rule.
     */
    public String lexUnit() {
        return lexUnit;
    }

    /**
     * Gets the automaton that is used to match a regEx for this rule.
     * 
     * @return Automaton that matches regEx.
     */
    public LexAutomaton getAutomaton() {
        return automaton;
    }

	@Override
	public void execute(Lex lex) {
		for (LexAction action : actions) {
			action.execute(lex);
		}
	}

}
