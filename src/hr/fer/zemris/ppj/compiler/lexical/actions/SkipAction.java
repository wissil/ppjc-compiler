package hr.fer.zemris.ppj.compiler.lexical.actions;

import hr.fer.zemris.ppj.compiler.lexical.exec.Lex;

/**
 * Skips the input that is currently in the <b>Lexical Analyzer</b>.
 * 
 * @author fiilip
 *
 */
public class SkipAction implements LexAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2613110324182619994L;
	
	
	public static final String NAME = "-";

	@Override
	public void execute(Lex lex) {
		lex.skip();
	}
}
