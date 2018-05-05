
public class Des2Round extends Round{
	public Des2Round(){
		super();
	}
	/***/
	public String function(String rightSide, String subkey) {
		rightSide = expansion(rightSide);
		rightSide = xor(rightSide, subkey);
		rightSide = inverseExpansion(rightSide);
		rightSide = permutation(rightSide);
		return rightSide;
	}
}