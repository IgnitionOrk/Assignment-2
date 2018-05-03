
public class Des2Round extends Round{
	public Des2Round(){
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