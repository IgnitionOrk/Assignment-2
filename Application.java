/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 
 * */
public class Application {
	public static void main(String[] args) {
		DES des = new DES("DES0", new Round());
		des.encrypt("", "");
		System.out.println("");
		des = new DES("DES1", new Des1Round());	
		des.encrypt("", "");
		System.out.println("");
		des = new DES("DES2", new Des2Round());
		des.encrypt("", "");
		System.out.println("");
		des = new DES("DES3", new Des3Round());
		des.encrypt("", "");
	}
}
