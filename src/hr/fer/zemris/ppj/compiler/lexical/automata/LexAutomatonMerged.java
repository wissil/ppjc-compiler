package hr.fer.zemris.ppj.compiler.lexical.automata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import hr.fer.zemris.ppj.compiler.lexical.regex.RegexUtil;

/**
 * This class represents an automaton created by merging all of the current
 * {@link LexAutomaton} into one.<br>
 * 
 * This allows us to not have duplicate nodes or edges in the automata forest, if
 * a single automaton is thought of as a graph.<br>
 * 
 * Consequently, this class acts as a joined container for all the {@link LexAutomaton} objects
 * created during the parsing process.
 * 
 * @author fiilip
 *
 */
public class LexAutomatonMerged implements Serializable {

	/**
	 * Generated serial version UID needed by the serializable class.
	 */
	private static final long serialVersionUID = 7087624169807117640L;
	
	/**
	 * Singleton instance of {@link LexAutomatonMerged}.
	 */
	private static LexAutomatonMerged instance = null;
	
	/**
	 * Epsilon symbol.
	 */
	private static final char EPS = '$';
	
	/**
	 * Kleene repetition symbol.
	 */
	private static final char KLEENE_SYM = '*';

	/**
	 * {regularDefinition} -> regEx
	 */
	private Map<String, String> regDefs;
	
	/**
	 * state -> [set of states]
	 */
	private final Map<Integer, Set<Integer>> epsTransitions;
	
	/**
	 * state + symbol -> [set of states]
	 */
	private final Map<Integer, Map<Character, Integer>> transitions;
	
	/**
	 * Current state of the automaton.
	 */
	private int currentState = 0;
	
	
	/**
	 * Creates a new instance of <b>empty</b> {@link LexAutomatonMerged} object.<br>
	 * 
	 * This object needs to be filled with the actual data coming from parser.
	 */
	private LexAutomatonMerged() {
		//this.regDefs = normalizeAll(Objects.requireNonNull(regDefs));
		this.epsTransitions = new HashMap<>();
		this.transitions = new HashMap<>();
	}
	
	/**
	 * Returns the singleton instance of {@link LexAutomatonMerged}.
	 * 
	 * @return Singleton instance of {@link LexAutomatonMerged}.
	 */
	public static LexAutomatonMerged getInstance() {
		// lazy initialization
		if (instance == null) {
			instance = new LexAutomatonMerged();
		}
		
		return instance;
	}
	
	/**
	 * Creates a new {@link LexAutomaton} instance based on the given regular expression.
	 * 
	 * @param regEx		Regular expression describing the automaton.
	 * @return new {@link LexAutomaton} instance based on the <code>regEx</code>.
	 */
	public LexAutomaton fromRegEx(String regEx) {
		return transform(RegexUtil.normalize(regEx, regDefs));
	}
	
