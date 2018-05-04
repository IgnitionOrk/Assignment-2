
public class Round{	/***/
	/***/
	public String function(String rightSide, String key) {
		expansionTable(rightSide);
		return "";
	}	
	/***/
	public String expansionTable(String rightSide){
		System.out.println("expansionTable");
		return "";
	}
	/***/
	public String inverseExpansionTable(String text){
		System.out.println("inverseExpansionTable");
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
				if(text.substring(i,1)==key.substring(i,1))
				{
					bit="0";
				}else{
					bit="1";
				}
				generatedText+=bit;
			}
		}

		return "";
	}
	/***/
	public String substitutionTable(String text){
		System.out.println("substitutionTable");
		return "";
	}
	/*initial permutations*/
	public String permutation(String text){
		return "permutation";
	}
	/***/
	public void hello(){
		System.out.println("hello");
	}
}
