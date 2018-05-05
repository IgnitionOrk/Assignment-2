
public class Round{	/***/
	/***/
	public String function(String rightSide, String key) {
		return "";
	}	
	/***/
	public String expansionTable(String rightSide){
		return "";
	}
	/***/
	public String inverseExpansionTable(String text){
		return "";
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
	/***/
	public String substitutionTable(String text){
		return "";
	}
	/*initial permutations*/
	public String permutation(String text){
		return "permutation";
	}
}
