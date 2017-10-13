package hr.fer.zemris.ppj.compiler.lexical.input;

import hr.fer.zemris.ppj.compiler.lexical.actions.LexAction;
import hr.fer.zemris.ppj.compiler.lexical.actions.LexActionFactory;

/**
 * This class is used to read in the lexical rules of the given language,
 * and parse them into the corresponding objects.
 * 
 * Parsed objects can be used by the {@link LA} for the lexical analysis.
 * 
 * @author fiilip
 *
 */
public class LexicalRulesParser {
	

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
