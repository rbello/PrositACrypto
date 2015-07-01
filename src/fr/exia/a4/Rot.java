package fr.exia.a4;

public class Rot {
	
	static final char[] ALPHABET = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
		'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
		'v', 'w', 'x', 'y', 'z' };
	
	public static String cesar_crypter(String text) {
		return crypter(3, text);
	}
	
	public static String cesar_decrypter(String text) {
		return decrypter(3, text);
	}
	
	public static String crypter(int n, String text) {
		// On est obligé de ramener les caractères dans l'alphabet
		text = text.toLowerCase();

		// On convertit le string en un tableau de char
		char[] charSIn = text.toCharArray();
		
		// Variable de sortie
		String out = "";
		
		// On prend chaque lettre du texte d'entrée  
		for (int i = 0; i < charSIn.length; i++) {
			
			// On prend sa position actuelle dans l'alphabet
			int pos1 = ordInAlphabet(charSIn[i]);
			// On calcule sa nouvelle position en fonction de n
			int pos2 = newPos(pos1, n);
			// Si -1, c'est que ce n'est pas une lettre, on laisse le même caractère
			if (pos2 == -1)
				out += charSIn[i];
			else
				out += ALPHABET[pos2];
		}
		
		return out;
	}

	public static String decrypter(int n, String text) {
		return crypter(-n, text);
	}

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

}
