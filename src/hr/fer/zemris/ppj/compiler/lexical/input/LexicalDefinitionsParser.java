package hr.fer.zemris.ppj.compiler.lexical.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.ppj.compiler.lexical.actions.LexAction;
import hr.fer.zemris.ppj.compiler.lexical.actions.LexActionFactory;
import hr.fer.zemris.ppj.compiler.lexical.automata.LexAutomaton;
import hr.fer.zemris.ppj.compiler.lexical.automata.LexAutomatonMerged;
import hr.fer.zemris.ppj.compiler.lexical.exec.LexRule;

/**
 * This class is used to read in the lexical rules of the given language,
 * and parse them into the corresponding objects.
 * 
 * Parsed objects can be used by the {@link LA} for the lexical analysis.
 * 
 * @author fiilip
 *
 */
public class LexicalDefinitionsParser {
	
	/**
	 * Pattern for lines containing lexical rules.
	 */
	private static final String LEX_RULE_PATTERN = "<.*>";
	
	/**
	 * Pattern for lines containing regular definitions.
	 */
	private static final String REG_DEF_PATTERN = "\\{[a-z]+[A-Z]*[a-z]*\\} ";
	
	/**
	 * Pattern that symbolizes the end of the action arguments.
	 */
	private static final String ACTIONS_END_PATTERN = "}";
	
	/**
	 * Symbol that represents the end of a regular definition.
	 */
	private static final char REG_DEF_END_SYM = '}';
	
	
	/**
	 * Symbol that represents the end of a state name.
	 */
	private static final char STATE_NAME_END_SYM = '>';
	
	/**
	 * Pattern for lines containing lexical units.
	 */
	private static final String LEX_UNITS_PATTERN = "%L ";
	
	/**
	 * Pattern for lines containing lexical state names.
	 */
	private static final String LEX_STATES_PATTERN = "%X ";
	
	/**
	 * Standard charset of the input stream.
	 */
	private static final Charset CS = StandardCharsets.UTF_8;
	
	/**
	 * Input stream of the lexical definitions.
	 */
	private final InputStream istream;
	
	/**
	 * List of all possible state names.
	 */
	private final List<String> stateNames;
	
	/**
	 * List of all possible lexical units.<br>
	 * ie. IDN, NUMBER, KEYWORD, etc.
	 */
	private final List<String> lexUnits;
	
	/**
	 * Maps the name of the states to the corresponding lexical rules.
	 */
	private final Map<String, List<LexRule>> states;
	
	/**
	 * Single lexical automaton merged from all the lexical automatons parsed from the input.<br>
	 * M_1, M_2, ..., M_n --> M_merged
	 */
	private final LexAutomatonMerged automatonMerged;
	
	/**
	 * Set of regular definitions defined in the input stream.
	 */
	private final Map<String, String> regDefs;
	
	/**
	 * Current line being read from the stream.
	 */
	private String currLine = null;
	
	/**
	 * Creates a new instance of {@link LexicalRulesParser}.
	 * 
	 * @param istream	Input stream of the lexical definitions.
	 */
	public LexicalDefinitionsParser(InputStream istream) {
		this.istream = Objects.requireNonNull(istream);
		this.stateNames = new ArrayList<>();
		this.lexUnits = new ArrayList<>();
		this.states = new LinkedHashMap<>();
		this.regDefs = new HashMap<>();
		this.automatonMerged = LexAutomatonMerged.getInstance();
	}
	
