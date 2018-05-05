
public class Des3Round extends Round{
	public Des3Round(){
		super();
	}
	/***/
	public String function(String rightSide, String subkey) {
		rightSide = expansion(rightSide);
		rightSide = xor(rightSide, subkey);
		rightSide = inverseExpansion(rightSide);
		return rightSide;
	}
}