package hr.fer.zemris.ppj.compiler.lexical.automata;

import hr.fer.zemris.ppj.compiler.automata.Automaton;
import hr.fer.zemris.ppj.compiler.lexical.input.LexicalData;

/**
 * Represents an automaton used in lexical analysis by the compiler.<br>
 * 
 * Epsilon non-deterministic finite automaton.<br>
 * 
 * <b>$-NFA</b>
 * 
 * @author fiilip
 *
 */
public class LexAutomaton implements Automaton<Character> {

	/**
	 * Generated serial version UID needed by the serializable class.
	 */
	private static final long serialVersionUID = -4255302386071927580L;
	
	/**
	 * Left, non-final, state of the automaton.
	 */
	private final int leftState;
	
	/**
	 * Right, final, state of the automaton.<br>
	 * This is the only final state.
	 */
	private final int rightState;
	
	/**
	 * Current state of this automaton.
	 */
	private int state;
	
	/**
	 * Creates a new instance of {@link LexAutomaton}.
	 * 
	 * @param leftState		Starting state of the automaton.
	 * @param rightState		Final state of the automaton.
	 */
	public LexAutomaton(int leftState, int rightState) {
		this.leftState = leftState;
		this.rightState = rightState;
	}
	
	/**
	 * Creates a new {@link LexAutomaton} instance based on the given regular expression.
	 * 
	 * @param regEx		Regular expression describing the automaton.
	 * @param lexData	Lexical data needed to be updated by the data provided by this <code>regEx</code>.
	 * @return new {@link LexAutomaton} instance based on the <code>regEx</code>.
	 */
	public static LexAutomaton fromRegEx(String regEx, LexicalData lexData) {
		
	}

	/**
	 * Gets the starting state of this automaton.
	 * 
	 * @return	Starting state of the automaton.
	 */
	public int getLeftState() {
		return leftState;
	}

	/**
	 * Gets the final state of this automaton.
	 * 
	 * @return	Final state of the automaton.
	 */
	public int getRightState() {
		return rightState;
	}
}