	/**
	 * Initiates the parsing process of the input stream.<br>
	 * 
	 * The input stream contains the language definitions.
	 */
	public void parse() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(istream, CS))) {
			readRegDef(reader);
			readStates(reader);
			readLexUnits(reader);
			readLexRules(reader);
		} catch (IOException e) {
			System.err.println(
					String.format("Error reading from stream. Exception: %s. Message: %s.", e, e.getMessage()));
		}
	}

	/**
	 * Reads lexical rules from the input.
	 * 
	 * @param reader Input reader.
	 * @throws IOException
	 */
	private void readLexRules(BufferedReader reader) throws IOException {
		while ((currLine = reader.readLine()) != null) {
			// check for errors
			if (!isLexRuleLine(currLine)) {
				throw new IllegalStateException(String.format(
						"Lexical rule expected but not found in line: '%s'. Check the input stream format.", currLine));
			}
			
			int endOfStateNameIdx = currLine.indexOf(STATE_NAME_END_SYM);
			
			String state = currLine.substring(1, endOfStateNameIdx);
			String regEx = currLine.substring(endOfStateNameIdx + 1);
			
			// create automaton from regex
			LexAutomaton automaton = automatonMerged.fromRegEx(regEx);
			
			// skip '{' symbols
			reader.readLine();
			
			String lexUnit = reader.readLine();
			List<LexAction> actions = new LinkedList<>();
			
			while (!isActionsEnd(currLine = reader.readLine())) {
				actions.add(getLexActionFromLine(currLine));
			}
			
			List<LexRule> lexRules = getLexRules(state);
			lexRules.add(new LexRule(lexUnit, automaton, actions));
			states.put(state, lexRules);
		}
	}

	/**
	 * Reads lexical units from the input.
	 * 
	 * @param reader Input reader.
	 * @throws IOException
	 */
	private void readLexUnits(BufferedReader reader) throws IOException {
		// check for errors
		if (!isLexUnitsLine(currLine = reader.readLine())) {
			throw new IllegalStateException(String.format(
					"Lexical units expected but not found in line: '%s'. Check the input stream format.", currLine));
		}
		
		// ignore the first 3 symbols "%L "
		currLine = currLine.substring(3);
		String[] lexUnitsArr = currLine.split("\\s");
		
		// add to the lexical units
		lexUnits.addAll(Arrays.asList(lexUnitsArr));
	}

	/**
	 * Reads state names from the input.
	 * 
	 * @param reader Input reader.
	 */
	private void readStates(BufferedReader reader) {
		// check for errors
		if (!isLexStatesLine(currLine)) {
			throw new IllegalStateException(String.format(
					"Lexical states expected but not found in line: '%s'. Check the input stream format.", currLine));
		}
		
		// ignore the first 3 symbols "%X "
		currLine = currLine.substring(3);
		String[] stateNamesArr = currLine.split("\\s");
		
		// add to the state names
		stateNames.addAll(Arrays.asList(stateNamesArr));
	}

	/**
	 * Reads regular definitions from the input.
	 * 
	 * @param reader	 Input reader.
	 * @throws IOException 
	 */
	private void readRegDef(BufferedReader reader) throws IOException {	
		while (isRegDefLine(currLine = reader.readLine())) {
			int regDefEndIdx = currLine.charAt(REG_DEF_END_SYM);
			
			String regDefName = currLine.substring(1, regDefEndIdx);
			String regEx = currLine.substring(regDefEndIdx + 2);
			
			regDefs.put(regDefName, regEx);
		}
		// assign generated reg defs to the merged automaton
		automatonMerged.setRegDefs(Collections.unmodifiableMap(regDefs));
	}
	
	/**
	 * Helper method that evaluates whether or not the current line
	 * contains a regular definition.
	 * 
	 * @param line	Line being evaluated.
	 * @return <code>True</code> if the line contains regular definition, <code>false</code> otherwise.
	 */
	private boolean isRegDefLine(String line) {
		return line.startsWith(REG_DEF_PATTERN);
	}
	
	/**
	 * Helper method that evaluates whether or not the current line
	 * contains lexical units.
	 * 
	 * @param line	Line being evaluated.
	 * @return <code>True</code> if the line contains lexical units, <code>false</code> otherwise.
	 */
	private boolean isLexUnitsLine(String line) {
		return line.startsWith(LEX_UNITS_PATTERN);
	}
	
	/**
	 * Helper method that evaluates whether or not the current line
	 * contains lexical state names.
	 * 
	 * @param line	Line being evaluated.
	 * @return <code>True</code> if the line contains lexical state names, <code>false</code> otherwise.
	 */
	private boolean isLexStatesLine(String line) {
		return line.startsWith(LEX_STATES_PATTERN);
	}
	
	/**
	 * Helper method that evaluates whether or not the current line
	 * contains a lexical rule definition.
	 * 
	 * @param line	Line being evaluated.
	 * @return <code>True</code> if the line contains lexical rule definition, <code>false</code> otherwise.
	 */
	private boolean isLexRuleLine(String line) {
		return line.startsWith(LEX_RULE_PATTERN);
	}
	
	/**
	 * Helper method that evaluates whether or not the current line
	 * represents the end of the action arguments..
	 * 
	 * @param line	Line being evaluated.
	 * @return <code>True</code> if the line is the actions end, <code>false</code> otherwise.
	 */
	private boolean isActionsEnd(String line) {
		return line.startsWith(ACTIONS_END_PATTERN);
	}
	
	/**
	 * Gets the lexical rules associated to the given <code>state</code>.
	 * 
	 * @param state State of interest.
	 * @return List of lexical rules associated to the given <code>state</code>, or
	 * an empty list if no such states exist.
	 */
	private List<LexRule> getLexRules(String state) {
		List<LexRule> lexRules = states.get(state);
		
		if (lexRules == null) {
			lexRules = new LinkedList<>();
		}
		
		return lexRules;
	}

	/**
	 * Parses the action from the given line.
	 * 
	 * @param line	Line containing the action.
	 * @return		{@link LexAction} parsed from the line.
	 */
	private LexAction getLexActionFromLine(String line) {
		String args[] = line.split("\\s+");
		
		return LexActionFactory.produce(args);
	}
}
