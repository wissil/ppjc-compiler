package hr.fer.zemris.ppj.compiler.automata;

import java.io.Serializable;

/**
 * This interface represents a <b>Finite State Automaton (FSA)</b>.
 * 
 * @author fiilip
 *
 * @param <T>	Type of symbols this automaton can operate upon.
 */
public interface Automaton<T> extends Serializable {
	
	/**
     * Applies a transition based on the given symbol.<br>
     * Ie. state1, symbol --> [set of states]
     * 
     * @param symbol Transition symbol.
     */
    public void consume(T symbol);

    /**
     * Puts the automaton to the starting position.
     */
    public void reset();

    /**
     * Tests if the automaton is in acceptable state.
     * 
     * @return <code>True</code> if automaton accepts, <code>false</code> otherwise.
     */
    public boolean accepts();

    /**
     * Automaton is dead if there is no current state.<br> 
     * 
     * It can become dead if it consumes a symbol from a state that 
     * doesn't provide a transition for that symbol.
     * 
     * @return <code>True</code> if automaton is dead, <code>false</code> otherwise.
     */
    public boolean isDead();

}
