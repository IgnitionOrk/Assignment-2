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
	
	/* @param args: Contains the arguments used for the input and output files. */
	public static void main(String[] args) {	
		try{
			// Name of the file containing the text, and key. 
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
	 * @throws UnsupportedEncodingException*/
	private static void decryption(String ciphertext, String key, String outputFilename) throws FileNotFoundException, UnsupportedEncodingException{
		DES des = new DES(DES.Version.DES0); //DES0 is the original DES algorithm
		des.initializeCipher(DES.DESMode.DECRYPT, key); //initialise to decrypt and use the supplied 56bit key
		des.begin(ciphertext);
		
		
		// Output the desired information for decryption. 
	    PrintWriter dWriter = new PrintWriter(outputFilename, "UTF-8");
		dWriter.write("DECRYPTION"+System.lineSeparator());
		dWriter.write("Ciphertext C:" + des.ciphertext()+System.lineSeparator());
		dWriter.write("Key K:" + key+System.lineSeparator());
		dWriter.write("Plaintext P:"+ des.plaintext()+System.lineSeparator());	
		dWriter.close();
	}
	
	/* @param plaintext
	 * @param key
	 * @param output
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException */
	private static void encryption(String plaintext, String key, String outputFilename) throws FileNotFoundException, UnsupportedEncodingException{
		// No difference in either plaintext P or key K. 
		DES[] desVersions = new DES[DES.Version.values().length]; //4 DES versions, the original and a few different modified versions
		for(int i = 0; i < DES.Version.values().length; i++){
			desVersions[i] = new DES(DES.Version.values()[i]); //Create a DES object for each version
			// Encrypt P under K;
			desVersions[i].initializeCipher(DES.DESMode.ENCRYPT, key);
			desVersions[i].begin(plaintext);
		}
		
		String p[] = bitDifferenceArray(desVersions[0].plaintext()); // An array of plaintexts differing by 1-bit.
		String k[] = bitDifferenceArray(key);	// An array of keys differing by 1-bit.
		
		// Plain texts with a difference of 1-bit. Pi under K;
		DES[][] desPDB = differentPlaintextsUnderKeyK(desVersions[0].plaintext().length(), key, p); 

		// Encrypting the plaintext P under different keys (by 1-bit).
		DES[][] desKBD = plaintextUnderDifferentKeys(key.length(), plaintext, k);
	
		// Output the desired information for encryption. 
	    PrintWriter eWriter = new PrintWriter(outputFilename, "UTF-8");
	    eWriter.write("ENCRYPTION"+System.lineSeparator());
	    eWriter.write("Plaintext P:" + desVersions[0].getRoundText(0)+System.lineSeparator());
	    eWriter.write("Key K:" + key+System.lineSeparator());
	    eWriter.write("Ciphertext C:"+ desVersions[0].ciphertext()+System.lineSeparator());
	    eWriter.write("Avalanche:"+System.lineSeparator());
		eWriter.write("P and Pi under K"+System.lineSeparator());
		eWriter.write("Round:\tDES0\tDES1\tDES2\tDES3"+System.lineSeparator());
	    avalanche(plaintext.length(), desVersions, desPDB, eWriter);
	    eWriter.write(System.lineSeparator());
		eWriter.write("P under K and Ki"+System.lineSeparator());
		eWriter.write("Round:\tDES0\tDES1\tDES2\tDES3"+System.lineSeparator());
	    avalanche(key.length(), desVersions, desKBD, eWriter);
		eWriter.close();

	}

	/* @param length
	 * @param key
	 * @param iPlaintexts
	 * @return */
	private static DES[][] differentPlaintextsUnderKeyK(int length, String key, String[] iPlaintexts){
		DES[][] tempDES = new DES[DES.Version.values().length][length];
		for(int x = 0; x < DES.Version.values().length; x++){ // For each DES algorithm...
			for(int y = 0; y < length; y++){
				tempDES[x][y] = new DES(DES.Version.values()[x]);
				tempDES[x][y].initializeCipher(DES.DESMode.ENCRYPT, key);
				tempDES[x][y].begin(iPlaintexts[y]); // Encrypt each plaintext under key K
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
		for(int x = 0; x < DES.Version.values().length; x++){
			for(int y = 0; y < length; y++){
				tempDES[x][y] = new DES(DES.Version.values()[x]);
				tempDES[x][y].initializeCipher(DES.DESMode.ENCRYPT, iKeys[y]);
				tempDES[x][y].begin(plaintext);
			}
		}
		return tempDES;
	}
	
	/* @param noOfBits
	 * @param desVersions
	 * @param desWith1BitDifference
	 * @param writer */
	private static void avalanche(int noOfBits, DES[] desVersions, DES[][] desWith1BitDifference, PrintWriter writer){
		// 16 rounds, however round 0, is based against the plaintext, that hasn't gone through the rounds. 
		double[][] avgAvalanche = new double[DES.Version.values().length][desVersions[0].NUMBEROFROUNDS + 1];
		for(int round = 0; round < desVersions[0].NUMBEROFROUNDS + 1; round++){
			for(int version = 0 ; version < DES.Version.values().length; version++){
				for(int y = 0; y < noOfBits; y++){
					avgAvalanche[version][round] += checkDifferences(desVersions[version].getRoundText(round), desWith1BitDifference[version][y].getRoundText(round));
				}		
				avgAvalanche[version][round] = Math.round(avgAvalanche[version][round] / noOfBits);
			}
		}
		writeToFile(desVersions[0].NUMBEROFROUNDS, avgAvalanche, writer);
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
			writer.write(System.lineSeparator());
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
