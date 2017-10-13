package hr.fer.zemris.ppj.compiler.lexical.actions;

public class EnterStateAction implements LexAction {
	
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

}
