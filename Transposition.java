
public class Transposition {
	public static String permutation(String text, int[] table){
		String permutation = "";
		for(int i = 0; i < table.length; i++){
			permutation += text.substring(table[i] - 1, table[i]);
		}
		return permutation;
	}
}
