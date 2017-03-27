package fr.exia.a4;

import java.util.ArrayList;
import java.util.List;

import fr.exia.a4.utils.ICipher;
import fr.exia.a4.utils.Utils;

public class SDES_BitAsBoolean implements ICipher<String, String> {

	private boolean[][][] s_box1 = new boolean[4][4][2];
	private boolean[][][] s_box2 = new boolean[4][4][2];

	public SDES_BitAsBoolean() {
		initSandbox();
	}

	private void initSandbox() {
		
		boolean b0[] = new boolean[2];
		b0[0] = false;
		b0[1] = false;

		boolean b1[] = new boolean[2];
		b0[0] = false;
		b0[1] = true;

		boolean b2[] = new boolean[2];
		b0[0] = true;
		b0[1] = false;

		boolean b3[] = new boolean[2];
		b0[0] = true;
		b0[1] = true;

		s_box1[0][0] = b1;
		s_box1[0][1] = b0;
		s_box1[0][2] = b3;
		s_box1[0][3] = b2;

		s_box1[1][0] = b3;
		s_box1[1][1] = b2;
		s_box1[1][2] = b1;
		s_box1[1][3] = b0;

		s_box1[2][0] = b0;
		s_box1[2][1] = b2;
		s_box1[2][2] = b1;
		s_box1[2][3] = b3;

		s_box1[3][0] = b3;
		s_box1[3][1] = b1;
		s_box1[3][2] = b3;
		s_box1[3][3] = b2;
		// ---------------------
		s_box2[0][0] = b0;
		s_box2[0][1] = b1;
		s_box2[0][2] = b2;
		s_box2[0][3] = b3;

		s_box2[1][0] = b2;
		s_box2[1][1] = b0;
		s_box2[1][2] = b1;
		s_box2[1][3] = b3;

		s_box2[2][0] = b3;
		s_box2[2][1] = b0;
		s_box2[2][2] = b1;
		s_box2[2][3] = b0;

		s_box2[3][0] = b2;
		s_box2[3][1] = b1;
		s_box2[3][2] = b0;
		s_box2[3][3] = b3;
		// ---------------------
	}

	@Override
	public String encryption(String data, String key) {

		// On convertit la chaîne de caractère de la clé en tableau de boolean
		// (des bits pour simplifier)
		boolean[] masterKey = Utils.char2bin(key);
		if (masterKey.length != 10) {
			throw new IllegalArgumentException("Key must have a length of 10 chars");
		}

		// Generate sub-keys
		List<boolean[]> keys = generateKeys(masterKey);
		boolean[] k1 = keys.get(0);
		boolean[] k2 = keys.get(1);

		// Fetch chars one by one
		StringBuilder ciphertext = new StringBuilder();
		for (int i = 0; i < data.length(); i++) {
			boolean[] b = Utils.byte2bits(data.charAt(i));
			// ciphertext = IP-1(FK(SW(FK(IP(plaintext)))))
			char c = (char) Utils.bits2byte(reverseIP(FK(Utils.switchBits(FK(IP(b), k1)), k2)));
			ciphertext.append(c);
		}

		// Return data as a string
		return ciphertext.toString();

	}
	
	@Override
	public String decryption(String data, String key) {
		// On convertit la chaîne de caractère de la clé en tableau de boolean
		// (des bits pour simplifier)
		boolean[] masterKey = Utils.char2bin(key);
		if (masterKey.length != 10) {
			throw new IllegalArgumentException("Key must have a length of 10 chars");
		}

		// Generate sub-keys
		List<boolean[]> keys = generateKeys(masterKey);
		boolean[] k1 = keys.get(0);
		boolean[] k2 = keys.get(1);

		// Fetch chars one by one
		StringBuilder ciphertext = new StringBuilder();
		for (int i = 0; i < data.length(); i++) {
			boolean[] b = Utils.byte2bits(data.charAt(i));
			// plaintext = IP-1(FK(SW(FK(IP(ciphertext)))))
			char c = (char) Utils.bits2byte(reverseIP(FK(Utils.switchBits(FK(IP(b), k2)), k1)));
			ciphertext.append(c);
		}

		// Return data as a string
		return ciphertext.toString();
	}

	public static List<boolean[]> generateKeys(boolean[] masterKey) {

		// Tableau qui va contenir K1 et K2
		List<boolean[]> keys = new ArrayList<>(2);

		// On applique P10 et on sépare en deux tableaux de 5 bits
		List<boolean[]> temp = Utils.splitBlock(P10(masterKey));

		// On applique P8
		keys.add(P8(Utils.circularLeftShift(temp.get(0), 1), Utils.circularLeftShift(temp.get(1), 1)));
		keys.add(P8(Utils.circularLeftShift(temp.get(0), 3), Utils.circularLeftShift(temp.get(1), 3)));

		return keys;
	}

