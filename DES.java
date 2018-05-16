/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */
public class DES {
	public enum DESMode{
		/**
		 * The boolean argument determines if we need to reverse the subkeys.
		 * false: no need to reverse, otherwise reverse subkeys. 
		 * */
		ENCRYPT(false), 
		DECRYPT(true);
		private boolean value;
		
		/**
		 * @return: 
		 */
		public boolean valueOf(){return this.value;}
		DESMode(boolean value){ this.value = value;}
	}

	/**
	 * This enum contains the specifications for the 4 DES versions used in the assignment
	 * */
	public enum Version{
		DES0("DES0", new int[]{0, 1} ),
		DES1("DES1", new int[]{0} ),
		DES2("DES2", new int[]{2, 1} ),
		DES3("DES3", new int[]{2} );

		private String version;
		private int[] sequence;

		/**
		 * @return 
		 */
		public String getVersion(){ return version;	}
		public int[] getSequence(){ return sequence;}

		/**
		 * @param value
		 */
		Version(String version, int[] sequence){
			this.version = version;
			this.sequence = sequence;
		}
	}  


	private DESMode mode;
	private Version version;
	private KeyGenerator keyGenerator;
	private Round round;
	
	private String plaintext;
	private String ciphertext;
	private final int[] initialPermutationTable = new int[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
	private final int[] finalPermutationTable = new int[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
	private final int ROUNDS = 16;
	private String[] roundText = new String[ROUNDS]; //contains the text at the end of each round. Used to calculate Avalanche effect


	/**
	 * @param version: The version of DES.
	 */
	public DES(Version version){
		this.version = version;
		this.round = new Round(version.getSequence()); //use the specification found in Version to get the function sequence
	}

	/**
	 * @param mode
	 * @param key: 56-bit key. 
	 * @param mode: The mode of DES either encrypting or decrypting the text. 
	 */
	public void initializeCipher(DESMode mode, String key){
		this.mode = mode;
		// .valueOf will either be true (decrypting) or false (encrypting). 
		// It defines if the generated subkeys are applied in reverse order. 
		this.keyGenerator = new KeyGenerator(mode.valueOf(), key);
	}

	/**
	 * @param text: The text DES is going to transform into either plaintext or ciphertext. 
	 */
	public void begin(String text){
		if(mode.valueOf()){
			// Decrypting the ciphertext to obtains its plaintext
			ciphertext = text;
			plaintext = transform(ciphertext);
		}else{
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
		String permutatedInput = Transposition.permute(text, initialPermutationTable);
		// Iteration through the rounds;
		String roundInput = rounds(permutatedInput);
		// Final permutation through the use of swapping left and right halves.
		String fpermutatedInput = swap(roundInput);
		// Final (Inverse) permutation; 
		return Transposition.permute(fpermutatedInput, finalPermutationTable);
	}

	/**
	 * @param text
	 * @return
	 */
<<<<<<< HEAD
	private String swap(String text){ return right(text)+left(text); }
=======
	private String swap(String text){
		return right(text)+left(text);
	}

>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
	/**
	 * @param text
	 * @return
	 */
	private String rounds(String text){
		for(int i = 0; i < ROUNDS; i++){
			text = round.process(left(text), right(text), keyGenerator.subkey()); //call our Round on the text with the correct subkey
			roundText[i] = text;
		}

		return text;
	}
	/**
	 * @param text
	 * @return
	 */
	private String left(String text){ return text.substring(0, (text.length() / 2)); }
	/**
	 * @param text
	 * @return
	 */
	private String right(String text){ return text.substring((text.length() / 2), text.length());}
	/**
	 * @return the version of DES. 
	 */
<<<<<<< HEAD
	public String version(){return version.getVersion();}
=======
	public String version(){
		return version.getVersion();
	}

>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
	/**
	 * @return the result after a block of 64-bit has been gone through DES Rounds.
	 */
	public String getCiphertext(){return ciphertext;}

	/**
	 * @return the original 64-bit plaintext.
	 */
<<<<<<< HEAD
	public String getPlaintext(){return plaintext;}
=======
	public String getPlaintext(){
		return plaintext;
	}

>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
	/**
	 * @param pos
	 * @return
	 */
	public String getRoundText(int pos){ return roundText[pos];}
}
