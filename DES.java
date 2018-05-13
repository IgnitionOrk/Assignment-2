/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */
public class DES {
	public enum DESMode{
		// The boolean argument determines if we need to reverse the subkeys.
		// false: no need to reverse, otherwise reverse subkeys. 
		ENCRYPT(false), 
		DECRYPT(true);
		private boolean value;
		
		/**
		 * @return
		 */
		public boolean valueOf(){
			return value;
		}
		/**
		 * @param value
		 */
		DESMode(boolean value){
			this.value = value;
		}
	}
	private DESMode mode;
	private KeyGenerator keyGenerator; 
	private Version version;
	private Round round;
	private String plaintext;
	private String ciphertext;
	private final int[] initialPermutationTable = new int[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
	private final int[] finalPermutationTable = new int[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
	private final int ROUNDS = 16;
	private String[] roundText = new String[ROUNDS];
	/**
	 * @param version: The version of DES.
	 * @param round: Round process, used to transform the text. 
	 * @param text: 64-bit text we wish to either encrypt or decrypt. 
	 * @param key: 56-bit key. 
	 * @param mode: The mode of DES either encrypting or decrypting the text. 
	 */
	public DES(Version version){
		this.version = version;
		this.round = new Round(version.getSequence());
	}
	/**
	 * @param mode
	 * @param key
	 */
	public void initializeCipher(DESMode mode, String key){
		this.mode = mode;
		// .valueOf will either be true or false, this is dependent on the mode of the DES.
		// DESMode is defined at the beginning of the class. 
		this.keyGenerator = new KeyGenerator(mode.valueOf(), key);
		
	}
	/**
	 * @param text
	 */
	public void begin(String text){
		if(mode.valueOf()){
			// Decrypting the ciphertext to obtains its plaintext
			ciphertext = text;
			plaintext = transform(ciphertext);
		}
		else{
			// Encrypting the plaintext to obtains its ciphertext
			plaintext = text;
			ciphertext = transform(plaintext);
		}
	}
	/**
	 * @param text
	 * @return
	 */
	private String transform(String text){
		// Initial permutation;
		String permutatedInput = Transposition.permutation(text, initialPermutationTable);
		// Iteration through the rounds;
		String roundInput = rounds(permutatedInput);
		// Final permutation through the use of swapping left and right halves.
		String fpermutatedInput = swap(roundInput);
		// Final (Inverse) permutation; 
		return Transposition.permutation(fpermutatedInput, finalPermutationTable);
	}
	/**
	 * @param text
	 * @return
	 */
	private String swap(String text){
		return right(text)+left(text);
	}
	/**
	 * @param text
	 * @return
	 */
	private String rounds(String text){
		for(int i = 0; i < ROUNDS; i++){
			text = round.process(left(text), right(text), keyGenerator.subkey());
			roundText[i] = text;
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
	 * @return
	 */
	public String version(){
		return version.getVersion();
	}
	/**
	 * @return
	 */
	public String getCiphertext(){
		return ciphertext;
	}

	/**
	 * @return
	 */
	public String getPlaintext(){
		return plaintext;
	}
	/**
	 * @param pos
	 * @return
	 */
	public String getRoundText(int pos){
		return roundText[pos];
	}
}
