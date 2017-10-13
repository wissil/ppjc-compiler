package hr.fer.zemris.ppj.compiler.lexical.input;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import hr.fer.zemris.ppj.compiler.lexical.regex.RegexUtil;

/**
 * This class acts as container for the data parsed from the lexical definitions
 * by {@link LexicalDefinitionsParser}.
 * 
 * @author fiilip
 *
 */
public class LexicalData implements Serializable {

	/**
	 * Generated serial version UID needed by the serializable class.
	 */
	private static final long serialVersionUID = 7087624169807117640L;

	/**
	 * {regularDefinition} -> regEx
	 */
	private final Map<String, String> regDefs;
	
	/**
	 * state -> [set of states]
	 */
	private final Map<Integer, Set<Integer>> epsTransitions;
	
	/**
	 * state + symbol -> [set of states]
	 */
	private final Map<Integer, Map<Character, Integer>> transitions;
	
	/**
	 * Singleton instance of this class.
	 */
	private static LexicalData instance;
	
	/**
	 * Creates a new instance of <b>empty</b> {@link LexicalData} object.<br>
	 * 
	 * This object needs to be filled with the actual data coming from parser.
	 */
	private LexicalData() {
		this.regDefs = new HashMap<>();
		this.epsTransitions = new HashMap<>();
		this.transitions = new HashMap<>();
	}
	
	/**
	 * Gets a singleton instance of {@link LexicalData} object.<br>
	 * 
	 * This instance is needed by the {@link LexicalDefinitionsParser} to fill it with data.
	 * 
	 * @return singleton instance of the {@link LexicalData} object.
	 */
	public static LexicalData getInstance() {
		// lazy initialization
		if (instance == null) {
			instance = new LexicalData();
		}
		
		return instance;
	}
	
    /**
     * Adds a regular definition to the data object.
     * 
     * @param regDefName		Name of the regular definition.
     * @param regEx			Correspondent regular expression.
     */
    public void addRegularDefinition(String regDefName, String regEx) {
        regDefs.put(regDefName, RegexUtil.normalize(regEx, regDefs));
    }
    
}
