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
	private static final String OUTPUTTXT = "output.txt";	

	public static void main(String[] args) throws FileNotFoundException {
		//load and read the file
		Scanner scanner = new Scanner(new File(args[0]));
		String bit = scanner.nextLine();
		String text = scanner.nextLine();
		String key = scanner.nextLine();
		
		//use the first line to determine if we're encrypting or decrypting
		try{
			if(bit.equals("0")){
				encryption(text, key);
			}
			else{
				decryption(text, key);
			}
		}

		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * @param scanner 
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	private static void decryption(String ciphertext, String key) throws FileNotFoundException, UnsupportedEncodingException{
	    PrintWriter dWriter = new PrintWriter(OUTPUTTXT, "UTF-8");
		
		DES des = new DES(DES.Version.DES0); //DES0 is the original DES algorithm
		des.initializeCipher(DES.DESMode.DECRYPT, key); //initialise to decrypt and use the supplied 56bit key
		des.begin(ciphertext); //decrypt our ciphertext
		
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
		DES[] desVersions = new DES[NUMBEROFVERSIONS]; //4 DES versions, the original and a few different modified versions
		for(int i = 0; i < NUMBEROFVERSIONS; i++){
			desVersions[i] = new DES(DES.Version.values()[i]); //Create a DES object for each version
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
	
	    PrintWriter eWriter = new PrintWriter(OUTPUTTXT, "UTF-8");
	    eWriter.write("ENCRYPTION\n");
	    eWriter.write("Plaintext P:" + plaintext+"\n");
	    eWriter.write("Key K:" + key+"\t\n");
	    eWriter.write("Ciphertext C:"+ desVersions[0].getCiphertext()+"\n");

		eWriter.write("\nAvalanche:\nP and Pi under K\nRound:\tDES0\tDES1\tDES2\tDES3\n");
	    avalanche(plaintext.length(), desVersions, desPDB, eWriter, plaintext);
		eWriter.write("\nP under K and Ki\nRound:\tDES0\tDES1\tDES2\tDES3\n");
<<<<<<< HEAD
	    avalanche(key.length(), desVersions, desKBD, eWriter, plaintext);
=======
	    avalanche(desVersions, desKBD, eWriter, plaintext);

>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
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

		for(int x = 0; x < NUMBEROFVERSIONS; x++){ // For each DES algorithm...
			for(int y = 0; y < length; y++){
				tempDES[x][y] = new DES(DES.Version.values()[x]);
				tempDES[x][y].initializeCipher(DES.DESMode.ENCRYPT, key);
				tempDES[x][y].begin(iPlaintexts[y]); // Encrypt each plaintext under key K
			}	
		}
<<<<<<< HEAD
=======
		
>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
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
				tempDES[x][y] = new DES(DES.Version.values()[x]);
				tempDES[x][y].initializeCipher(DES.DESMode.ENCRYPT, iKeys[y]);
				tempDES[x][y].begin(plaintext);
			}
		}
<<<<<<< HEAD
=======
		
>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
		return tempDES;
	}

	/**
<<<<<<< HEAD
	 * Calculates the avalanche effect for each of the, 
	 * @param noOfBits 
	 * @return
	 */
	private static void avalanche(int noOfBits, DES[] desVersions, DES[][] desWith1BitDifference, PrintWriter writer, String text){
		int avalanche0=0;
		int avalanche1=0;
		int avalanche2=0;
		int avalanche3=0;
		
		int[] avgAvalanche = new int[NUMBEROFVERSIONS];
		
		for(int x = 0 ; x < NUMBEROFVERSIONS; x++){
			avgAvalanche[x] = average(desVersions[x].getPlaintext(), desWith1BitDifference[x]);
		}
		
		String line = "0";
		for(int x = 0; x < NUMBEROFVERSIONS; x++){
			line += "\t\t"+avgAvalanche[x];
		}
		
		line += "\n";
		// Save the average no. of bits for the round. 
		writer.write(line);
		
		for(int y = 1; y <= 16; y++){
			for(int x = 0 ; x < NUMBEROFVERSIONS; x++){
				avgAvalanche[x] = average(desVersions[x].getRoundText(x), desWith1BitDifference[x]);
			}
			
			for(int x = 0; x < NUMBEROFVERSIONS; x++){
				line += y+"\t\t"+avgAvalanche[x];
			}
			
			line += "\n";
			// Save the average no. of bits for the round. 
			writer.write(line);
			
			line = "";
		}
		// After sixteen rounds calculate the average no of bits for each round. 
		for(int i = 0; i <= 16; i++)
		{	
			if(i == 0){
				for(int x = 0 ; x < NUMBEROFVERSIONS; x++){
					avgAvalanche[x] = average(desVersions[x].getPlaintext(), desWith1BitDifference[x]);
				}
				avalanche0 = average(desVersions[0].getPlaintext(), desWith1BitDifference[0]);
				avalanche0 = average(desVersions[1].getPlaintext(), desWith1BitDifference[1]);
				avalanche0 = average(desVersions[2].getPlaintext(), desWith1BitDifference[2]);
				avalanche0 = average(desVersions[3].getPlaintext(), desWith1BitDifference[3]);

				// calculate the average no of bits for the round. 
				for(int o=0; o < noOfBits; o++)
=======
	 * Calculates the avalanche effect for each of the DES versions
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
				for(int o=0; o < desWith1BitDifference[0].length; o++) //for each round, check the average avalanche for each of Pi/Ki
>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
				{
					avalanche0+=checkDifferences(desVersions[0].getPlaintext(), desWith1BitDifference[0][o].getPlaintext());
					avalanche1+=checkDifferences(desVersions[1].getPlaintext(), desWith1BitDifference[1][o].getPlaintext());
					avalanche2+=checkDifferences(desVersions[2].getPlaintext(), desWith1BitDifference[2][o].getPlaintext());
					avalanche3+=checkDifferences(desVersions[3].getPlaintext(), desWith1BitDifference[3][o].getPlaintext());
				}
			}
			else{
				avalanche0=1;
				avalanche1=1;
				avalanche2=1;
				avalanche3=1;
<<<<<<< HEAD
				// calculate the average no of bits for the round. 
				for(int o=0; o < noOfBits; o++)
=======

				for(int o=0; o < desWith1BitDifference[0].length; o++) //for each round, check the average avalanche for each of Pi/Ki
>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
				{
					avalanche0+=checkDifferences(desVersions[0].getRoundText(i - 1), desWith1BitDifference[0][o].getRoundText(i - 1));
					avalanche1+=checkDifferences(desVersions[1].getRoundText(i - 1), desWith1BitDifference[1][o].getRoundText(i - 1));
					avalanche2+=checkDifferences(desVersions[2].getRoundText(i - 1), desWith1BitDifference[2][o].getRoundText(i - 1));
					avalanche3+=checkDifferences(desVersions[3].getRoundText(i - 1), desWith1BitDifference[3][o].getRoundText(i - 1));
				}
			}
			
<<<<<<< HEAD
			int a0=(int)Math.round(avalanche0/noOfBits); //take the average
			int a1=(int)Math.round(avalanche1/noOfBits); //take the average
			int a2=(int)Math.round(avalanche2/noOfBits); //take the average
			int a3=(int)Math.round(avalanche3/noOfBits); //take the average
			
		}
	}
	private static int average(String text, DES[] desWith1BitDifference){
		int count = 0;
		// calculate the average no of bits for the round. 
		for(int o=0; o < desWith1BitDifference.length; o++){
			count+=checkDifferences(text, desWith1BitDifference[o].getPlaintext());
=======
			int a0=(int)Math.round(avalanche0/desWith1BitDifference[0].length); //take the average
			int a1=(int)Math.round(avalanche1/desWith1BitDifference[0].length); //take the average
			int a2=(int)Math.round(avalanche2/desWith1BitDifference[0].length); //take the average
			int a3=(int)Math.round(avalanche3/desWith1BitDifference[0].length); //take the average

			writer.write((i)+"\t\t"+a0+"\t\t"+a1+"\t\t"+a2+"\t\t"+a3+"\n");
>>>>>>> 4dfc239164089a81029666a79b4761b8bdd29130
		}
		return count / desWith1BitDifference.length;
	}
	
	/**
	 * @param text: 
	 * @return: An array, with each String instance all differing in 1-bit. 
	 */
	private static String[] bitDifferenceArray(String text){
		String[] bitDifference = new String[64];

		for(int i=0; i<text.length(); i++){ //for each character in String text
			bitDifference[i] = text; //create a string that matches it

			//then invert a single bit at index i
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

		for(int i=0; i<a.length(); i++) // Loop through the strings
		{
			if(i<b.length())
			{
				if(a.charAt(i)!=b.charAt(i)) //increase the different count for every bit that doesn't match
				{
					differenceCount++;
				}
			}
		}

		return differenceCount;
	}
}
