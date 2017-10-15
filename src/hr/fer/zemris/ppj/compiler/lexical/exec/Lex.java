package hr.fer.zemris.ppj.compiler.lexical.exec;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private final String currentState;
    private final List<LexRule> currentRules;

    private int startIndex;
    private int endIndex;
    private int lastIndex;
    private int lineNumber;
    
    private final LexAutomatonMerged merged;

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
    				LexAutomatonMerged merged, StreamManager streamManager) {
    		this.streamManager = Objects.requireNonNull(streamManager);
        this.states = Objects.requireNonNull(states);
        this.merged = Objects.requireNonNull(merged);
        
        changeState(startState);

        startIndex = 0;
        endIndex = -1;
        lastIndex = -1;
        lineNumber = 1;
    }
    
    /**
     * Consumes symbols from the input stream and performs lexical analysis.
     * 
     * @param istream Input stream.
     */
    public void analyze(InputStream istream) {
    		
    }

}
