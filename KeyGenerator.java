/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */
public class KeyGenerator {
	private int rNumber;
	private int iCount;
	private String[] subkeys;
	private boolean reverseKeys;
	private int[] shifts = new int[]{1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1}; 
	private final int[] PC1 = new int[]{57,49,41,33,25,17,9,1,58,50,42,34,26,18,10,2,59,51,43,35,27,19,11,3,60,52,44,36,63,55,47,39,31,23,15,7,62,54,46,38,30,22,14,6,61,53,45,37,29,21,13,5,28,20,12,4};
	private final int[] PC2 = new int[]{14,17,11,24,1,5,3,28,15,6,21,10,23,19,12,4,26,8,16,7,27,20,13,2,41,52,31,37,47,55,30,40,51,45,33,48,44,49,39,56,34,53,46,42,50,36,29,32};
	private final int PADKEYLEGNTH = 64;
	private final char BIT = '1';
	/**
	 * @param key 
	 * @param b 
	 * 
	 */
	public KeyGenerator(boolean decrypting, String key){
		this.iCount = 0;
		this.subkeys = new String[shifts.length];
		this.reverseKeys = decrypting;
		this.generateSubkeys(key);

	}
	/**
	 * Initializes the subkey array 
	 * @param mode: A 1-bit used to determine if we are encrypting or decrypting.
	 * 1 = decrypting, otherwise we are encrypting. 
	 * @param key
	 */
	private void generateSubkeys(String key){
		if(reverseKeys){
			// Begin at the very end of the generated subkey array.
			this.rNumber = shifts.length;
		}
		else{
			this.rNumber = -1;
		}
		iCount = 0;
		key = padded(key);
		key = Transposition.permutation(key, PC1);
		subkeys(key);
	}
	/**
	 * @param key
	 * @return
	 */
	private String padded(String key){
		int eLength = 7;
		String padded = "";
		String sevenBits = "";
		for(int lowerBound = 0, upperBound = eLength; lowerBound < key.length() / eLength; lowerBound++, upperBound += eLength){
			sevenBits = key.substring(lowerBound * eLength, upperBound);
			// initial seven bits, but with an additional bit, return from the xor function. 
			padded += sevenBits+xor(sevenBits);
		}
		return padded;
	}
	/**
	 * @param sevenBits
	 * @return
	 */
	private String xor(String sevenBits){
		// If there is an even number of 'BITs' return the complement of BIT
		if(count(sevenBits) % 2 == 0){
			return complement();	
		}
		else{
			return complement();
		}
	}
	/**
	 * @return complement of BIT. 
	 */
	private String complement(){
		if(BIT == '0'){
			return "1";
		}
		else{
			return "0";
		}
	}
	/**
	 * Determines the number of occurrences of BIT within the argument 'sevenBits'.
	 * @param sevenBits
	 * @return
	 */
	private int count(String sevenBits){
		int count = 0;
		for(int i = 0; i < sevenBits.length(); i++){
			if(sevenBits.charAt(i) == BIT){
				count++;
			}
		}
		return count;
	}
	/**
	 * 
	 * @param key
	 */
	private void subkeys(String key) {
		String c = "";
		String d = "";
		for(int i = 0; i < subkeys.length; i++){
			c = leftShift(i, leftSide(key));
			d = leftShift(i, rightSide(key));
			key = c+d;
			add(Transposition.permutation(key, PC2));
		}
	}
	/**
	 * @param round
	 * @param side
	 * @return
	 */
	private String leftShift(int round, String side){
		int shift = shifts[round];
		String text = "";
		for(int i = 0, index = 0; i < side.length(); i++){
			index = (i + shift) % side.length();
			text += side.substring(index, index + 1);
		}
		return text;
	}
	/**
	 * @param subkey
	 */
	private void add(String subkey){
		subkeys[iCount] = subkey;
		iCount++;
	}
	/**
	 * @param iRound
	 * @return
	 */	
	public String subkey(){
		if(reverseKeys){
			rNumber--;
		}
		else{
			rNumber++;
		}
		return subkeys[rNumber];
	}
	/**
	 * @param key
	 * @return
	 */
	private String leftSide(String key){
		return key.substring(0, (key.length() / 2));
	}
	/**
	 * @param key
	 * @return
	 */
	private String rightSide(String key){
		return key.substring((key.length() / 2), key.length());
	}
}
