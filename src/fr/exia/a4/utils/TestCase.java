package fr.exia.a4.utils;

import fr.exia.a4.ROT;
import fr.exia.a4.SDES_BitAsBoolean;
import vecmath.Matrix3f;

public class TestCase {

	public static <K, C> boolean test(ICipher<K, C> cipher, String clearData, K key) {
		return test(cipher, clearData, key, null);
	}
	
	public static <K, C> boolean test(ICipher<K, C> cipher, String clearData, K key, C cipherData) {
		
		// Encypt data
		C enc = cipher.encryption(clearData, key);
		
		// Check if encrypted data matches given data
		if (cipherData != null) {
			System.out.println("Test '" + cipher.getClass().getSimpleName() + "' with key '" + key + "' to encode : " 
					+ (cipherData.equals(enc) ? "SUCCESS" : "FAILURE"));
		}
		
		// Check if clearData is equals to decrypted data
		boolean result = clearData.equals(cipher.decryption(enc, key));
		System.out.println("Test '" + cipher.getClass().getSimpleName() + "' with key '" + key + "' to decode : " 
				+ (result ? "SUCCESS" : "FAILURE"));
		
		return result;
		
	}
	
	public static void main(String[] args) {
		
		// ROT
		test(new ROT(), "abcd", 3, "defg");
		test(new ROT(), "abcd", 13, "nopq");
		
		// HILL
		Matrix3f matKey = new Matrix3f(
			-3, -3, -4,
		     0,  1,  1,
			 3,  3,  3
		);
		//test(new HillCipher3x3(), "abcd", matKey, "LDJCYD");
		
		// S-DES
		String data = Utils.readAllLocalFile("/fr/exia/a4/data/messageintercepte.txt");
		System.out.println(data);
		test(new SDES_BitAsBoolean(), data, "1100101101");

		System.out.println(new SDES_BitAsBoolean().decryption(data, "1100101101"));
		
	}
	
}
