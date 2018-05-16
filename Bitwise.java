/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */
public class Bitwise {
	/**
	 * @param text1
	 * @param text2
	 * @return
	 */
	public static String xor(String text1, String text2){
		String xored = "";
		for(int i = 0; i < text1.length(); i++){
			if(text1.charAt(i) == text2.charAt(i)){
				xored += "0";
			}
			else{
				xored += "1";
			}
		}
		return xored;
	}
}
