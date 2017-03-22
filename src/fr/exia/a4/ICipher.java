package fr.exia.a4;

public interface ICipher<K, C> {

	C encryption(String data, K key);
	
	String decryption(C data, K key);
	
}
