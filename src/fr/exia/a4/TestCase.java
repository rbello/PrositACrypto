package fr.exia.a4;

import vecmath.Matrix3f;

public class TestCase {

	public static <K, C> boolean test(ICipher<K, C> cipher, String clearData, K key) {
		// Encypt data
		C cipherData = cipher.encryption(clearData, key);
		// Check if clearData is equals to decrypted data
		boolean result = clearData.equals(cipher.decryption(cipherData, key));
		System.out.println("Test '" + cipher.getClass().getSimpleName() + "' with key '" + key + "' : " 
				+ (result ? "SUCCESS" : "FAILURE"));
		return result;
	}
	
	public static void main(String[] args) {
		test(new ROT(), "abcd", 3);
		test(new ROT(), "abcd", 13);
		Matrix3f matKey = new Matrix3f(
			-3, -3, -4,
		     0,  1,  1,
			 3,  3,  3
		);
		test(new HillCipher(), "abcd", matKey);
	}
	
}
