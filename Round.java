/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */

public class Round{	
	private KeyGenerator keyGenerator;
	private int[] eTable = new int[]{32,1,2,3,4,5,4,5,6,7,8,9,8,9,10,11,12,13,12,13,14,15,16,17,16,17,18,19,20,21,20,21,22,23,24,25,24,25,26,27,28,29,28,29,30,31,32,1};
	private int[] iETable = new int[]{2,3,4,5,8,9,10,11,14,15,16,17,20,21,22,23,26,27,28,29,32,33,34,35,38,39,40,41,44,45,46,47};
	private int[] pTable = new int[]{16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};
	public Round(){
		this.keyGenerator = new KeyGenerator();
	}
	/**
	 * @param rightSide
	 * @param subkey
	 * @return
	 */
	public void initialize(int mode, String key) {
		keyGenerator.initialize(mode, key);
	}
	/**
	 * @param leftSide
	 * @param rightSide
	 * @return
	 */
	public String process(String leftSide, String rightSide){
		String leftXor = leftSide;
		leftSide = rightSide;
		rightSide = function(rightSide, keyGenerator.subkey());
		rightSide = xor(leftXor, rightSide);
		return leftSide+rightSide;
	}
	/**
	 * @param rightSide
	 * @param subkey
	 * @return
	 */
	private String function(String rightSide, String subkey) {
		rightSide = expansion(rightSide);
		rightSide = xor(rightSide, subkey);
		//rightSide = substitution(rightSide);
		rightSide = permutation(rightSide);
		return rightSide;
	}	
	/**
	 * @param text
	 * @return
	 */
	public String expansion(String text){
		return permutate(text, eTable);
	}
	/**
	 * @param text
	 * @return
	 */
	public String inverseExpansion(String text){
		return permutate(text, iETable);
	}	
	/**
	 * @param text
	 * @return
	 */
	public String permutation(String text){
		return permutate(text, pTable);
	}
	/**
	 * @param text
	 * @param table
	 * @return
	 */
	private String permutate(String text, int[] table){
		String expandedText = "";
		for(int i = 0; i < table.length;i++){
			expandedText += text.substring(table[i] - 1, table[i]);
		}
		return expandedText;
	}
	/**
	 * @param text
	 * @return
	 */
	public String substitution(String text){
		return text;
	}
	/*xor's 2 texts over each other. Generated text will be the same length as the shorter text*/
	public String xor(String text, String key){
		String generatedText="";
		String bit="";

		for(int i=0; i<text.length(); i++)
		{
			if(i<key.length())
			{
				if(text.substring(i,i+1).equals(key.substring(i,i+1)))
				{
					bit="0";
				}else{
					bit="1";
				}
				generatedText+=bit;
			}
		}

		return generatedText;
	}
}
