/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 
 * */
public class DES {
	private String version;
	private Round round;
	private KeyGenerator keyGenerator;
	private final int ROUNDS = 1;
	/***/
	public DES(String version, Round round){
		this.version = version;
		this.round = round;
		this.keyGenerator = new KeyGenerator();
	}
	/***/
	public String encrypt(String plaintext, String key){
		String permutatedInput = initialPermutation(plaintext);
		String roundInput = rounds(permutatedInput, key);
		//String inversePermutatedInput = swap(roundInput);
		return inverseInitialPermutation("");
	}
	/***/
	public String decrypt(String ciphertext, String key){
		String inversePermutatedInput = inverseInitialPermutation(ciphertext);
		String roundInput = rounds(inversePermutatedInput, key);
		String permutatedInput = swap(roundInput);
		return initialPermutation(permutatedInput);
	}
	/***/
	private String swap(String text){
		String leftSide = text.substring(0, (text.length() / 2) + 1);
		String rightSide = text.substring((text.length() / 2) + 1);
		return rightSide+leftSide;
	}
	/***/
	private String rounds(String text, String subkey){
		String leftSide = "";
		String rightSide = "";
		for(int i = 0; i < ROUNDS; i++){
			round.function(rightSide, subkey);
			subkey = keyGenerator.generateKey(subkey);
		}
		return text;
	}
	
	private String initialPermutation(String text){
		return "";
	}
	
	private String inverseInitialPermutation(String text){
		return "";
	}
}
