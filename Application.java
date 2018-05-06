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
			DES des0 = new DES("DES0", new Round(new int[]{0, 1, 2, 3}));
			DES des1 = new DES("DES1", new Round(new int[]{0, 1, 2}));
			DES des2 = new DES("DES2", new Round(new int[]{0, 1, 4, 3}));
			DES des3 = new DES("DES3", new Round(new int[]{0, 1, 4}));

			System.out.println("VERSION: "+des0.version());
			des0.initialize(0, key);
			System.out.println("ORIGINAL  Plaintext:  "+plaintext);
			String ciphertext0 = des0.transform(plaintext);
			des0.initialize(1, key);
			System.out.println("Ciphertext:           "+ciphertext0);
			plaintext = des0.transform(ciphertext0);
			System.out.println("DECRYPTED Plaintext:  "+plaintext);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

			System.out.println("VERSION: "+des1.version());
			des1.initialize(0, key);
			System.out.println("ORIGINAL  Plaintext:  "+plaintext);
			String ciphertext1 = des1.transform(plaintext);
			des1.initialize(1, key);
			System.out.println("Ciphertext:           "+ciphertext1);
			plaintext = des1.transform(ciphertext1);
			System.out.println("DECRYPTED Plaintext:  "+plaintext);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

			System.out.println("VERSION: "+des2.version());
			des2.initialize(0, key);
			System.out.println("ORIGINAL  Plaintext:  "+plaintext);
			String ciphertext2 = des2.transform(plaintext);
			des2.initialize(1, key);
			System.out.println("Ciphertext:           "+ciphertext2);
			plaintext = des2.transform(ciphertext2);
			System.out.println("DECRYPTED Plaintext:  "+plaintext);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

			System.out.println("VERSION: "+des3.version());
			des3.initialize(0, key);
			System.out.println("ORIGINAL  Plaintext:  "+plaintext);
			String ciphertext3 = des3.transform(plaintext);			
			des3.initialize(1, key);
			System.out.println("Ciphertext:           "+ciphertext3);
			plaintext = des3.transform(ciphertext3);
			System.out.println("DECRYPTED Plaintext:  "+plaintext);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
