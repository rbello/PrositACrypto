package fr.exia.a4.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static char bits2byte(boolean block[]) {
		String result = "";
		for (int i = 0; i < block.length; i++) {
			result += bin2str(block[i]);
		}
		return binstr2char(result);
	}

	public static boolean[] switchBits(boolean[] input) {
		boolean[] switched = new boolean[8];
		int index = 0;
		for (int i = 4; index < input.length; i++) {
			switched[index++] = input[i % input.length];
		}
		return switched;
	}

	public static char binstr2char(String str) {
		char ret = 0;
		int v = 0;
		int p = 0;
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.charAt(i) == '1')
				v += Math.pow(2, p);
			p++;
		}
		ret = (char) v;
		return ret;
	}

	public static int[] toCharArray(String str) {
		// On en fait un tableau, et on ordonne les caractères par rapport à
		// leurs positions dans l'alphabet
		int[] ret = new int[str.length()];
		int i = 0;
		for (char c : str.toCharArray()) {
			ret[i++] = c - 97;
		}
		return ret;
	}

	public static String bin2str(boolean value) {
		return value ? "1" : "0";
	}

	public static boolean[] xor(boolean[] a, boolean[] b) {
		// On fabrique un tableau de la taille maximale de deux tableaux a ou b
		// Initialement, toutes les valeurs valent FALSE
		boolean[] result = new boolean[Math.max(a.length, b.length)];

		// On parcours les deux tableaux jusqu'� la fin du plus petit
		for (int i = 0, min = Math.min(a.length, b.length); i < min; i++) {
			result[i] = ((a[i] && b[i]) || (!a[i] && !b[i])) ? false : true;
		}
		return result;
	}

	public static boolean[] char2bin(String key) {
		boolean[] output = new boolean[key.length()];
		for (int i = 0; i < key.length(); i++) {
			output[i] = char2bin(key.charAt(i));
		}
		return output;
	}

	public static boolean char2bin(char bit) {
		if (bit == '0')
			return false;
		else if (bit == '1')
			return true;
		else
			throw new RuntimeException("Key must be in binary string format [0,1]");
	}

	public static boolean[] circularLeftShift(boolean[] key, int bits) {
		boolean[] shifted = new boolean[key.length];
		for (int index = 0, i = bits; index < key.length; i++) {
			shifted[index++] = key[i % key.length];
		}
		return shifted;
	}

	/**
	 * Renvoie un tableau de booleans (assimilables � des bits) correspondant
	 * aux valeurs des bits de l'octet pass� en param�tre.
	 * 
	 * @param block
	 * @return
	 */
	public static boolean[] byte2bits(char block) {
		boolean result[] = new boolean[8];
		int c = block;
		// On parcours les 8 bits en partant des bits de poids forts (la gauche)
		for (int p = 7, i = 0; p >= 0; p--, i++) {
			// Si le bit actuel est � 1
			if (c - Math.pow(2, p) >= 0) {
				result[i] = true;
				// On d�calle au bit pr�c�dent (toujours en partant de la
				// gauche)
				c -= Math.pow(2, p);
			} else
				result[i] = false;
		}
		return result;
	}

	/**
	 * Renvoie une cha�ne de caract�re compos�e de 0 et de 1 correspondant aux
	 * valeurs des bits de l'octet pass� en param�tre.
	 * 
	 * @param block
	 * @return
	 */
	public static String char2binstr(char block) {
		String ret = "";
		int c = block;
		// On parcours les 8 bits en partant des bits de poids forts (la gauche)
		for (int p = 7; p >= 0; p--) {
			// Si le bit actuel est � 1
			if (c - Math.pow(2, p) >= 0) {
				ret += "1";
				// On d�calle au bit pr�c�dent (toujours en partant de la
				// gauche)
				c -= Math.pow(2, p);
			} else
				ret += "0";
		}
		return ret;
	}

	public static List<boolean[]> splitBlock(boolean[] block) {

		// On pr�pare deux tableaux
		List<boolean[]> splited = new ArrayList<>(2);
		splited.add(new boolean[block.length / 2]);
		splited.add(new boolean[block.length / 2]);

		// On split en deux
		int index = 0;
		for (int i = 0, middle = block.length / 2; i < middle; i++) {
			splited.get(0)[i] = block[index++];
		}
		for (int i = 0, middle = block.length / 2; i < middle; i++) {
			splited.get(1)[i] = block[index++];
		}
		return splited;
	}

	public static String readAllFile(String path, String charset) {
		try {
			return new String(Files.readAllBytes(Paths.get("file")), StandardCharsets.UTF_8);
//			File file = new File(path);
//			FileInputStream fis = new FileInputStream(file);
//			byte[] data = new byte[(int) file.length()];
//			fis.read(data);
//			fis.close();
//			return new String(data, charset);
		}
		catch (Throwable t) {
			return null;
		}
	}
	
	public static String readAllLocalFile(String path) {
		try {
			return readAllFile(Utils.class.getResource("../../../../" + path).toURI());
		}
		catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static String readAllFile(URI path) {
		try {
			return new String(Files.readAllBytes(Paths.get(path)));
		}
		catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
}
