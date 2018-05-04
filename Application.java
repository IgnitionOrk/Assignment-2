/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * */

import java.util.Scanner;

public class Application {
	public static void main(String[] args) {
		String plaintext="";
		String key="";
		String ciphertext="";

		String test64="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890()";
		String test56="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234";

		/*RUN PROGRAM WITH PARAMETERS java Application < data.txt
		to load the plaintext/ciphertext*/
		Scanner scan = new Scanner(System.in);
		plaintext=scan.nextLine();
		key=scan.nextLine();

		System.out.println("Plaintext: "+plaintext);
		System.out.println("Key: "+key);

		System.out.println(new Round().xor("1100","1010"));

		DES des = new DES("DES0", new Round());
		ciphertext=des.encrypt(plaintext, key);

		System.out.println("Ciphertext: "+ciphertext);

		/*
		des = new DES("DES1", new Des1Round());	
		des.encrypt("", "");
		System.out.println("");
		des = new DES("DES2", new Des2Round());
		des.encrypt("", "");
		System.out.println("");
		des = new DES("DES3", new Des3Round());
		des.encrypt("", "");
		*/
	}
}
