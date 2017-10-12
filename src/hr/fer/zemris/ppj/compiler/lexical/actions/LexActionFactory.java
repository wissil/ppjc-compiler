package hr.fer.zemris.ppj.compiler.lexical.actions;

/**
 * Factory class that produces {@link LexAction} objects.
 * 
 * @author fiilip
 *
 */
public class LexActionFactory {

	/**
	 * Factory method.<br>
	 * Produces a single {@link LexAction} objects based on the passed <code>args</code>.
	 * 
	 * @param args	Arguments used to produce a {@link LexAction} object.
	 * @return		New {@link LexAction} object based on the <code>args</code>.
	 */
	public static LexAction produce(String[] args) {		
		switch (args[0]) {
			case NewLineAction.NAME: return new NewLineAction();
			case EnterStateAction.NAME: return new EnterStateAction();
			case GoBackAction.NAME: return new GoBackAction(Integer.parseInt(args[1]));
			case SkipAction.NAME: return new SkipAction();
			default: throw new IllegalArgumentException(String.format("Action with name %s is not supported.", args[0]));
		}
	}
}
