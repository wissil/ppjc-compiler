package hr.fer.zemris.ppj.compiler.lexical.actions;

import hr.fer.zemris.ppj.compiler.lexical.exec.Lex;

/**
 * Action that returns part of the input back to the <b>Lexical Analyzer</b>.
 * 
 * @author fiilip
 *
 */
public class GoBackAction implements LexAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1413584013173736098L;

	public static final String NAME = "VRATI_SE";
	
	/**
	 * Number of symbols to go back for.
	 */
	private final int goBack;
	
    /**
     * Creates a new action that returns the symbols into the input of {@link Lex}.
     *  
     * @param goBack Number of symbols that aren't returned.
     */
	public GoBackAction(int goBack) {
		this.goBack = goBack;
	}

	@Override
	public void execute(Lex lex) {
		lex.goBack(goBack);
	}
}
