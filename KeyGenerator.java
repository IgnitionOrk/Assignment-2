/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */
public class KeyGenerator {
	private int rNumber;
	private int iCount;
	private int[] shifts = new int[]{1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1}; 
	private int[] PC2 = new int[]{14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
	private String[] subkeys;
	/**
	 * 
	 */
	public KeyGenerator(){
		this.rNumber = -1;
		this.iCount = 0;
		this.subkeys = new String[shifts.length];

	}
	/**
	 * Initializes the subkey array 
	 * @param mode: A 1-bit used to determine if we are encrypting or decrypting.
	 * 1 = decrypting, otherwise we are encrypting. 
	 * @param key
	 */
	public void initialize(int mode, String key){
		rNumber = -1;
		iCount = 0;
		generateSubkeys(key);
		if(mode == 1){
			reverseOrderOfSubKeys();
		}
	}
	/**
	 * 
	 * @param key
	 */
	private void generateSubkeys(String key) {
		String c = "";
		String d = "";
		for(int i = 0; i < subkeys.length; i++){
<<<<<<< HEAD
			c = leftShift(i, leftSide(key));
			d = leftShift(i, rightSide(key));
=======
			//System.out.println("KEY: "+key);
			c = leftShift(i, leftSide(key));
			d = leftShift(i, rightSide(key));
			//System.out.println("C:   "+c);
			//System.out.println("D:   "+d);
			//System.out.println("Round: "+i);
>>>>>>> 6f7ae0af5cbd823f0c32d3a799ad04b2bdd640fa
			key = c+d;
			add(permutate(key, PC2));
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
	 * Reverses the order of the subkeys
	 * @return: Returns the reversed order of subkeys. 
	 * 
	 */
	private String[] reverseOrderOfSubKeys(){
		String temp = "";
		for(int i = 0; i < subkeys.length / 2; i++){
			temp = subkeys[i];
			subkeys[i] = subkeys[subkeys.length - 1 - i];
			subkeys[subkeys.length - 1 - i] = temp;
		}
		return new String[2];
	}
	/**
	 * @param subkey
	 */
	private void add(String subkey){
		subkeys[iCount] = subkey;
		iCount++;
<<<<<<< HEAD
=======
		//System.out.println("Subkey: "+subkey);
>>>>>>> 6f7ae0af5cbd823f0c32d3a799ad04b2bdd640fa
	}
	/**
	 * @param iRound
	 * @return
	 */	
	public String subkey(){
		rNumber++;
		return subkeys[rNumber];
	}
	/**
	 * @param text
	 * @param pTable
	 * @return
	 */
	private String permutate(String text, int[] pTable) {
		String permutation = "";
		for(int i = 0; i < pTable.length; i++){
			permutation += text.substring(pTable[i] - 1, pTable[i]);
		}
		return permutation;
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
