package hr.fer.zemris.ppj.compiler.lexical.actions;

public class GoBackAction implements LexAction {

	public static final String NAME = "VRATI_SE";
	
	private final int goBack;
	
	public GoBackAction(int goBack) {
		this.goBack = goBack;
	}
}
