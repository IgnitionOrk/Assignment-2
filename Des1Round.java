
public class Des1Round extends Round{
	public Des1Round(){
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
