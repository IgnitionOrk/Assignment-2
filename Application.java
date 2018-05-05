/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/*
 * 
 	RUN PROGRAM WITH PARAMETERS java Application < data.txt
	to load the plaintext/ciphertext
	String test64="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890()";
	String test56="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234";
*/
public class Application {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(new File(args[0]));
			String plaintext = scanner.nextLine();
			String key = scanner.nextLine();
			//DES des = new DES("DES0", new Round());
			//String ciphertext = des.encrypt(plaintext, key);
			KeyGenerator keyGenerator = new KeyGenerator();
			
			key = "11110000110011001010101011110101010101100110011110001111";
			keyGenerator.initiate(0, key);
			System.out.println("ENCRYPTION");
			for(int i = 0; i < 16; i++){
				System.out.println("Round "+i+": subkey: "+keyGenerator.subkey(i));
			}
			
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			keyGenerator.initiate(1, key);
			System.out.println("DECRYPTION");
			int index = 15;
			int x = 0;
			for(int i = 0; i < 16; i++){
				x = index - i;
				System.out.println("Round "+x+": subkey: "+keyGenerator.subkey(i));
			}
			String test32="abcdefghijklmnopqrstuvwxyzABCDEF";
			System.out.println(test32 +  " length: "+test32.length());
			String result = new Round().expansion(test32);
			System.out.println(result + "  length: "+result.length());
			String result2 = new Round().inverseExpansion(result);
			System.out.println(result2 + "  length: "+result2.length());
			String pResult = new Round().permutation(test32);
			System.out.println(pResult + "  length: "+pResult.length());
			//System.out.println("Ciphertext: "+ciphertext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