	/**
	 * Transforms the given <code>regEx</code> into a {@link LexAutomaton}.
	 * 
	 * @param regEx Regular expression.
	 * @return Lexical automaton based on the given <code>regEx</code>.
	 */
	private LexAutomaton transform(String regEx) {
		List<String> choices = RegexUtil.splitChoices(regEx);
		
		int leftState = generateNewState();
		int rightState = generateNewState();
		
		// recursive call
		if (choices.size() > 1) {
			for (String choice : choices) {
				LexAutomaton tmp = transform(choice);
				addEpsTransition(leftState, tmp.getLeftState());
				addEpsTransition(tmp.getRightState(), rightState);
			}
		} else {
			// single regex choice
			boolean prefixed = false;
			int len = regEx.length();
			int lastState = leftState;
			
			for (int i=0; i<len; i++) {
				int state1;
				int state2;
				
				char symbol = regEx.charAt(i);
				
				if (prefixed) {
					prefixed = false;
					char escape = RegexUtil.unescape(symbol);
					
					state1 = generateNewState();
					state2 = generateNewState();
					
					addTransition(state1, state2, escape);
				} else {
					if (symbol == '\\') {
						prefixed = true;
						continue;
					}
					
					if (symbol == '(') {
						int closing = RegexUtil.findClosingOperator(regEx, '(', ')', i);
						String sub = regEx.substring(i + 1, closing);
						
						LexAutomaton tmp = transform(sub);
						state1 = tmp.getLeftState();
						state2 = tmp.getRightState();
						i = closing;
					} else {
						state1 = generateNewState();
						state2 = generateNewState();
						
						if (symbol == EPS) {
							addEpsTransition(state1, state2);
						} else {
							addTransition(state1, state2, symbol);
						}
					}
				}
				
				// Kleene repetition symbol
				if (i < len-1 && regEx.charAt(i+1) == KLEENE_SYM) {
					int stateTmp1 = state1;
					int stateTmp2 = state2;
					
					state1 = generateNewState();
					state2 = generateNewState();
					
					addEpsTransition(state1, stateTmp1);
					addEpsTransition(state1, state2);
					addEpsTransition(stateTmp2, stateTmp1);
					addEpsTransition(stateTmp2, state2);
					i ++;
				}
				
				// link to previous choice
				addEpsTransition(lastState, state1);
				lastState = state2;
			}
			
			// link to last state
			addEpsTransition(lastState, rightState);
		}
		
		return new LexAutomaton(leftState, rightState);
	}
	
	/**
	 * Adds a new transition from <code>leftState</code> for a <code>symbol</code>
	 * to the <code>rightState</code>.
	 * 
	 * @param leftState Left state.
	 * @param rightState Right state.
	 * @param symbol Transition symbol.
	 */
	private void addTransition(int leftState, int rightState, char symbol) {
		Map<Character, Integer> transition = getNormalStates(leftState);
		transition.put(symbol, rightState);
		transitions.put(leftState, transition);
	}
	
	/**
	 * Gets the state that can be accessed via symbol transition from a given state.<br>
	 * If no such states exist, an empty map is returned.
	 * 
	 * @param state
	 * @return
	 */
	public Map<Character, Integer> getNormalStates(int state) {
		Map<Character, Integer> transition = transitions.get(state);
		
		if (transition == null) {
			transition = new HashMap<>();
		}
		
		return transition;
	}
	
	/**
	 * Adds an epsilon transition from left state to the right state.
	 * 
	 * @param leftState Left state of the eps transition,
	 * @param rightState Right state of the eps transition.
	 */
	private void addEpsTransition(int leftState, int rightState) {
		Set<Integer> states = getEpsilonStates(leftState);
		states.add(rightState);
		epsTransitions.put(leftState, states);
	}
	
	/**
	 * Gets the states that can be accessed from a given <code>state</code>.
	 * 
	 * @param state State of interest.
	 * @return States that can be accessed via eps transitions, or an empty set
	 * if such states don't exist.
	 */
	public Set<Integer> getEpsilonStates(int state) {
		Set<Integer> states = epsTransitions.get(state);
		
		if (states == null) {
			states = new HashSet<>();
		}
		
		return states;
	}
	
	/**
	 * Sets the regular definitions to this automaton.
	 * 
	 * @param regDefs Regular definitions.
	 */
	public void setRegDefs(Map<String, String> regDefs) {
		this.regDefs = Objects.requireNonNull(normalizeAll(regDefs));
	}


	/**
	 * Generates a new, unique state.
	 * 
	 * @return	New unique state of this automaton.
	 */
	private int generateNewState() {
		return currentState ++;
	}
	
	
    /**
     * Normalizes all regular definitions into the normal regular expression format.
     * 
     * @param regDefs Regular definitions.
     * @return Normalized regular expression.
     */
    private static Map<String, String> normalizeAll(Map<String, String> regDefs) {
    		return regDefs.entrySet().stream().collect(Collectors.toMap(
    				e -> e.getKey(),
    				e -> RegexUtil.normalize(e.getValue(), regDefs)));
    }
    
}
