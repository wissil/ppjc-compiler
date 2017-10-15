package hr.fer.zemris.ppj.compiler.lexical.exec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.ppj.compiler.lexical.automata.LexAutomaton;
import hr.fer.zemris.ppj.compiler.lexical.automata.LexAutomatonMerged;
import hr.fer.zemris.ppj.compiler.util.StreamManager;

/**
 * Lexical Analyzer.
 * 
 * @author fiilip
 *
 */
public class Lex {
	
	private String input;
	
	private final StreamManager streamManager;
	
    private final Map<String, List<LexRule>> states;
    
    /**
     * Current state of this lexical analyzer.
     */
    private String currentState;
    
    
    private List<LexRule> currentRules;

    private int startIndex;
    private int endIndex;
    private int lastIndex;
    private int lineNumber;
        
    private final OutputStream ostream;

    /**
     * Creates a new instance of {@link Lex}.<br> 
     * 
     * It has a defined starting state.<br> 
     * 
     * For every state available, there is a list of rules that are executed when {@link Lex} is in that state.
     * 
     * @param startState Starting state.
     * @param states Mappings from state to list of rules.
     * @param merged Merged automaton.
     * @param output Output stream used for results output.
     */
    public Lex(String startState, Map<String, List<LexRule>> states, 
    				StreamManager streamManager, OutputStream ostream, LexAutomatonMerged merged) {
    		this.streamManager = Objects.requireNonNull(streamManager);
        this.states = Objects.requireNonNull(states);
        this.ostream = Objects.requireNonNull(ostream);
        
        // statically set data for all lex automatons
        LexAutomaton.setLexAutomatonMerged(merged);
        
        // put the lexical analyzer to the starting state
        enterState(startState);

        startIndex = 0;
        endIndex = -1;
        lastIndex = -1;
        lineNumber = 1;
    }
    
    /**
     * Changes the state of this lexical analyzer to the new state <code>toState</code>.<br>
     * This also obtains the new set of rules - the ones that belong to the new state.
     * 
     * @param toState New state of this lexical analyzer.
     */
    public void enterState(String toState) {
    		currentState = toState;
    		currentRules = states.get(currentState);
    		resetCurrentAutomatons();
	}

    /**
     * Resets all of the automatons that belong to the current state.
     */
	private void resetCurrentAutomatons() {
		for (LexRule rule : currentRules) {
			rule.getAutomaton().reset();
		}
	}

	/**
     * Consumes symbols from the input stream and performs lexical analysis.
     * 
     * @param istream Input stream.
	 * @throws IOException 
     */
    public void analyze(InputStream istream) throws IOException {
    		input = loadToMemory(istream); 	
    		int len = input.length();
    		LexRule lastRule = null;
    		
    		// process every symbol from the stream
    		while (endIndex < len - 1) {
    			boolean alive = true;
    			
    			while (alive && endIndex < len - 1) {
    				alive = false;    				
    				boolean accepted = false;
    				
    				char symbol = input.charAt(++endIndex);
    				
    				for (LexRule rule : currentRules) {
    					LexAutomaton curr = rule.getAutomaton();
    					
    					if (curr.isDead()) {
    						continue;
    					}
    					
    					// automaton alive
    					alive = true;
    					curr.consume(symbol);
    					
    					if (!accepted && curr.accepts()) {
    						// move to next token
    						accepted = true;
    						lastRule = rule;
    						lastIndex = endIndex;
    					}
    				}
    			}
    			
    			if (lastRule == null) {
    				// none of the automatons accepted the string; error recovery needed
    				endIndex = startIndex ++;
    			} else {
    				// string was accepted
    				endIndex = lastIndex;
    				
    				// execute rule
    				lastRule.execute(this);
    				
    				if (lastRule.hasLexUnit()) {
    					output(lastRule.lexUnit());
    					startIndex = lastIndex + 1;
    				} else {
    					skip();
    				}
    				
    				lastRule = null;
    				resetCurrentAutomatons();
    			}
    		}
    }
    
    /**
     * Increments line number by <code>1</code>.
     */
    public void incrementLineNumber() {
    		lineNumber ++;
    }
    
    /**
     * Moves the current index back to the <code>startIndex + toIdx</code>.
     * 
     * @param toIdx Index to move, counting from the starting position.
     */
    public void goBack(int toIdx) {
    		resetCurrentAutomatons();
    		
    		int idx = startIndex + toIdx - 1;
    		endIndex = lastIndex = idx;
    		
    		char[] feed = input.substring(startIndex, lastIndex + 1).toCharArray();
    		
    		for (LexRule rule : currentRules) {
    			for (char symbol : feed) {
    				rule.getAutomaton().consume(symbol);
    			}
    		}
    }
    
    /**
     * Skips current input.
     */
    public void skip() {
    		startIndex = endIndex + 1;
	}

	/**
     * Outputs the lexical unit to the output stream.
     * 
     * @param lexUnit Lexical unit.
     * @throws IOException 
     */
    private void output(String lexUnit) throws IOException {
    		String content = input.substring(startIndex, lastIndex + 1);
    		
    		streamManager.writeToStream(
    				String.format("%s %d %s%s", lexUnit, lineNumber, content, System.lineSeparator()), 
    				ostream);
	}

	/**
     * Loads the entire content of input stream to memory as a String.
     * 
     * @param istream Input stream.
     * @return Content read from the input stream returned as a stream.
     * @throws IOException 
     */
    private String loadToMemory(InputStream istream) throws IOException {
    		try {
			// read the entire content of input stream to memory
    			return streamManager.readFromStream(istream);
		} catch (IOException e) {
			// create new exception just for the more specified error message; consider adding a custom exception
			throw new IOException(String.format("Error reading from input stream: %s.", e.getMessage()), e);
		}
    }

}
