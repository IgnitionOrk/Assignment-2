
public class Des3Round extends Round{
	public Des3Round(){
		super();
	}
	/***/
	public String function(String rightSide, String key){
		super.expansionTable(rightSide);
		super.xor(rightSide, key);
		super.permutation(rightSide);
		return "";
	}
}