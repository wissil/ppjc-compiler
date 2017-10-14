package hr.fer.zemris.ppj.compiler.lexical.automata;

import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.ppj.compiler.automata.Automaton;

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
	 * Whether or not this automaton is in an acceptable state.
	 */
	private boolean accepts;
	
	/**
	 * Currently active states of this automaton.
	 */
	private Set<Integer> currentStates;
	
	/**
	 * Singleton reference to joined automaton containing all the transitions.
	 */
	private final LexAutomatonMerged automatonMerged;
	
	/**
	 * Creates a new instance of {@link LexAutomaton}.
	 * 
	 * @param leftState		Starting state of the automaton.
	 * @param rightState		Final state of the automaton.
	 */
	public LexAutomaton(int leftState, int rightState) {
		this.leftState = leftState;
		this.rightState = rightState;
		this.accepts = false;
		this.currentStates = new TreeSet<>();
		
		this.automatonMerged = LexAutomatonMerged.getInstance();
		
		currentStates.add(leftState);
		updateCurrentStates();
	}
	
	/**
	 * Updates the currently active states of this automaton with
	 * the states reachable through epsilon transitions from the current states.
	 */
	private void updateCurrentStates() {
		accepts = false;
		
		// epsilon environment
		Set<Integer> epsStates = null;
		
		do {
			epsStates = new TreeSet<>();
			
			for (int state : currentStates) {
				if (state == rightState) {
					accepts = true;
				}
				
				epsStates.addAll(automatonMerged.getEpsilonStates(state));
			}
			
		} while (currentStates.addAll(epsStates));
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

	@Override
	public void consume(Character symbol) {
		Set<Integer> states = new TreeSet<>();
		
		for (Integer state : currentStates) {
			Integer transitionState = automatonMerged.getNormalStates(state).get(symbol);
			
			if (transitionState != null) {
				states.add(transitionState);
			}
		}
		
		currentStates = states;
		updateCurrentStates();
	}

	@Override
	public void reset() {
		currentStates = new TreeSet<>();
		currentStates.add(leftState);
		updateCurrentStates();
	}

	@Override
	public boolean accepts() {
		return accepts;
	}

	@Override
	public boolean isDead() {
		return currentStates.isEmpty();
	}
}
