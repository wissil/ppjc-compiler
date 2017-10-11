import java.io.InputStream;

/**
 * <b>Generator of Lexical Analyzer.</b> <br>
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
		// TODO Auto-generated method stub

	}
	
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
	}

	/**
	 * Generates the objects needed by the {@link LA}.<br>
	 * 
	 * In terms, generates the automatons used by the <b>Lexical Analyzer</b>.
	 */
	public void generateLA() {
		
	}
}
