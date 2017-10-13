package hr.fer.zemris.ppj.compiler.lexical.regex;

import java.util.Map;

/**
 * Utility class for manipulation with regular expressions.
 * 
 * @author fiilip
 *
 */
public class RegexUtil {
	
	/**
	 * Symbol that represents a start of a regular definition within a regEx.
	 */
	private static final char REG_DEF_START_SYM = '{';
	
	/**
	 * Symbol that represents an end of a regular definition within a regEx.
	 */
	private static final char REG_DEF_END_SYM = '}';
	
    /**
     * Removes the regular definition from the regular expression, and swaps
     * it with a real regular expression.
     * 
     * @param regEx		Regular expression to be normalized.
     * @param regDefs	Regular definitions.
     * @return			Normalized regular expression.
     */
    public static String normalize(String regEx, Map<String, String> regDefs) {
    		StringBuilder sb = new StringBuilder();
    		int len = regEx.length();
    		
    		for (int idx=0; idx<len; idx++) {
    			if (regEx.charAt(idx) == REG_DEF_START_SYM && !isPrefixed(regEx, idx)) {
    				int cidx = findCloser(regEx, REG_DEF_START_SYM, REG_DEF_END_SYM, idx);    				
                String regdef = regEx.substring(idx + 1, cidx);
                
                String reg = regDefs.get(regdef);
                
                if (reg == null) {
                		throw new IllegalStateException(
                				String.format("No regular expression defined for reg. def. {%s}", regdef));
                }
                
                sb.append('(');
                sb.append(reg);
                sb.append(')');
                idx = cidx;
    			} else {
    				sb.append(regEx.charAt(idx));
    			}
    		}	
    		return sb.toString();
    }
    
    /**
     * Finds the next closing operator starting from given index in a regEx.
     * 
     * @param regEx	Regular expression being searched.
     * @param open	Opening operator
     * @param close Closing operator
     * @param startFrom	Starting index
     * 
     * @return Index of a first found closing operator, <code>-1</code> if none found.
     */
    private static int findCloser(String regex, char open, char close, int startFrom) {
        int len = regex.length();
        int operators = 0;
        
        for (int idx = startFrom; idx < len; idx++) {
            char sym = regex.charAt(idx);
            
            if (isPrefixed(regex, idx)) {
                continue;
            }
            
            if (sym == open) {
                operators ++;
            } 
            
            if (sym == close) {
            		operators --;
            }
            
            if (operators == 0) {
            		return idx;
            }
        }
        
        return -1;
    }
    
    /**
     * Evaluates whether or not the character at index <code>idx</code> in <code>regEx</code> is prefixed or not.<br>
     * 
     * The character is considered to be prefixed if it's preceded by the odd number of '\' symbols.
     * 
     * @param regEx	Regular expression.
     * @param idx	Index of character evaluated for being prefixed.
     * @return		<code>True</code> if the character at <code>idx</code> is prefixed, and <code>false</code> otherwise.
     */
    private static boolean isPrefixed(String regEx, int idx) {
    		if (idx <= 0) {
    			return false;
    		}
    		
    		int numOfPrefs = 0;
    		int i = idx - 1;
    		
    		while (i >= 0 && regEx.charAt(i) == '\\') {
    			numOfPrefs ++;
    			i --;
    		}
    		
    		return numOfPrefs % 2 != 0;
    }
}
