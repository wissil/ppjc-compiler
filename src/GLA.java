import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import hr.fer.zemris.ppj.compiler.lexical.input.LexicalDefinitionsParser;
import hr.fer.zemris.ppj.compiler.util.StreamManager;

/**
 * <b>Generator of Lexical Analyzer</b><br>
 * 
 * This class is used for generating the <b>Lexical Analyzer</b> 
 * based on it's definitions. <br>
 * 
 * As per current version, it reads in the definitions stream, creates
 * automatons and writes them to the file that will be read by the <b>Lexical Analyzer</b>.
 * 
 * 
 * @author fiilip
 *
 */
public class GLA {

	/**
	 * Program entry - point.
	 * 
	 * @param args	Not used.
	 */
	public static void main(String[] args) {
		new GLA(System.in).generateLA(StreamManager.LEX_OBJECTS);
	}
	
	private final StreamManager streamManager;
	
	/**
	 * Source of the input file containing the definitions.<br>
	 * Possible sources: System.in, FileInputStream, etc.
	 */
	private final InputStream istream;
	
	/**
	 * Public constructor.<br>
	 * Constructs the new {@link GLA} object.
	 * 
	 * @param istream	Stream of the input definitions.
	 */
	public GLA(InputStream istream) {
		this.istream = istream;
		this.streamManager = new StreamManager();
	}

	/**
	 * Generates the objects needed by the {@link LA}.<br>
	 * In terms, generates the automatons used by the <b>Lexical Analyzer</b>.
	 * 
	 * @param toFilename		Name of the file that this {@link GLA} generates it's output.
	 */
	public void generateLA(String toFilename) {
		LexicalDefinitionsParser parser = new LexicalDefinitionsParser(istream);
		
		// parse the input file into the needed data structures
		parser.parse();
		
		try (ObjectOutputStream stream = streamManager.getOutputStream(toFilename)) {
			stream.writeObject(parser.getStartState());
			stream.writeObject(parser.getStates());
			stream.writeObject(parser.getAutomatonMerged());
		} catch (IOException e) {
			System.err.println(String.format("Error occured in GLA: %s.", e.getMessage()));
		}
	}
	
}
