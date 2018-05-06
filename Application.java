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
			DES des = new DES("DES0", new Round());
			
			// Encryption
			des.initialize(0, key);
			System.out.println("Plaintext:  "+plaintext);
			String ciphertext = des.transform(plaintext);
			
			// Decryption
			des.initialize(1, key);
			System.out.println("Ciphertext: "+ciphertext);
			plaintext = des.transform(ciphertext);
			
			System.out.println("Plaintext:  "+plaintext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
