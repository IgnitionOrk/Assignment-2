/**
 * Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * 
 * This program will encrypt and decrypt a 64 bit message using a 56 bit key using a hand-coded version of the DES algorithm.
 * It contains the original DES algorithm, as well as three slightly modified versions to demonstrate the importance of each part.
 * It will also encrypt each message P under every possible Ki where Ki differs from key K by 1 bit.
 * As well as encrypt each message Pi under key K, where Pi is every possible message that differs from P by 1 bit.
 * Finally it will check the average difference between the text generated at the end of each of the 16 rounds for Pi
 *	and that of message P (and the same for Ki and K). It does this for each algorithm DES0/1/2/3 and demonstrates how much a small
 *	change to the algorithm affects the security of the cipher.
 * */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Application {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {	
			Scanner scanner = new Scanner(new File(args[0]));
			String text = scanner.nextLine();
			String key = scanner.nextLine();
			if(args[0].toLowerCase().contains("decryption")){
				decryptFile(text, key);
			}
			else{
				encryptFile(text, key);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param scanner 
	 * 
	 */
	private static void encryptFile(String plaintext, String key){
		String p[] = bitDifferenceArray(plaintext); // An array of plaintexts differing by 1-bit.
		String k[] = bitDifferenceArray(key);	// An array of keys differing by 1-bit.
		/**
		P and Pi under K, DES0/1/2/3
		*/
		DES des0Normal=new DES("DES0", new Round(new int[]{0, 1, 2, 3}), plaintext, key);
		DES des1Normal=new DES("DES1", new Round(new int[]{0, 1, 2}), plaintext, key);
		DES des2Normal=new DES("DES2", new Round(new int[]{0, 1, 4, 3}), plaintext, key);
		DES des3Normal=new DES("DES3", new Round(new int[]{0, 1, 4}), plaintext, key);

		des0Normal.encrypt();
		des1Normal.encrypt();
		des2Normal.encrypt();
		des3Normal.encrypt();

		DES des0[] = new DES[64];
		DES des1[] = new DES[64];
		DES des2[] = new DES[64];
		DES des3[] = new DES[64];

		for(int i=0; i<64; i++)
		{
			des0[i]=new DES("DES0", new Round(new int[]{0, 1, 2, 3}), p[i], key); //encrypt P and Pi{1-63} under K
			des0[i].encrypt();
			des1[i]=new DES("DES1", new Round(new int[]{0, 1, 2}), p[i], key); //encrypt P and Pi{1-63} under K
			des1[i].encrypt();
			des2[i]=new DES("DES2", new Round(new int[]{0, 1, 4, 3}), p[i], key); //encrypt P and Pi{1-63} under K
			des2[i].encrypt();
			des3[i]=new DES("DES3", new Round(new int[]{0, 1, 4}), p[i], key); //encrypt P and Pi{1-63} under K
			des3[i].encrypt();
		}

		/**
		P under K and Ki, DES0/1/2/3
		*/

		DES desK0Normal=new DES("DES0", new Round(new int[]{0, 1, 2, 3}), plaintext, key);
		DES desK1Normal=new DES("DES1", new Round(new int[]{0, 1, 2}), plaintext, key);
		DES desK2Normal=new DES("DES2", new Round(new int[]{0, 1, 4, 3}), plaintext, key);
		DES desK3Normal=new DES("DES3", new Round(new int[]{0, 1, 4}), plaintext, key);

		desK0Normal.encrypt();
		desK1Normal.encrypt();
		desK2Normal.encrypt();
		desK3Normal.encrypt();

		DES desK0[] = new DES[64];
		DES desK1[] = new DES[64];
		DES desK2[] = new DES[64];
		DES desK3[] = new DES[64];
		for(int i=0; i<56; i++)
		{
			desK0[i]=new DES("DES0", new Round(new int[]{0, 1, 2, 3}), plaintext, k[i]); //encrypt P and Pi{1-63} under K
			desK0[i].encrypt();
			desK1[i]=new DES("DES1", new Round(new int[]{0, 1, 2}), plaintext, k[i]); //encrypt P and Pi{1-63} under K
			desK1[i].encrypt();
			desK2[i]=new DES("DES2", new Round(new int[]{0, 1, 4, 3}), plaintext, k[i]); //encrypt P and Pi{1-63} under K
			desK2[i].encrypt();
			desK3[i]=new DES("DES3", new Round(new int[]{0, 1, 4}), plaintext, k[i]); //encrypt P and Pi{1-63} under K
			desK3[i].encrypt();
		}

		/**
		finished initialising all the DES objects
		*/

		try{
		    PrintWriter eWriter = new PrintWriter("ENCRYPTIONOUTPUT.txt", "UTF-8");
		    
		    eWriter.write("ENCRYPTION\n");
		    eWriter.write("Plaintext P:" + plaintext+"\n");
		    eWriter.write("Key K:" + key+"\t\n");
		    eWriter.write("Ciphertext C:"+ des0Normal.getCiphertext()+"\n");


		    eWriter.write("\nAvalanche:\nP and Pi under K\nRound:\tDES0\tDES1\tDES2\tDES3\n");
		    eWriter.write("0\t\t1\t\t1\t\t1\t\t1\n");
			for(int i=0; i<16; i++)
			{
				double avalanche0=1;
				double avalanche1=1;
				double avalanche2=1;
				double avalanche3=1;
				for(int o=0; o<64; o++) //for each round, check the average avalanche for each of Pi
				{
					avalanche0+=checkDifferences(des0Normal.getRoundText(i), des0[o].getRoundText(i));
					avalanche1+=checkDifferences(des1Normal.getRoundText(i), des1[o].getRoundText(i));
					avalanche2+=checkDifferences(des2Normal.getRoundText(i), des2[o].getRoundText(i));
					avalanche3+=checkDifferences(des3Normal.getRoundText(i), des3[o].getRoundText(i));
				}
				int a0=(int)Math.round(avalanche0/64.0); //take the average
				int a1=(int)Math.round(avalanche1/64.0); //take the average
				int a2=(int)Math.round(avalanche2/64.0); //take the average
				int a3=(int)Math.round(avalanche3/64.0); //take the average

				eWriter.write((i+1)+"\t\t"+a0+"\t\t"+a1+"\t\t"+a2+"\t\t"+a3+"\n");
			}

			eWriter.write("\nP under K and Ki\nRound:\tDES0\tDES1\tDES2\tDES3\n");
			eWriter.write("0\t\t1\t\t1\t\t1\t\t1\n");
			for(int i=0; i<16; i++)
			{
				double avalanche0=1;
				double avalanche1=1;
				double avalanche2=1;
				double avalanche3=1;
				for(int o=0; o<56; o++) //for each round, check the average avalanche for each of Pi
				{
					avalanche0+=checkDifferences(desK0Normal.getRoundText(i), desK0[o].getRoundText(i));
					avalanche1+=checkDifferences(desK1Normal.getRoundText(i), desK1[o].getRoundText(i));
					avalanche2+=checkDifferences(desK2Normal.getRoundText(i), desK2[o].getRoundText(i));
					avalanche3+=checkDifferences(desK3Normal.getRoundText(i), desK3[o].getRoundText(i));
				}
				int a0=(int)Math.round(avalanche0/56.0); //take the average
				int a1=(int)Math.round(avalanche1/56.0); //take the average
				int a2=(int)Math.round(avalanche2/56.0); //take the average
				int a3=(int)Math.round(avalanche3/56.0); //take the average

				eWriter.write((i+1)+"\t\t"+a0+"\t\t"+a1+"\t\t"+a2+"\t\t"+a3+"\n");
			}
			eWriter.close();

			
		} catch (Exception e) {}	
	}
	/**
	 * Calculates the avalanche effect for each of the, 
	 * @return
	 */
	private String avalanche(){
		return "";
	}
	
	/**
	 * @param text
	 * @return
	 */
	private static String[] bitDifferenceArray(String text){
		String[] bitDifference = new String[64];
		for(int i=0; i<text.length(); i++){
			bitDifference[i] = text;
			if(bitDifference[i].charAt(i)=='0'){
				bitDifference[i]=bitDifference[i].substring(0,i)+"1"+bitDifference[i].substring(i+1);
			}
			else{
				bitDifference[i]=bitDifference[i].substring(0,i)+"0"+bitDifference[i].substring(i+1);
			}
		}
		return bitDifference;
	}
	
	/**
	 * @param scanner 
	 * 
	 */
	private static void decryptFile(String ciphertext, String key){
		try{
		    PrintWriter dWriter = new PrintWriter("DECRYPTIONOUTPUT.txt", "UTF-8");
			//DECRYPTION, read first line as ciphertext, second line as key
			DES des0Decrypt=new DES("DES0", new Round(new int[]{0, 1, 2, 3}), "", key, ciphertext); //version, round, plaintext, key, ciphertext
			des0Decrypt.decrypt();		
			dWriter.write("DECRYPTION\n");
			dWriter.write("Ciphertext C:" + des0Decrypt.getCiphertext()+"\n");
			dWriter.write("Key K:" + key+"\n");
			dWriter.write("Plaintext P:"+ des0Decrypt.getPlaintext()+"\n");
			dWriter.close();
		}
		catch(Exception ex){
			
		}
	}
	
	/**
	 * @param a
	 * @param b
	 * @return
	 */
	private static int checkDifferences(String a, String b)
	{
		int differenceCount=0;

		for(int i=0; i<a.length(); i++)
		{
			if(i<b.length())
			{
				if(a.charAt(i)!=b.charAt(i))
				{
					differenceCount++;
				}
			}
		}

		return differenceCount;
	}
}
