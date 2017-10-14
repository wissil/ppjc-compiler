package hr.fer.zemris.ppj.compiler.lexical.actions;

import hr.fer.zemris.ppj.compiler.lexical.exec.Lex;

public class EnterStateAction implements LexAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6138346673209279731L;

	public static final String NAME = "UDJI_U_STANJE";
	
	/**
	 * New state of the lexical analyzer.
	 */
	private final String state;
	
	/**
	 * Creates a new instance of {@link EnterStateAction}.
	 * 
	 * @param state	New state of the lexical analyzer. State about to be entered.
	 */
	public EnterStateAction(String state) {
		this.state = state;
	}

	@Override
	public void execute(Lex lex) {
		// TODO Auto-generated method stub
		
	}

}