	public boolean[] FK(boolean[] IP, boolean[] key) {
		List<boolean[]> temp = Utils.splitBlock(IP);
		boolean[] Left = Utils.xor(temp.get(0), F(temp.get(1), key));
		boolean[] joined = new boolean[8];
		int index = 0;
		for (int i = 0; i < 4; i++) {
			joined[index++] = Left[i];
		}
		for (int i = 0; i < 4; i++) {
			joined[index++] = temp.get(1)[i];
		}
		return joined;
	}

	public static boolean[] IP(boolean[] plainText) {
		// Indice dans key : 1 2 3 4 5 6 7 8
		// Permutation : 2 6 3 1 4 8 5 7
		boolean[] permutatedArray = new boolean[8];
		permutatedArray[0] = plainText[1];
		permutatedArray[1] = plainText[5];
		permutatedArray[2] = plainText[2];
		permutatedArray[3] = plainText[0];
		permutatedArray[4] = plainText[3];
		permutatedArray[5] = plainText[7];
		permutatedArray[6] = plainText[4];
		permutatedArray[7] = plainText[6];
		return permutatedArray;
	}

	public static boolean[] reverseIP(boolean[] permutedText) {
		// Indice dans key : 1 2 3 4 5 6 7 8
		// Permutation : 4 1 3 5 7 2 8 6
		boolean[] permutatedArray = new boolean[8];
		permutatedArray[0] = permutedText[3];
		permutatedArray[1] = permutedText[0];
		permutatedArray[2] = permutedText[2];
		permutatedArray[3] = permutedText[0];
		permutatedArray[4] = permutedText[3];
		permutatedArray[5] = permutedText[7];
		permutatedArray[6] = permutedText[4];
		permutatedArray[7] = permutedText[6];
		return permutatedArray;
	}

	public static boolean[] P10(boolean[] key) {
		// Indice dans key : 1 2 3 4 5 6 7 8 9 10
		// Permutation : 3 5 2 7 4 10 1 9 8 6
		boolean[] permutatedArray = new boolean[10];
		permutatedArray[0] = key[2];
		permutatedArray[1] = key[4];
		permutatedArray[2] = key[1];
		permutatedArray[3] = key[6];
		permutatedArray[4] = key[3];
		permutatedArray[5] = key[9];
		permutatedArray[6] = key[0];
		permutatedArray[7] = key[8];
		permutatedArray[8] = key[7];
		permutatedArray[9] = key[5];
		return permutatedArray;
	}

	public static boolean[] P8(boolean[] part1, boolean[] part2) {
		// Indice dans key : 1 2 3 4 5 6 7 8
		// Permutation : 5 2 6 3 7 4 9 8
		boolean[] permutatedArray = new boolean[8];
		permutatedArray[0] = part2[0];// 5
		permutatedArray[1] = part1[2];// 2
		permutatedArray[2] = part2[1];// 6
		permutatedArray[3] = part1[3];// 3
		permutatedArray[4] = part2[2];// 7
		permutatedArray[5] = part1[4];// 4
		permutatedArray[6] = part2[4];// 9
		permutatedArray[7] = part2[3];// 8
		return permutatedArray;
	}

	public static boolean[] EP(boolean[] input) {
		boolean[] permutatedArray = new boolean[8];
		permutatedArray[0] = input[3];
		permutatedArray[1] = input[0];
		permutatedArray[2] = input[1];
		permutatedArray[3] = input[2];
		permutatedArray[4] = input[1];
		permutatedArray[5] = input[2];
		permutatedArray[6] = input[3];
		permutatedArray[7] = input[0];
		return permutatedArray;
	}

	public static boolean[] P4(boolean[] part1, boolean[] part2) {
		// Indice dans key : 1 2 3 4
		// Permutation : 0,1 1,1 1,0 0,0
		boolean[] permutatedArray = new boolean[4];
		permutatedArray[0] = part1[1];// 0,1
		permutatedArray[1] = part2[1];// 1,1
		permutatedArray[2] = part2[0];// 1,0
		permutatedArray[3] = part1[0];// 0,0
		return permutatedArray;
	}

	public boolean[] F(boolean[] right, boolean[] sk) {
		// On applique ep sur right
		boolean[] a = EP(right);

		// On effectue un OU exclusif entre le résultat obtenu et la sous-clé sk
		boolean[] b = Utils.xor(a, sk);

		// On divise
		List<boolean[]> temp = Utils.splitBlock(b);

		// On effectue les opérations des sand-boxes sur chaque moitié obtenue
		boolean[] sb1 = getSandBoxes(temp.get(0), s_box1);
		boolean[] sb2 = getSandBoxes(temp.get(0), s_box2);

		// On applique P4 et on renvoie le résultat
		return P4(sb1, sb2);
	}

	public static boolean[] getSandBoxes(boolean[] block, boolean[][][] sandBox) {
		return sandBox[Utils.binstr2char(Utils.bin2str(block[0]) + Utils.bin2str(block[3]))][Utils
				.binstr2char(Utils.bin2str(block[1]) + Utils.bin2str(block[2]))];
	}

}
