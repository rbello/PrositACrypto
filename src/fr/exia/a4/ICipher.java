package fr.exia.a4;

/**
 * 
 * @author rbello
 *
 * @param <K> Type of the key
 * @param <C> Type of ciphered data
 */
public interface ICipher<K, C> {

	C encryption(String data, K key);
	
	String decryption(C data, K key);
	
}
