package fr.exia.a4;

import fr.exia.a4.utils.ICipher;

public class ROT implements ICipher<Integer, String> {
	
	static final char[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
		'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
		'v', 'w', 'x', 'y', 'z' };
	
	/**
	 * Renvoie la position du caractere dans le tableau Et -1 si il n'est pas
	 * dans le tableau
	 */
	public static final int posChar(char c, char[] tab) {
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] == c)
				return i;
		}
		return -1;
	}
	
	public static final int ordInAlphabet(char c) {
		return posChar(c, ALPHABET);
	}

	/**
	 * Donne la nouvelle position dans l'alphabet en fonction de n
	 */
	public static final int newPos(int pos, int n) {
		
		if (pos <= -1) {
			return -1;
		}

		int pos2 = pos;
		int i = 0;
		while (i < Math.abs(n)) {
			if (n < 0) {
				if (pos2 - 1 == -1)
					pos2 = 25;
				else
					pos2--;
			} else {
				if (pos2 + 1 >= 25)
					pos2 = 0;
				else
					pos2++;
			}
			i++;
		}
		return pos2;
	}

	@Override
	public String encryption(String data, Integer key) {
		char[] charSIn = data.toLowerCase().toCharArray();
		String out = "";
		for (int i = 0; i < charSIn.length; i++) {
			// Get char position in the alphabet
			int pos1 = ordInAlphabet(charSIn[i]);
			// 
			int pos2 = newPos(pos1, key);
			// Not a chat
			if (pos2 == -1)
				out += charSIn[i];
			else
				out += ALPHABET[pos2];
		}
		
		return out;
	}

	@Override
	public String decryption(String data, Integer key) {
		return encryption(data, -key);
	}

}
