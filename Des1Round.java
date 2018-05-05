
public class Des1Round extends Round{
	public Des1Round(){
		super();
	}
	/***/
	public String function(String rightSide, String subkey) {
		rightSide = expansion(rightSide);
		rightSide = xor(rightSide, subkey);
		rightSide = substitution(rightSide);
		return rightSide;
	}
}
