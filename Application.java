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
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
public class Application {
	private static final int NUMBEROFVERSIONS = 4;
	private static Version[] versions = new Version[NUMBEROFVERSIONS];
	
	public static void main(String[] args) throws FileNotFoundException {
		versions();
		Scanner scanner = new Scanner(new File(args[0]));
		String text = scanner.nextLine();
		String key = scanner.nextLine();
		try{
			if(args[0].toLowerCase().contains("decryption")){
				decryption(text, key);
			}
			else{
				encryption(text, key);
			}
			
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * 
	 */
	private static void versions(){
		versions[0] = new Version("DES0", new int[]{0, 1} );
		versions[1] = new Version("DES1", new int[]{0} );
		versions[2] = new Version("DES2", new int[]{2, 1} );
		versions[3] = new Version("DES3", new int[]{2} );
	}
	/**
	 * @param i
	 * @return
	 */
	private static Version Versions(int i){
		return versions[i];
	}
	/**
	 * @param scanner 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 * 
	 */
	private static void decryption(String ciphertext, String key) throws FileNotFoundException, UnsupportedEncodingException{
	    PrintWriter dWriter = new PrintWriter("DECRYPTIONOUTPUT.txt", "UTF-8");
		DES des = new DES(Versions(0));
		des.initializeCipher(DES.DESMode.DECRYPT, key);
		des.begin(ciphertext);
		dWriter.write("DECRYPTION\n");
		dWriter.write("Ciphertext C:" + des.getCiphertext()+"\n");
		dWriter.write("Key K:" + key+"\n");
		dWriter.write("Plaintext P:"+ des.getPlaintext()+"\n");
		dWriter.close();
	}
	/**
	 * @param scanner 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 * 
	 */
	private static void encryption(String plaintext, String key) throws FileNotFoundException, UnsupportedEncodingException{
		// No difference in either plaintext P or key K. 
		DES[] desVersions = new DES[NUMBEROFVERSIONS];
		for(int i = 0; i < NUMBEROFVERSIONS; i++){
			desVersions[i] = new DES(Versions(i));
			// Encrypt P under K;
			desVersions[i].initializeCipher(DES.DESMode.ENCRYPT, key);
			desVersions[i].begin(plaintext);
		}
		
		String p[] = bitDifferenceArray(plaintext); // An array of plaintexts differing by 1-bit.
		String k[] = bitDifferenceArray(key);	// An array of keys differing by 1-bit.
		
		// Plain texts with a difference of 1-bit. Pi under K;
		DES[][] desPDB = differentPlaintextsUnderKeyK(plaintext.length(), key, p); 

		// Encrypting the plaintext P under different keys (by 1-bit).
		DES[][] desKBD = plaintextUnderDifferentKeys(key.length(), plaintext, k);
	
	    PrintWriter eWriter = new PrintWriter("ENCRYPTIONOUTPUT.txt", "UTF-8");
	    eWriter.write("ENCRYPTION\n");
	    eWriter.write("Plaintext P:" + plaintext+"\n");
	    eWriter.write("Key K:" + key+"\t\n");
	    eWriter.write("Ciphertext C:"+ desVersions[0].getCiphertext()+"\n");
		eWriter.write("\nAvalanche:\nP and Pi under K\nRound:\tDES0\tDES1\tDES2\tDES3\n");
	    avalanche(desVersions, desPDB, eWriter, plaintext);
		eWriter.write("\nP under K and Ki\nRound:\tDES0\tDES1\tDES2\tDES3\n");
	    avalanche(desVersions, desKBD, eWriter, plaintext);
		eWriter.close();

	}
	/**
	 * @param length
	 * @param key
	 * @param iPlaintexts
	 * @return
	 */
	private static DES[][] differentPlaintextsUnderKeyK(int length, String key, String[] iPlaintexts){
		DES[][] tempDES = new DES[NUMBEROFVERSIONS][length];
		for(int x = 0; x < NUMBEROFVERSIONS; x++){
			for(int y = 0; y < length; y++){
				tempDES[x][y] = new DES(Versions(x));
				tempDES[x][y].initializeCipher(DES.DESMode.ENCRYPT, key);
				tempDES[x][y].begin(iPlaintexts[y]);
			}	
		}
		
		//beginDES(tempDES, length);
		return tempDES;
	}
	/**
	 * @param length
	 * @param key
	 * @param iPlaintexts
	 * @return
	 */
	private static DES[][] plaintextUnderDifferentKeys(int length, String plaintext, String[] iKeys){
		DES[][] tempDES = new DES[NUMBEROFVERSIONS][length];
		for(int x = 0; x < NUMBEROFVERSIONS; x++){
			for(int y = 0; y < length; y++){
				tempDES[x][y] = new DES(Versions(x));
				tempDES[x][y].initializeCipher(DES.DESMode.ENCRYPT, iKeys[y]);
				tempDES[x][y].begin(plaintext);
			}
		}
		//beginDES(tempDES, length);
		return tempDES;
	}

	/**
	 * Calculates the avalanche effect for each of the, 
	 * @return
	 */
	private static String avalanche(DES[] desVersions, DES[][] desWith1BitDifference, PrintWriter writer, String text){
		double avalanche0=0;
		double avalanche1=0;
		double avalanche2=0;
		double avalanche3=0;
		for(int i=0; i <= 16; i++)
		{	
			if(i == 0){
				for(int o=0; o < desWith1BitDifference[0].length; o++) //for each round, check the average avalanche for each of Pi
				{
					avalanche0+=checkDifferences(text, desWith1BitDifference[0][o].getPlaintext());
					avalanche1+=checkDifferences(text, desWith1BitDifference[1][o].getPlaintext());
					avalanche2+=checkDifferences(text, desWith1BitDifference[2][o].getPlaintext());
					avalanche3+=checkDifferences(text, desWith1BitDifference[3][o].getPlaintext());
				}
			}
			else{
				avalanche0=1;
				avalanche1=1;
				avalanche2=1;
				avalanche3=1;
				for(int o=0; o < desWith1BitDifference[0].length; o++) //for each round, check the average avalanche for each of Pi
				{
					avalanche0+=checkDifferences(desVersions[0].getRoundText(i - 1), desWith1BitDifference[0][o].getRoundText(i - 1));
					avalanche1+=checkDifferences(desVersions[1].getRoundText(i - 1), desWith1BitDifference[1][o].getRoundText(i - 1));
					avalanche2+=checkDifferences(desVersions[2].getRoundText(i - 1), desWith1BitDifference[2][o].getRoundText(i - 1));
					avalanche3+=checkDifferences(desVersions[3].getRoundText(i - 1), desWith1BitDifference[3][o].getRoundText(i - 1));
				}
			}
			
			int a0=(int)Math.round(avalanche0/64.0); //take the average
			int a1=(int)Math.round(avalanche1/64.0); //take the average
			int a2=(int)Math.round(avalanche2/64.0); //take the average
			int a3=(int)Math.round(avalanche3/64.0); //take the average

			writer.write((i)+"\t\t"+a0+"\t\t"+a1+"\t\t"+a2+"\t\t"+a3+"\n");
		}
		return "";
	}
	
	/**
	 * @param text: 
	 * @return: An array, with each String instance all differing in 1-bit. 
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
