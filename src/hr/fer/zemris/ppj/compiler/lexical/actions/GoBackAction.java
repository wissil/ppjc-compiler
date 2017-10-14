package hr.fer.zemris.ppj.compiler.lexical.actions;

import hr.fer.zemris.ppj.compiler.lexical.exec.Lex;

public class GoBackAction implements LexAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1413584013173736098L;

	public static final String NAME = "VRATI_SE";
	
	private final int goBack;
	
	public GoBackAction(int goBack) {
		this.goBack = goBack;
	}

	@Override
	public void execute(Lex lex) {
		// TODO Auto-generated method stub
		
	}
}
