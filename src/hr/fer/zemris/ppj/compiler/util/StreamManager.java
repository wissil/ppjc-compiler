package hr.fer.zemris.ppj.compiler.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A class used to manage input and output streams.<br>
 * 
 * Input streams are normally used by generators, and output streams by analyzers.
 * 
 * @author fiilip
 *
 */
public class StreamManager {
	
	// names of files used by analizers and generators
	private static final String FOLDER = "analyzer";
	
	/**
	 * Name of the file to store lexical objects to.
	 */
	public static final String LEX_OBJECTS = "lex_objects.bin";
	
	/**
	 * Name of the file to store syntax objects to.
	 */
	public static final String SYN_OBJECTS = "syn_objects.bin";
	
	/**
	 * Standard charset used to encode textual streams.
	 */
	private static final Charset CS = StandardCharsets.UTF_8;

	/**
	 * Buffer capacity for the reader.
	 */
	private static final int BUFF_CAPACITY = 1 << 10;
	
	/**
	 * Default constructor.
	 */
	public StreamManager() {
		// create root directory
		createRootDir(FOLDER);
	}
	
	/**
	 * Gets the {@link ObjectOutputStream} used to read serialized objects.
	 * 
	 * @param fileName Name of the file with serialized data.
	 * @return {@link ObjectOutputStream} based on the <code>fileName</code>.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public ObjectOutputStream getOutputStream(String fileName) throws FileNotFoundException, IOException {
		return new ObjectOutputStream(new FileOutputStream(new File(fileName)));		
	}
	
	/**
	 * Gets the {@link ObjectInputStream} used to write serialized objects.
	 * 
	 * @param fileName Name of the file with serialized data.
	 * @return {@link ObjectInputStream} based on the <code>fileName</code>.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public ObjectInputStream getInputStream(String fileName) throws FileNotFoundException, IOException {
		return new ObjectInputStream(new FileInputStream(new File(fileName)));
	}
	
	/**
	 * Reads entire content of the <code>istream</code> to memory as {@link String}.
	 * 
	 * @param istream Input stream.
	 * @return String containing the entire input stream.
	 * @throws IOException 
	 */
	public String readFromStream(InputStream istream) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		byte[] buffer = new byte[BUFF_CAPACITY];
		int length;
		
		while ((length = istream.read(buffer)) > 0) {
			sb.append(new String(buffer, 0, length, CS));
		}
		
		return sb.toString();
	}
	
	/**
	 * Writes a given <code>object</code> as a string to the output stream.
	 * 
	 * @param object Object to be written to the output stream.
	 * @param ostream Output stream.
	 * @throws IOException 
	 */
	public void writeToStream(Object object, OutputStream ostream) throws IOException {
		ostream.write(object.toString().getBytes(CS));
	}
	
	/**
	 * Gets the path for generator.
	 * 
	 * @return String representing path for the generator.
	 */
	public static String getPath4Generator() {
		return String.format("%s/%s", FOLDER, LEX_OBJECTS);
	}
	
	/**
	 * Gets the path for analyzer.
	 * 
	 * @return String representing path for the analyzer.
	 */
	public static String getPath4Analyzer() {
		return String.format("%s/%s", FOLDER, LEX_OBJECTS);
	}

	/**
	 * Creates a root directory for sub-folders.
	 * 
	 * @param folder Root folder.
	 */
	private void createRootDir(String folder) {
		new File(FOLDER).mkdirs();
	}
}
