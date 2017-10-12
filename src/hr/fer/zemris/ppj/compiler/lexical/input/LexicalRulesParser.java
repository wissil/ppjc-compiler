package hr.fer.zemris.ppj.compiler.lexical.input;

import hr.fer.zemris.ppj.compiler.lexical.actions.EnterStateAction;
import hr.fer.zemris.ppj.compiler.lexical.actions.GoBackAction;
import hr.fer.zemris.ppj.compiler.lexical.actions.LexAction;
import hr.fer.zemris.ppj.compiler.lexical.actions.NewLineAction;
import hr.fer.zemris.ppj.compiler.lexical.actions.SkipAction;

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
	private LexAction createAction(String line) {
		String args[] = line.split("\\s+");
		
		switch (args[0]) {
			case NewLineAction.NAME: return new NewLineAction();
			case EnterStateAction.NAME: return new EnterStateAction();
			case GoBackAction.NAME: return new GoBackAction(Integer.parseInt(args[1]));
			case SkipAction.NAME: return new SkipAction();
			default: throw new IllegalArgumentException(String.format("Action with name %s is not supported.", args[0]));
		}
	}

}
