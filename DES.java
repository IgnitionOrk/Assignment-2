/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 
 * */
public class DES {
	private String version;
	private Round round;
	// initial permutation table taken from https://en.wikipedia.org/wiki/DES_supplementary_material
	private final int[] initialPermutationTable = new int[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
	private final int[] finalPermutationTable = new int[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};

	private final int ROUNDS = 16;
	/**
	 * @param version
	 * @param round
	 */
	public DES(String version, Round round){
		this.version = version;
		this.round = round;
	}
	/**
	 * @param plaintext
	 * @param key
	 * @return
	 */
	public String encrypt(String plaintext, String key){
		String permutatedInput = initialPermutation(plaintext);
		round.initialize(0, key);
		String roundInput = rounds(permutatedInput);
		String fpermutatedInput = swap(roundInput);
		return finalInitialPermutation(fpermutatedInput);
	}
	/**
	 * @param ciphertext
	 * @param key
	 * @return
	 */
	public String decrypt(String ciphertext, String key){
		String inversePermutatedInput = finalInitialPermutation(ciphertext);
		// reversing the order in which are used.  
		round.initialize(1, key);
		String roundInput = rounds(inversePermutatedInput);
		String permutatedInput = swap(roundInput);
		return initialPermutation(permutatedInput);
	}
	/**
	 * @param text
	 * @return
	 */
	private String swap(String text){
		String leftSide = text.substring(0, (text.length() / 2) + 1);
		String rightSide = text.substring((text.length() / 2) + 1);
		return rightSide+leftSide;
	}
	/**
	 * @param text
	 * @return
	 */
	private String rounds(String text){
		for(int i = 0; i < ROUNDS; i++){
			text = round.process(left(text), right(text));
		}
		return text;
	}
	/**
	 * @param text
	 * @return
	 */
	private String left(String text){
		return text.substring(0, (text.length() / 2));
	}
	/**
	 * @param text
	 * @return
	 */
	private String right(String text){
		return text.substring((text.length() / 2), text.length());
	}
	/**
	 * @param text
	 * @return
	 */
	public String initialPermutation(String text){
		return permutate(text, initialPermutationTable);
	}
	/**
	 * @param text
	 * @return
	 */
	private String finalInitialPermutation(String text){
		return permutate(text, finalPermutationTable);
	}
	/**
	 * @param text
	 * @param table
	 * @return
	 */
	private String permutate(String text, int[] table){
		String permutation = "";
		for(int i = 0; i < text.length(); i++){
			permutation += text.substring(table[i] - 1, table[i]);
		}
		return permutation;
	}
}
