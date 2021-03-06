import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.ppj.compiler.lexical.automata.LexAutomatonMerged;
import hr.fer.zemris.ppj.compiler.lexical.exec.Lex;
import hr.fer.zemris.ppj.compiler.lexical.exec.LexRule;
import hr.fer.zemris.ppj.compiler.util.StreamManager;

/**
 * <b>Lexical Analyzer</b><br>
 * 
 * This class represents a <b>Lexical Analyzer</b> based on the 
 * automatons generated by the {@link GLA}.<br>
 * 
 * It reads the automatons, and analyzes the input based on the automatons
 * that it's read.
 * 
 * @author fiilip
 *
 */
public class LA {

	/**
	 * Program entry - point.
	 * 
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		new LA(System.in, System.out).analyze(StreamManager.LEX_OBJECTS);
	}
	
	/**
	 * Input stream to be analyzed by the <b>Lexical Analyzer</b>.
	 */
	private final InputStream istream;
	
	/**
	 * Output stream for the resulting analysis.
	 */
	private final OutputStream ostream;
	
	private final StreamManager streamManager;
	
	/**
	 * Public constructor.<br>
	 * 
	 * Creates the new instance of {@link LA}.
	 * 
	 * @param istream	Input stream to be analyzed.
	 * @param ostream	Output stream for the resulting analysis.
	 */
	public LA(InputStream istream, OutputStream ostream) {
		this.istream = istream;
		this.ostream = ostream;
		this.streamManager = new StreamManager();
	}
	
	/**
	 * Performs the lexical analysis of the input stream.
	 * 
	 * @param filename	Name of the file to be lexically analyzed.
	 */
	@SuppressWarnings("unchecked")
	public void analyze(String filename) {
		
        try (ObjectInputStream stream = streamManager.getInputStream(filename)) {
            String startState = (String) stream.readObject();
            Map<String, List<LexRule>> states = (Map<String, List<LexRule>>) stream.readObject();
            LexAutomatonMerged merged = (LexAutomatonMerged) stream.readObject();
            
            new Lex(startState, states, streamManager, ostream, merged).analyze(istream);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(String.format("Error in LA: %s.", e.getMessage()));
        }
	}
}
