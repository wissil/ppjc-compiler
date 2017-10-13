package hr.fer.zemris.ppj.compiler.lexical.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.ppj.compiler.lexical.actions.LexAction;
import hr.fer.zemris.ppj.compiler.lexical.actions.LexActionFactory;
import hr.fer.zemris.ppj.compiler.lexical.rules.LexRule;

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
	 * Pattern for lines containing regular definitions.
	 */
	private static final String REG_DEF_PATTERN = "\\{[a-z]+[A-Z]*[a-z]*\\} ";
	
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

	private void readLexRules(BufferedReader reader) throws IOException {
		while ((currLine = reader.readLine()) != null) {
			
		}
	}

	private void readLexUnits(BufferedReader reader) throws IOException {
		// check for errors
		if (!isLexUnitsLine(currLine = reader.readLine())) {
			throw new IllegalStateException(String.format(
					"Lexical units expected but not found in line: '%s'. Check the input stream format.", currLine));
		}
		
	}

	private void readStates(BufferedReader reader) {
		// check for errors
		if (!isLexStatesLine(currLine)) {
			throw new IllegalStateException(String.format(
					"Lexical states expected but not found in line: '%s'. Check the input stream format.", currLine));
		}
		
	}

	/**
	 * Reads regular definitions from the input.
	 * 
	 * @param reader		Input reader.
	 * @throws IOException 
	 */
	private void readRegDef(BufferedReader reader) throws IOException {	
		while (isRegDefLine(currLine = reader.readLine())) {
			
		}
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
