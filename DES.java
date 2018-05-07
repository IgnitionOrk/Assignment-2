/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 
 * */
public class DES {
	private String version;
	private Round round;

	private String plaintext;
	private String key;
	private String ciphertext;
	private String[] roundText = new String[16];

	// initial permutation table taken from https://en.wikipedia.org/wiki/DES_supplementary_material
	private final int[] initialPermutationTable = new int[]{58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
	private final int[] finalPermutationTable = new int[]{40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
	private final int ROUNDS = 16;
	
	/**
	 * @param version
	 * @param round
	 * @param plaintext
	 * @param key
	 * @param ciphertext
	 */
	public DES(String version, Round round, String plaintext, String key, String ciphertext){
		this.version = version;
		this.round = round;
		this.plaintext=plaintext;
		this.key=key;
		this.ciphertext=ciphertext;
	}

	/**
	 * @param version
	 * @param round
	 * @param plaintext
	 * @param key
	 */
	public DES(String version, Round round, String plaintext, String key){
		this.version = version;
		this.round = round;
		this.plaintext=plaintext;
		this.key=key;
		this.ciphertext="";
	}

	/**
	 * @param mode
	 */
	public void initialize(int mode){
		//round.initialize(mode); //this will be done at the start of each transform now
	}

	public String encrypt()
	{
		if(plaintext.length()!=64 || key.length()!=56){return "";}
		return transform(true);
	}
	public String decrypt()
	{
		if(ciphertext.length()!=64 || key.length()!=56){return "";}
		return transform(false);
	}

	/**
	 * @param plainToCipher
	 * @return
	 */
	//true to encrypt, false to decrypt
	public String transform(boolean plainToCipher){
		String text=plaintext;
		if(plainToCipher==false)
		{
			round.initialize(1, key); //initialise our round to DECRYPT
			text=ciphertext;
		}else{
			round.initialize(0, key); //initialise our round to ENCRYPT
		}

		// Initial permutation;
		String permutatedInput = Transposition.permutation(text, initialPermutationTable);
		// Iteration through the rounds;
		String roundInput = rounds(permutatedInput);
		// Final permutation through the use of swapping left and right halves.
		String fpermutatedInput = swap(roundInput);
		// Final (Inverse) permutation; 
		text=Transposition.permutation(fpermutatedInput, finalPermutationTable);

		if(plainToCipher)
		{
			ciphertext=text;
		}else{
			plaintext=text;
		}

		return text;
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
		String temp=text;

		for(int i = 0; i < ROUNDS; i++){
			text = round.process(left(text), right(text));
			roundText[i]=text;
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
	public String version() {
		// TODO Auto-generated method stub
		return version;
	}

	/**
	 * @return
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @return
	 */
	public String getCiphertext()
	{
		return ciphertext;
	}

	/**
	 * @return
	 */
	public String getPlaintext()
	{
		return plaintext;
	}

	/**
	 * @param key
	 */
	public void setKey(String key)
	{
		this.key=key;
	}

	/**
	 * @param plaintext
	 */
	public void setPlaintext(String plaintext)
	{
		this.plaintext=plaintext;
	}

	/**
	 * @param ciphertext
	 */
	public void setCiphertext(String ciphertext)
	{
		this.ciphertext=ciphertext;
	}

	/**
	 * @param pos
	 * @return
	 */
	public String getRoundText(int pos)
	{
		if(pos>15){return "";}
		return roundText[pos];
	}

	/**
	 * @return
	 */
	public String[] getAllRoundText()
	{
		return roundText;
	}
}
