package fr.exia.a4;

import java.util.ArrayList;
import java.util.List;

public class SDES_BitAsBoolean implements ICipher<String, String> {

	@Override
	public String encryption(String data, String key) {
		
		// On convertir la chaîne de données en tableau de boolean (des bits pour simplifier)
		boolean[] bits_block = BitAsBooleanUtils.byte2bits(data);
		
		// On convertit la chaîne de caractère de la clé en tableau de boolean (des bits pour simplifier)
		boolean[] masterKey = BitAsBooleanUtils.char2bin(key);
		if (masterKey.length != 10) {
			throw new IllegalArgumentException("Key must have a length of 10 chars");
		}
		
		return null;
	}

	


    public static List<boolean[]> generateKeys(boolean[] masterKey)
    {
    	// Tableau qui va contenir K1 et K2
        List<boolean[]> keys = new ArrayList<>(2);
        
        // On applique P10 et on sépare en deux tableaux de 5 bits
        List<boolean[]> temp = BitAsBooleanUtils.splitBlock(P10(masterKey));
        
        // On applique P8 sur 
        keys.add(P8(BitAsBooleanUtils.circularLeftShift(temp.get(0), 1), BitAsBooleanUtils.circularLeftShift(temp.get(1), 1)));
        keys.add(P8(BitAsBooleanUtils.circularLeftShift(temp.get(0), 3), BitAsBooleanUtils.circularLeftShift(temp.get(1), 3)));
        
        return keys;
    }
    
	
	
	public static boolean[] IP(boolean[] plainText) {
		// Indice dans key : 1 2 3 4 5 6 7 8
        // Permutation     : 2 6 3 1 4 8 5 7
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
        // Permutation     : 4 1 3 5 7 2 8 6
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
	
	/**
	 * La fonction de permutation est définie telle que :
	 * 	P10(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10) = (k3, k5, k2, k7, k4, k10, k1, k9, k8, k6)
	 * 
	 * @param key
	 * @return
	 */
    public static boolean[] P10(boolean[] key)
    {
    	// Indice dans key : 1 2 3 4 5 6  7 8 9 10
        // Permutation     : 3 5 2 7 4 10 1 9 8 6
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
    
    public static boolean[] P8(boolean[] part1, boolean[] part2)
    {
    	// Indice dans key : 1 2 3 4 5 6 7 8
        // Permutation     : 5 2 6 3 7 4 9 8
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
    
    /**
     *  E/P (n1, n2, n3, n4) = (n4, n1, n2, n3, n2, n3, n4, n1)
     * 
     * @param input
     * @return
     */
    public static boolean[] EP(boolean[] input)
    {
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
    
    
    
    /**
     * TODO Doc
     * 
     * @param part1
     * @param part2
     * @return
     */
    public static boolean[] P4(boolean[] part1, boolean[] part2)
    {
    	// Indice dans key : 1 2 3 4
        // Permutation     : 0,1 1,1 1,0 0,0
    	boolean[] permutatedArray = new boolean[4];
        permutatedArray[0] = part1[1];// 0,1
        permutatedArray[1] = part2[1];// 1,1
        permutatedArray[2] = part2[0];// 1,0
        permutatedArray[3] = part1[0];// 0,0
        return permutatedArray;
    }
    
    

    /**
     * 
     * @param right
     * @param sk sous-clé
     * @return
     */
    public static boolean[] F(boolean[] right, boolean[] sk)
    {
    	// On applique ep sur right
    	boolean[] a = EP(right);
    	
    	// On effectue un OU exclusif entre le résultat obtenu et la sous-clé sk
    	boolean[] b = BitAsBooleanUtils.xor(a, sk);
    	
    	// On divise 
    	List<boolean[]> temp = BitAsBooleanUtils.splitBlock(b);
    	
    	// On effectue les opérations des sand-boxes sur chaque moitié obtenue
    	sandBoxes(temp.get(0));
        
    }
    
    public static boolean[] getSandBoxes(boolean[] block, boolean[][][] sandBox)
    {
        return sandBox[BitAsBooleanUtils.binstr2char(bin2str(block[0]) + bin2str(block[3]))]
        		[BitAsBooleanUtils.binstr2char(bin2str(block[1]) + bin2str(block[2]))];
    }
    
    public static String bin2str(boolean value)
    {
    	return value ? "1" : "0";
    }

	@Override
	public String decryption(String data, String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
