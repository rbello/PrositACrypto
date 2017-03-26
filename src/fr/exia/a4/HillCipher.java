package fr.exia.a4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.exia.a4.utils.ICipher;
import vecmath.Matrix3d;
import vecmath.Matrix3f;
    
/** 
 *  
 * This class is the implementation of encryption and decryption 
 *         using Hill cipher algorithm 
 *  
 *  https://github.com/jester155/HillCipher/blob/master/src/Cipher/HillCipher.java
 *  or with lib
 *  https://github.com/tstevens/Hill-Cipher/blob/master/Main.java
 */
public abstract class HillCipher<K, C> implements ICipher<K, C> {

	private static final char PADDING_CHAR = 'x';
	
	static int[][] decrypt = new int[3][1]; 
     static int[][] a = new int[3][3]; 
     static int[][] b = new int[3][3]; 
     static int[][] mes = new int[3][1]; 
     static int[][] res = new int[3][1]; 
     static BufferedReader br = new BufferedReader(new InputStreamReader( 
               System.in)); 
     static Scanner sc = new Scanner(System.in);
     
     static char[] ALPHABET = new char[26];
     
     
     /**
 	 * Initializes the English ASCII alphabet to an array.
 	 */
 	static {
 		for (int i = 0; i < 26; i++) {
 			ALPHABET[i] = (char) (97 + i);
 		}
 	}
     
	protected int N;
	
    public HillCipher(int n) {
    	 this.N = n;
	}

	public static void main(String[] args) throws IOException { 
          getKeys(); 
          for (int i = 0; i < 3; i++) 
               for (int j = 0; j < 1; j++) 
                    for (int k = 0; k < 3; k++) { 
                         res[i][j] = res[i][j] + a[i][k] * mes[k][j]; 
                    } 
          System.out.print("\nEncrypted string is : "); 
          for (int i = 0; i < 3; i++) { 
               // Hill Cipher actual Encryption logic modulo 26 
               System.out.print((char) (res[i][0] % 26 + 97)); 
               res[i][0] = res[i][0]; 
          } 
          getInverse(); 
          for (int i = 0; i < 3; i++) 
               for (int j = 0; j < 1; j++) 
                    for (int k = 0; k < 3; k++) { 
                         decrypt[i][j] = decrypt[i][j] + b[i][k] * res[k][j]; 
                    } 
          System.out.print("\nDecrypted string is : "); 
          for (int i = 0; i < 3; i++) { 
               // Hill cipher actual Decryption logic inverse the matrix and modulo 26 
               System.out.print((char) (decrypt[i][0] % 26 + 97)); 
          } 
          System.out.print("\n"); 
     }
     
	/** 
	 * This method actually takes the input from user or from console for encryption and decryption 
	 * @throws IOException Only one checked exception as input is being taken at runtime from console 
	 */ 
     public static void getKeys() throws IOException { 
          System.out.println("Enter 3x3 matrix for key (It should be inversible): "); 
          for (int i = 0; i < 3; i++) {
        	  for (int j = 0; j < 3; j++) {
        		  a[i][j] = sc.nextInt();
        	  }
          }
          System.out.print("\nEnter a 3 letter string: "); 
          String msg = br.readLine(); 
          for (int i = 0; i < 3; i++) 
               mes[i][0] = msg.charAt(i) - 97; 
     } 

     /** 
      * This method is called actually for decryption 
      * 
      * This inverse is according to Hill Ciphers decryption logic 
      */ 

     public static void getInverse() { 
          int p, q; 
          int[][] c = a; 
          for (int i = 0; i < 3; i++) 
               for (int j = 0; j < 3; j++) { 
                    // a[i][j]=sc.nextFloat(); 
                    if (i == j) 
                         b[i][j] = 1; 
                    else 
                         b[i][j] = 0; 
               } 
          for (int k = 0; k < 3; k++) { 
               for (int i = 0; i < 3; i++) { 
                    p = c[i][k]; 
                    q = c[k][k]; 
                    for (int j = 0; j < 3; j++) { 
                         if (i != k) { 
                              c[i][j] = c[i][j] * q - p * c[k][j]; 
                              b[i][j] = b[i][j] * q - p * b[k][j]; 
                         } 
                    } 
               } 
          } 
          for (int i = 0; i < 3; i++) 
               for (int j = 0; j < 3; j++) { 
                    b[i][j] = b[i][j] / c[i][i]; 
               } 
          System.out.println(""); 
          System.out.println("\nInverse Matrix is : "); 
          for (int i = 0; i < 3; i++) { 
               for (int j = 0; j < 3; j++) 
                    System.out.print(b[i][j] + " "); 
               System.out.print("\n"); 
          } 
     }
     
     /**
      * Fills the plain text string with x to match the matrix requirements
      */
     public String fillStringPadding(String data) {
    	 while (data.length() % N != 0)
    		 data += PADDING_CHAR;
    	 return data;
	}

     /**
      * TODO Doc
      * @param plainText
      * @return
      */
 	public static final String normalizeText(String plainText) {
		return Normalizer.normalize(plainText.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^a-zA-Z]", ""); 
	}
 	
 	/**
	 * Converts a character to a number (0-25)
	 * @param c
	 * @return A number 0-25
	 */
	public static int toNumber(char c) {
		for (int i = 0; i < ALPHABET.length; i++) {
			if (c == ALPHABET[i]) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Converts a number to a character with a modulo of 26. (a-z)
	 * @param nbr
	 * @return ASCII Character a-z
	 */
	public static char toCharacter(int nbr) {
		int x = nbr;
		int t = 0;
		if (nbr > 25) {
			x /= 26;
			x *= 26;
			t = nbr - x;
		}
		else {
			t = nbr;
		}
		return ALPHABET[t];
	}

	/*
	public final static List<Matrix3f> splitToMatrix3x3(String plainText) {
        int splitValue = 3;
        List<Matrix3f> chunks = new ArrayList<Matrix3f>();
        
            for (int i = 0, sub = splitValue; sub <= plainText.length(); i += splitValue, sub += splitValue) {
                char[] chunk = plainText.substring(i, sub).toCharArray();
                
                new matrix3f
                
                
                
                DoubleMatrix temp = stringToMatrix(chunk);
                chunks.add(temp);
//             TODO Parse non even length strings
//                if(i+splitValue > plainText.length()){
//                    if (splitValue == 3){
//                        if((plainText.length() - sub) == 2){
//                            char[] fill = {plainText.substring(sub, sub+1).toCharArray()[0], 'A', 'A'};
//                            temp = stringToMatrix(fill);
//                        } else {
//                            char[] fill = {plainText.substring(i, sub+2).toCharArray()[0], 'A'};
//                            temp = stringToMatrix(fill);
//                        }
//                        chunks.add(temp);
//                    } else {
//                        char[] fill = {plainText.substring(i, sub).toCharArray()[0], 'A'};
//                        temp = stringToMatrix(fill);
//                        chunks.add(temp);
//                    }
//                }
            }
            return chunks;
        } else {
            return null;
        }


}*/
} 
