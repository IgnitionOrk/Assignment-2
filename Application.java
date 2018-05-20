/* Student name: Student Number:
 * 	1) Ryan Cunneen: 3179234
 * 	2) Jonathan Low: 3279624
 * 
 * This program will encrypt and decrypt a 64 bit message using a 56 bit key using a hand-coded version of the DES algorithm.
 * It contains the original DES algorithm, as well as three slightly modified versions to demonstrate the importance of each part.
 * It will also encrypt each message P under every possible Ki where Ki differs from key K by 1 bit.
 * As well as encrypt each message Pi under key K, where Pi is every possible message that differs from P by 1 bit.
 * Finally it will check the average difference between the text generated at the end of each of the 16 rounds for Pi
 * and that of message P (and the same for Ki and K). It does this for each algorithm DES0/1/2/3 and demonstrates how much a small
 * change to the algorithm affects the security of the cipher. */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Application {
	
	/* @param args: Contains the arguments used for the input and output files */
	public static void main(String[] args) {	
		// Name of the file containing the text, and key. 
		try{
			Scanner scanner = new Scanner(new File(args[0]));
				
			// Name of the file the data will be saved to. 
			String output = args[1];	

			String bit = scanner.nextLine();
			String text = scanner.nextLine();
			String key = scanner.nextLine();
			
			if(!output.contains(".txt")){
				output += ".txt";
			}
			
			if(bit.equals("0")){
				encryption(text, key, output);
			}else{
				decryption(text, key, output);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/* @param ciphertext
	 * @param key
	 * @param output
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException */
	private static void decryption(String ciphertext, String key, String output) throws FileNotFoundException, UnsupportedEncodingException{
	    PrintWriter dWriter = new PrintWriter(output, "UTF-8");
		DES des = new DES(DES.Version.DES0); //DES0 is the original DES algorithm

		des.initializeCipher(DES.DESMode.DECRYPT, key); //initialise to decrypt and use the supplied 56bit key
		des.begin(ciphertext);
		
		dWriter.write("DECRYPTION\n");
		dWriter.write("Ciphertext C:" + des.getCiphertext()+"\n");
		dWriter.write("Key K:" + key+"\n");
		dWriter.write("Plaintext P:"+ des.getPlaintext()+"\n");	
		dWriter.close();
	}
	
	/* @param plaintext
	 * @param key
	 * @param output
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException */
	private static void encryption(String plaintext, String key, String output) throws FileNotFoundException, UnsupportedEncodingException
	{
		DES[][] desKi, desPi; //DES P under each Ki and K under each Pi
		DES[] desPK;
		String[] p, k;
		PrintWriter eWriter;

		// No difference in either plaintext P or key K. 
		desPK = new DES[DES.Version.values().length]; //4 DES versions, the original and a few different modified versions
		
		for(int i = 0; i < DES.Version.values().length; i++){
			desPK[i] = new DES(DES.Version.values()[i]); //Create a DES object for each version
			
			// Encrypt P under K;
			desPK[i].initializeCipher(DES.DESMode.ENCRYPT, key);
			desPK[i].begin(plaintext);
		}

		
		// Arrays of plaintexts and keys differing by 1-bit.
		p = bitDifferenceArray(plaintext);
		k = bitDifferenceArray(key);
		
		// DES Plain texts with a difference of 1-bit. Pi under K and plaintext P under different keys (by 1-bit).
		desKi = differentPlaintextsUnderKeyK(plaintext.length(), key, p); 
		desPi = plaintextUnderDifferentKeys(key.length(), plaintext, k);

		eWriter = new PrintWriter(output, "UTF-8");

	    eWriter.write("ENCRYPTION\n");
	    eWriter.write("Plaintext P:" + plaintext+"\n");
	    eWriter.write("Key K:" + key+"\t\n");
	    eWriter.write("Ciphertext C:"+ desPK[0].getCiphertext()+"\n");

		eWriter.write("\nAvalanche:\nP and Pi under K\nRound:\tDES0\tDES1\tDES2\tDES3\n");
	    avalanche(plaintext.length(), desPK, desKi, eWriter);
		eWriter.write("\nP under K and Ki\nRound:\tDES0\tDES1\tDES2\tDES3\n");
	    avalanche(key.length(), desPK, desPi, eWriter);

		eWriter.close();

	}

	/* @param length
	 * @param key
	 * @param iPlaintexts
	 * @return */
	private static DES[][] differentPlaintextsUnderKeyK(int length, String key, String[] iPlaintexts){
		DES[][] tempDES = new DES[DES.Version.values().length][length];

		for(int version = 0; version < DES.Version.values().length; version++){ // For each DES algorithm...
			for(int bit = 0; bit < length; bit++){
				tempDES[version][bit] = new DES(DES.Version.values()[version]);
				tempDES[version][bit].initializeCipher(DES.DESMode.ENCRYPT, key);
				tempDES[version][bit].begin(iPlaintexts[bit]); // Encrypt each plaintext under key K
			}	
		}

		return tempDES;
	}
	
	/* @param length
	 * @param plaintext
	 * @param iKeys
	 * @return */
	private static DES[][] plaintextUnderDifferentKeys(int length, String plaintext, String[] iKeys){
		DES[][] tempDES = new DES[DES.Version.values().length][length];

		for(int version = 0; version < DES.Version.values().length; version++){
			for(int bit = 0; bit < length; bit++){
				tempDES[version][bit] = new DES(DES.Version.values()[version]);
				tempDES[version][bit].initializeCipher(DES.DESMode.ENCRYPT, iKeys[bit]);
				tempDES[version][bit].begin(plaintext);
			}
		}

		return tempDES;
	}
	
	/* @param noOfBits
	 * @param desPK
	 * @param desWith1BitDifference
	 * @param writer */
	private static void avalanche(int noOfBits, DES[] desPK, DES[][] desWith1BitDifference, PrintWriter writer){
		// 16 rounds, however round 0, is based against the plaintext, that hasn't gone through the rounds. 
		double[][] avgAvalanche = new double[DES.Version.values().length][desPK[0].NUMBEROFROUNDS + 1];

		for(int round = 0; round < desPK[0].NUMBEROFROUNDS + 1; round++){
			for(int version = 0; version < DES.Version.values().length; version++){
				for(int bit = 0; bit < noOfBits; bit++){
					avgAvalanche[version][round] += checkDifferences(desPK[version].getRoundText(round), desWith1BitDifference[version][bit].getRoundText(round));
				}

				avgAvalanche[version][round] = Math.round(avgAvalanche[version][round] / noOfBits);
			}
		}

		writeToFile(desPK[0].NUMBEROFROUNDS, avgAvalanche, writer);
	}
	
	/* @param rounds
	 * @param data
	 * @param writer */
	private static void writeToFile(int rounds, double [][] data, PrintWriter writer){
		for(int i = 0; i < rounds + 1; i++){
			writer.write(""+i);
			
			for(int y = 0; y < DES.Version.values().length; y++){
				writer.write("\t\t"+(int)data[y][i]);
			}

			writer.write("\n");
		}
	}
	
	/* @param text: 
	 * @return: An array, with each String instance all differing in 1-bit. */
	private static String[] bitDifferenceArray(String text){
		String[] bitDifference = new String[text.length()];

		for(int i=0; i < text.length(); i++){ //for each character in String text
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

	/* @param a
	 * @param b
	 * @return */
	private static int checkDifferences(String a, String b){
		int differenceCount=0;

		for(int i=0; i<a.length(); i++){
			if(i<b.length()){
				if(a.charAt(i)!=b.charAt(i)){
					differenceCount++;
				}
			}
		}

		return differenceCount;
	}
}
