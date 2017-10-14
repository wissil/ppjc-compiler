package hr.fer.zemris.ppj.compiler.lexical.exec;

import java.io.Serializable;

/**
 * Interface that represents any lexical entity that can be executed
 * by {@link Lex}.
 * 
 * @author fiilip
 *
 */
public interface LexExecutable extends Serializable {

	/**
	 * Execution method ran by the lexical analyzer.
	 * 
	 * @param lex Lexical analyzer.
	 */
	void execute(Lex lex);
}
